package org.czareg;

import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.pawn.PawnEnPassantMoveGenerator;
import org.czareg.piece.Pawn;
import org.czareg.position.IndexChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.czareg.game.Metadata.Key.CAPTURE_PIECE;
import static org.czareg.game.Metadata.Key.EN_PASSANT_CAPTURE_PIECE_POSITION;
import static org.czareg.game.MoveType.*;
import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PawnEnPassantTests extends BaseTests {

    private PieceMoveGenerator pieceMoveGenerator;

    @BeforeEach
    void setUp() {
        pieceMoveGenerator = new PawnEnPassantMoveGenerator();
    }

    @Test
    void givenWhitePawnAndTwoBlackPawns_whenEnPassantConditionsAreMet_thenEnPassantIsGeneratedAndExecutedCorrectly() {
        Pawn whitePawn = new Pawn(WHITE);
        Pawn blackPawnToBeCaptured = new Pawn(BLACK);
        Pawn blackPawnIrrelevantForTest = new Pawn(BLACK);
        board.placePiece(pf.create("D", 2), whitePawn);
        board.placePiece(pf.create("E", 7), blackPawnToBeCaptured);
        board.placePiece(pf.create("A", 7), blackPawnIrrelevantForTest);
        moveMaker.make(context, new Move(whitePawn, pf.create("D", 2), pf.create("D", 4), new Metadata(INITIAL_DOUBLE_FORWARD)));
        moveMaker.make(context, new Move(blackPawnIrrelevantForTest, pf.create("A", 7), pf.create("A", 6), new Metadata(MOVE)));
        moveMaker.make(context, new Move(whitePawn, pf.create("D", 4), pf.create("D", 5), new Metadata(MOVE)));
        moveMaker.make(context, new Move(blackPawnToBeCaptured, pf.create("E", 7), pf.create("E", 5), new Metadata(INITIAL_DOUBLE_FORWARD)));
        Move enPassantMove = pieceMoveGenerator.generate(context, whitePawn, pf.create("D", 5), new IndexChange(1, 1)).orElseThrow();

        moveMaker.make(context, enPassantMove);

        assertFalse(board.hasPiece(pf.create("E", 5)));
        assertEquals(whitePawn, board.getPiece(pf.create("E", 6)));
        Move lastMove = history.getLastPlayedMove().orElseThrow();
        assertEquals(enPassantMove, lastMove);
        Metadata expectedMetadata = new Metadata(EN_PASSANT)
                .put(CAPTURE_PIECE, blackPawnToBeCaptured)
                .put(EN_PASSANT_CAPTURE_PIECE_POSITION, pf.create("E", 5));
        Move expectedMove = new Move(whitePawn, pf.create("D", 5), pf.create("E", 6), expectedMetadata);
        assertEquals(expectedMove, enPassantMove);
    }
}
