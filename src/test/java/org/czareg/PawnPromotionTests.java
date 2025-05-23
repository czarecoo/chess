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

import static org.czareg.game.Metadata.Key.PROMOTION_PIECE;
import static org.czareg.game.MoveType.PROMOTION;
import static org.czareg.piece.Player.BLACK;
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
        board.placePiece(pf.create(7, "D"), whitePawn);
        Move promotionMove = pieceMoveGenerator
                .generate(context, whitePawn, pf.create(7, "D"), new IndexChange(1, 0))
                .orElseThrow();

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> game.makeMove(context, promotionMove));
        assertEquals("Promotion move missing chosen piece.", e.getMessage());
    }

    @Test
    void givenWhitePawnOnSeventhRank_whenMovingForwardToEighthAndChoosingPromotionPieceWithDifferentPlayer_thenExceptionIsThrown() {
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(pf.create(7, "D"), whitePawn);
        Move promotionMove = pieceMoveGenerator
                .generate(context, whitePawn, pf.create(7, "D"), new IndexChange(1, 0))
                .orElseThrow();
        promotionMove.getMetadata().put(PROMOTION_PIECE, new Queen(BLACK));

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> game.makeMove(context, promotionMove));
        assertTrue(e.getMessage().contains("Cannot promote to different player"));
    }

    @Test
    void givenWhitePawnOnSeventhRank_whenMovingForwardToEighth_thenPromotionIsGeneratedAndExecuted() {
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(pf.create(7, "D"), whitePawn);
        Move promotionMove = pieceMoveGenerator
                .generate(context, whitePawn, pf.create(7, "D"), new IndexChange(1, 0))
                .orElseThrow();
        Queen newPiece = new Queen(WHITE);
        promotionMove.getMetadata().put(PROMOTION_PIECE, newPiece);

        game.makeMove(context, promotionMove);

        assertEquals(WHITE, board.getPiece(pf.create(8, "D")).getPlayer());
        assertInstanceOf(Queen.class, board.getPiece(pf.create(8, "D")));
        assertFalse(board.hasPiece(pf.create(7, "D")));

        Move lastMove = history.getLastPlayedMove().orElseThrow();
        assertEquals(promotionMove, lastMove);
        Metadata expectedMetadata = new Metadata(PROMOTION)
                .put(PROMOTION_PIECE, newPiece);
        Move expectedMove = new Move(whitePawn, pf.create(7, "D"), pf.create(8, "D"), expectedMetadata);
        assertEquals(expectedMove, promotionMove);
    }

    @Test
    void givenWhitePawnNotOnSeventhRank_whenTryingPromotion_thenPromotionIsRejected() {
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(pf.create(6, "D"), whitePawn);

        Optional<Move> illegalPromotionAttempt = pieceMoveGenerator
                .generate(context, whitePawn, pf.create(6, "D"), new IndexChange(1, 0));

        assertTrue(illegalPromotionAttempt.isEmpty());
    }

}
