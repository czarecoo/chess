package org.czareg;

import org.czareg.board.Board;
import org.czareg.game.*;
import org.czareg.piece.Pawn;
import org.czareg.piece.move.MoveGenerator;
import org.czareg.position.PositionFactory;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PawnEnPassantTests {

    @Test
    void givenWhitePawnAndTwoBlackPawns_whenEnPassantConditionsAreMet_thenEnPassantIsGeneratedAndExecutedCorrectly() {
        Game game = new ClassicGame();
        Board board = game.getBoard();
        PositionFactory pf = board.getPositionFactory();
        Pawn whitePawn = new Pawn(WHITE);
        Pawn blackPawnToBeCaptured = new Pawn(BLACK);
        Pawn blackPawnIrrelevantForTest = new Pawn(BLACK);
        board.placePiece(pf.create(2, "D"), whitePawn);
        board.placePiece(pf.create(7, "E"), blackPawnToBeCaptured);
        board.placePiece(pf.create(7, "A"), blackPawnIrrelevantForTest);
        game.makeMove(new Move(whitePawn, pf.create(2, "D"), pf.create(4, "D")));
        game.makeMove(new Move(blackPawnIrrelevantForTest, pf.create(7, "A"), pf.create(6, "A")));
        game.makeMove(new Move(whitePawn, pf.create(4, "D"), pf.create(5, "D")));
        Move blackDoubleForwardMove = new Move(blackPawnToBeCaptured, pf.create(7, "E"), pf.create(5, "E"));
        blackDoubleForwardMove.getMetadata().put(Metadata.Key.MOVE_TYPE, MoveType.PAWN_DOUBLE_FORWARD);
        game.makeMove(blackDoubleForwardMove);
        MoveGenerator moveGenerator = game.getMoveGenerator();
        Stream<Move> moves = moveGenerator.generate(game, pf.create(5, "D"));
        Move enPassantMove = moves
                .filter(m -> m.getMetadata().isExactly(Metadata.Key.MOVE_TYPE, MoveType.EN_PASSANT))
                .findFirst()
                .orElseThrow();

        game.makeMove(enPassantMove);

        assertFalse(board.hasPiece(pf.create(5, "E")));
        assertEquals(whitePawn, board.getPiece(pf.create(6, "E")));
        Move lastMove = game.getHistory().getLastPlayedMove().orElseThrow();
        assertEquals(enPassantMove, lastMove);
        Metadata expectedMetadata = new Metadata();
        Move expectedMove = new Move(whitePawn, pf.create(5, "D"), pf.create(6, "E"), expectedMetadata);
        assertEquals(expectedMove, enPassantMove);
    }
}
