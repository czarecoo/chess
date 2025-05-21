package org.czareg;

import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.piece.Pawn;
import org.junit.jupiter.api.Test;

import static org.czareg.game.Metadata.Key.CAPTURE_PIECE;
import static org.czareg.game.Metadata.Key.EN_PASSANT_CAPTURE_PIECE_POSITION;
import static org.czareg.game.MoveType.*;
import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PawnEnPassantTests extends BaseTests {

    @Test
    void givenWhitePawnAndTwoBlackPawns_whenEnPassantConditionsAreMet_thenEnPassantIsGeneratedAndExecutedCorrectly() {
        Pawn whitePawn = new Pawn(WHITE);
        Pawn blackPawnToBeCaptured = new Pawn(BLACK);
        Pawn blackPawnIrrelevantForTest = new Pawn(BLACK);
        board.placePiece(pf.create(2, "D"), whitePawn);
        board.placePiece(pf.create(7, "E"), blackPawnToBeCaptured);
        board.placePiece(pf.create(7, "A"), blackPawnIrrelevantForTest);
        game.makeMove(context, new Move(whitePawn, pf.create(2, "D"), pf.create(4, "D"), new Metadata(INITIAL_DOUBLE_FORWARD)));
        game.makeMove(context, new Move(blackPawnIrrelevantForTest, pf.create(7, "A"), pf.create(6, "A"), new Metadata(MOVE)));
        game.makeMove(context, new Move(whitePawn, pf.create(4, "D"), pf.create(5, "D"), new Metadata(MOVE)));
        game.makeMove(context, new Move(blackPawnToBeCaptured, pf.create(7, "E"), pf.create(5, "E"), new Metadata(INITIAL_DOUBLE_FORWARD)));
        Move enPassantMove = moveGenerator.generate(context, pf.create(5, "D"), pf.create(6, "E"), EN_PASSANT).orElseThrow();

        game.makeMove(context, enPassantMove);

        assertFalse(board.hasPiece(pf.create(5, "E")));
        assertEquals(whitePawn, board.getPiece(pf.create(6, "E")));
        Move lastMove = history.getLastPlayedMove().orElseThrow();
        assertEquals(enPassantMove, lastMove);
        Metadata expectedMetadata = new Metadata(EN_PASSANT)
                .put(CAPTURE_PIECE, blackPawnToBeCaptured)
                .put(EN_PASSANT_CAPTURE_PIECE_POSITION, pf.create(5, "E"));
        Move expectedMove = new Move(whitePawn, pf.create(5, "D"), pf.create(6, "E"), expectedMetadata);
        assertEquals(expectedMove, enPassantMove);
    }
}
