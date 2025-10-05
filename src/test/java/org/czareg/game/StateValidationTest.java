package org.czareg.game;

import org.czareg.BaseTests;
import org.czareg.board.ClassicPieceStartingPositionPlacer;
import org.czareg.board.PiecePlacer;
import org.czareg.piece.King;
import org.czareg.piece.Knight;
import org.czareg.piece.Piece;
import org.czareg.position.Position;
import org.junit.jupiter.api.Test;

import static org.czareg.game.Metadata.Key.CAPTURE_PIECE;
import static org.czareg.game.MoveType.*;
import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StateValidationTest extends BaseTests {

    @Test
    void givenBlackCheckmated_whenTryingToMoveAfter_thenValidationThrowsCheckMate() {
        board.placePiece(pf.create("H", 1), new King(WHITE));
        board.placePiece(pf.create("A", 8), new King(BLACK));
        board.placePiece(pf.create("A", 7), new Knight(BLACK));
        board.placePiece(pf.create("B", 7), new Knight(BLACK));
        board.placePiece(pf.create("B", 8), new Knight(BLACK));
        Position knightStartingPosition = pf.create("D", 5);
        Knight whiteKnight = new Knight(WHITE);
        board.placePiece(knightStartingPosition, whiteKnight);
        Position knightEndPosition = pf.create("C", 7);
        Move checkMatingMove = new Move(whiteKnight, knightStartingPosition, knightEndPosition, new Metadata(MOVE));
        moveMaker.make(context, checkMatingMove);

        Move afterCheckMate = new Move(board.getPiece(pf.create("A", 7)), pf.create("A", 7), pf.create("B", 5), new Metadata(MOVE));
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> moveMaker.make(context, afterCheckMate));
        assertEquals("There are no legal moves available.", e.getMessage());
    }

    @Test
    void givenQuickScholarsMateGame_whenTheLastMoveIsMade_thenItsFinallyCheckMate() {
        PiecePlacer piecePlacer = new ClassicPieceStartingPositionPlacer();
        piecePlacer.place(board);
        moveMaker.make(context, new Move(board.getPiece(pf.create("E", 2)), pf.create("E", 2), pf.create("E", 4), new Metadata(INITIAL_DOUBLE_FORWARD)));
        moveMaker.make(context, new Move(board.getPiece(pf.create("H", 7)), pf.create("H", 7), pf.create("H", 6), new Metadata(MOVE)));
        moveMaker.make(context, new Move(board.getPiece(pf.create("F", 1)), pf.create("F", 1), pf.create("C", 4), new Metadata(MOVE)));
        moveMaker.make(context, new Move(board.getPiece(pf.create("B", 7)), pf.create("B", 7), pf.create("B", 6), new Metadata(MOVE)));
        moveMaker.make(context, new Move(board.getPiece(pf.create("D", 1)), pf.create("D", 1), pf.create("F", 3), new Metadata(MOVE)));
        moveMaker.make(context, new Move(board.getPiece(pf.create("A", 7)), pf.create("A", 7), pf.create("A", 6), new Metadata(MOVE)));
        moveMaker.make(context, new Move(board.getPiece(pf.create("F", 3)), pf.create("F", 3), pf.create("F", 7), new Metadata(CAPTURE).put(CAPTURE_PIECE, board.getPiece(pf.create("F", 7)))));

        Move afterCheckMate = new Move(board.getPiece(pf.create("A", 6)), pf.create("A", 6), pf.create("A", 5), new Metadata(MOVE));
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> moveMaker.make(context, afterCheckMate));
        assertEquals("There are no legal moves available.", e.getMessage());
    }

    @Test
    void givenBothPlayersMoveOnlyKnights_whenOver100TotalMovesAreDone_thenDrawByInsufficientMaterial() {
        PiecePlacer piecePlacer = new ClassicPieceStartingPositionPlacer();
        piecePlacer.place(board);
        Position b1 = pf.create("B", 1);
        Position a3 = pf.create("A", 3);
        Position b8 = pf.create("B", 8);
        Position a6 = pf.create("A", 6);
        Piece whiteKnight = board.getPiece(b1);
        Move whiteKnightMoveForward = new Move(whiteKnight, b1, a3, new Metadata(MOVE));
        Move whiteKnightMoveBackward = new Move(whiteKnight, a3, b1, new Metadata(MOVE));
        Piece blackKnight = board.getPiece(b8);
        Move blackKnightMoveForward = new Move(blackKnight, b8, a6, new Metadata(MOVE));
        Move blackKnightMoveBackward = new Move(blackKnight, a6, b8, new Metadata(MOVE));
        for (int i = 0; i < 25; i++) {
            moveMaker.make(context, whiteKnightMoveForward);
            moveMaker.make(context, blackKnightMoveForward);
            moveMaker.make(context, whiteKnightMoveBackward);
            moveMaker.make(context, blackKnightMoveBackward);
        }

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> moveMaker.make(context, whiteKnightMoveForward));

        assertEquals("Drawn by 50 move rule.", e.getMessage());
    }
}