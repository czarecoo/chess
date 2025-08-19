package org.czareg;

import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.pawn.PawnPromotionMoveGenerator;
import org.czareg.piece.Pawn;
import org.czareg.piece.Queen;
import org.czareg.position.IndexChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.czareg.game.Metadata.Key.PROMOTION_PIECE_CLASS;
import static org.czareg.game.MoveType.PROMOTION;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class PawnPromotionTests extends BaseTests {

    private PieceMoveGenerator pieceMoveGenerator;

    @BeforeEach
    void setUp() {
        pieceMoveGenerator = new PawnPromotionMoveGenerator();
    }

    @Test
    void givenWhitePawnOnSeventhRank_whenMovingForwardToEighthWithoutChoosingPromotionPiece_thenExceptionIsThrown() {
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(pf.create("D", 7), whitePawn);
        Move promotionMove = pieceMoveGenerator
                .generate(context, whitePawn, pf.create("D", 7), new IndexChange(0, 1))
                .orElseThrow();

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> moveMaker.make(context, promotionMove));
        assertEquals("Move is not legal Move(piece=Pawn(player=WHITE), start=Position(file=D, rank=7), end=Position(file=D, rank=8), metadata=Metadata(data={MOVE_TYPE=PROMOTION}))", e.getMessage());
    }

    @Test
    void givenWhitePawnOnSeventhRank_whenMovingForwardToEighth_thenPromotionIsGeneratedAndExecuted() {
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(pf.create("D", 7), whitePawn);
        Move promotionMove = pieceMoveGenerator
                .generate(context, whitePawn, pf.create("D", 7), new IndexChange(0, 1))
                .orElseThrow();
        promotionMove.getMetadata().put(PROMOTION_PIECE_CLASS, Queen.class);

        moveMaker.make(context, promotionMove);

        assertEquals(WHITE, board.getPiece(pf.create("D", 8)).getPlayer());
        assertInstanceOf(Queen.class, board.getPiece(pf.create("D", 8)));
        assertFalse(board.hasPiece(pf.create("D", 7)));

        Move lastMove = history.getLastPlayedMove().orElseThrow();
        assertEquals(promotionMove, lastMove);
        Metadata expectedMetadata = new Metadata(PROMOTION)
                .put(PROMOTION_PIECE_CLASS, Queen.class);
        Move expectedMove = new Move(whitePawn, pf.create("D", 7), pf.create("D", 8), expectedMetadata);
        assertEquals(expectedMove, promotionMove);
    }

    @Test
    void givenWhitePawnNotOnSeventhRank_whenTryingPromotion_thenPromotionIsRejected() {
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(pf.create("D", 6), whitePawn);

        Optional<Move> illegalPromotionAttempt = pieceMoveGenerator
                .generate(context, whitePawn, pf.create("D", 6), new IndexChange(0, 1));

        assertTrue(illegalPromotionAttempt.isEmpty());
    }

}
