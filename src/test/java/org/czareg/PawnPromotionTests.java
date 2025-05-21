package org.czareg;

import org.czareg.board.Board;
import org.czareg.game.*;
import org.czareg.move.MoveGenerator;
import org.czareg.piece.Pawn;
import org.czareg.piece.Queen;
import org.czareg.position.PositionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.czareg.game.Metadata.Key.PROMOTION_PIECE;
import static org.czareg.game.MoveType.PROMOTION;
import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class PawnPromotionTests {

    private Context context;
    private Board board;
    private PositionFactory pf;
    private MoveGenerator moveGenerator;
    private Game game;
    private History history;

    @BeforeEach
    void setUp() {
        context = new ClassicContext();
        board = context.getBoard();
        pf = board.getPositionFactory();
        moveGenerator = context.getMoveGenerator();
        game = context.getGame();
        history = context.getHistory();
    }

    @Test
    void givenWhitePawnOnSeventhRank_whenMovingForwardToEighthWithoutChoosingPromotionPiece_thenExceptionIsThrown() {
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(pf.create(7, "D"), whitePawn);
        Move promotionMove = moveGenerator
                .generate(context, pf.create(7, "D"), pf.create(8, "D"), PROMOTION)
                .orElseThrow();

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> game.makeMove(context, promotionMove));
        assertEquals("Promotion move missing chosen piece.", e.getMessage());
    }

    @Test
    void givenWhitePawnOnSeventhRank_whenMovingForwardToEighthAndChoosingPromotionPieceWithDifferentPlayer_thenExceptionIsThrown() {
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(pf.create(7, "D"), whitePawn);
        Move promotionMove = moveGenerator
                .generate(context, pf.create(7, "D"), pf.create(8, "D"), PROMOTION)
                .orElseThrow();
        promotionMove.getMetadata().put(PROMOTION_PIECE, new Queen(BLACK));

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> game.makeMove(context, promotionMove));
        assertTrue(e.getMessage().contains("Cannot promote to different player"));
    }

    @Test
    void givenWhitePawnOnSeventhRank_whenMovingForwardToEighth_thenPromotionIsGeneratedAndExecuted() {
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(pf.create(7, "D"), whitePawn);
        Move promotionMove = moveGenerator
                .generate(context, pf.create(7, "D"), pf.create(8, "D"), PROMOTION)
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

        Optional<Move> illegalPromotionAttempt = moveGenerator
                .generate(context, pf.create(6, "D"), pf.create(7, "D"), PROMOTION);

        assertTrue(illegalPromotionAttempt.isEmpty());
    }

}
