package org.czareg;

import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.pawn.PawnPromotionCaptureMoveGenerator;
import org.czareg.piece.Bishop;
import org.czareg.piece.Knight;
import org.czareg.piece.Pawn;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.czareg.game.Metadata.Key.CAPTURE_PIECE;
import static org.czareg.game.Metadata.Key.PROMOTION_PIECE_CLASS;
import static org.czareg.game.MoveType.PROMOTION_CAPTURE;
import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class PawnPromotionCaptureTests extends BaseTests {

    private PieceMoveGenerator pieceMoveGenerator;

    @BeforeEach
    void setUp() {
        pieceMoveGenerator = new PawnPromotionCaptureMoveGenerator();
    }

    @Test
    void givenWhitePawnOnSeventhRank_whenCapturingLeftToEighth_thenPromotionIsGeneratedAndExecuted() {
        Knight blackKnight = new Knight(BLACK);
        Position c8 = pf.create("C", 8);
        board.placePiece(c8, blackKnight);
        Pawn whitePawn = new Pawn(WHITE);
        Position d7 = pf.create("D", 7);
        board.placePiece(d7, whitePawn);
        Move promotionCaptureMove = pieceMoveGenerator
                .generate(context, whitePawn, d7, new IndexChange(-1, 1))
                .orElseThrow();
        promotionCaptureMove.getMetadata().put(PROMOTION_PIECE_CLASS, Bishop.class);

        moveMaker.make(context, promotionCaptureMove);

        assertEquals(WHITE, board.getPiece(c8).getPlayer());
        assertInstanceOf(Bishop.class, board.getPiece(c8));
        assertFalse(board.hasPiece(d7));

        Move lastMove = history.getLastPlayedMove().orElseThrow();
        assertEquals(promotionCaptureMove, lastMove);
        Metadata expectedMetadata = new Metadata(PROMOTION_CAPTURE)
                .put(PROMOTION_PIECE_CLASS, Bishop.class)
                .put(CAPTURE_PIECE, blackKnight);
        Move expectedMove = new Move(whitePawn, d7, c8, expectedMetadata);
        assertEquals(expectedMove, promotionCaptureMove);
    }

}
