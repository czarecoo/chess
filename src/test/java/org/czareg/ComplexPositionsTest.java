package org.czareg;

import org.czareg.game.GeneratedMoves;
import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.game.state.State;
import org.czareg.piece.*;
import org.czareg.position.Position;
import org.junit.jupiter.api.Test;

import static org.czareg.game.MoveType.MOVE;
import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class ComplexPositionsTest extends ClassicContextTests {

    @Test
    void givenComplexPosition_whenWhiteCheckmatesBlack_thenBlackHasNoMoves() {
        place(BLACK, Rook.class, "C", 1);
        place(WHITE, Bishop.class, "B", 1);
        place(WHITE, King.class, "A", 1);
        place(WHITE, Pawn.class, "A", 2);
        place(BLACK, Pawn.class, "B", 4);
        place(BLACK, Queen.class, "A", 4);
        place(WHITE, Pawn.class, "G", 5);
        place(WHITE, Pawn.class, "F", 5);
        place(BLACK, Pawn.class, "D", 5);
        place(BLACK, Pawn.class, "D", 6);
        place(BLACK, King.class, "G", 7);
        place(BLACK, Pawn.class, "F", 7);
        place(WHITE, Rook.class, "B", 7);
        place(WHITE, Rook.class, "D", 8);
        Position startingPosition = pf.create("F", 5);
        Position endPosition = pf.create("F", 6);
        Piece pawn = board.getPiece(startingPosition);
        Move checkMatingMove = new Move(pawn, startingPosition, endPosition, new Metadata(MOVE));
        moveMaker.make(context, checkMatingMove);

        GeneratedMoves generatedMoves = moveGenerators.getOrGenerateLegal(context);
        assertTrue(generatedMoves.isEmpty());
        State state = context.getStateChecker().check(context);
        assertInstanceOf(State.Win.class, state);
        assertEquals(WHITE, ((State.Win) state).winner());
    }

    private void place(Player white, Class<? extends Piece> piece, String file, int rank) {
        board.placePiece(pf.create(file, rank), Piece.create(piece, white));
    }
}