package org.czareg.game;

import org.czareg.BaseTests;
import org.czareg.board.ClassicPieceStartingPositionPlacer;
import org.czareg.board.PiecePlacer;
import org.czareg.piece.King;
import org.czareg.piece.Knight;
import org.czareg.position.Position;
import org.junit.jupiter.api.Test;

import static org.czareg.game.MoveType.*;
import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassicStateAnalyzerTest extends BaseTests {

    @Test
    void givenBlackKingSurroundedByBlackKnightsInTheCorner_whenWhiteKnightAttacks_thenItsCheckMate() {
        board.placePiece(pf.create(1, "H"), new King(WHITE));
        board.placePiece(pf.create(8, "A"), new King(BLACK));
        board.placePiece(pf.create(7, "A"), new Knight(BLACK));
        board.placePiece(pf.create(7, "B"), new Knight(BLACK));
        board.placePiece(pf.create(8, "B"), new Knight(BLACK));
        Position knightStartingPosition = pf.create(5, "D");
        Knight whiteKnight = new Knight(WHITE);
        board.placePiece(knightStartingPosition, whiteKnight);
        Position knightEndPosition = pf.create(7, "C");
        moveMaker.make(context, new Move(whiteKnight, knightStartingPosition, knightEndPosition, new Metadata(MOVE)));

        boolean isCheckMate = stateAnalyzer.isCheckMate(context);

        assertTrue(isCheckMate);
    }

    @Test
    void givenQuickScholarsMateGame_whenTheLastMoveIsMade_thenItsFinallyCheckMate() {
        assertFalse(stateAnalyzer.isCheckMate(context));
        PiecePlacer piecePlacer = new ClassicPieceStartingPositionPlacer();
        piecePlacer.place(board);
        assertFalse(stateAnalyzer.isCheckMate(context));
        moveMaker.make(context, new Move(board.getPiece(pf.create(2, "E")), pf.create(2, "E"), pf.create(4, "E"), new Metadata(INITIAL_DOUBLE_FORWARD)));
        assertFalse(stateAnalyzer.isCheckMate(context));
        moveMaker.make(context, new Move(board.getPiece(pf.create(7, "H")), pf.create(7, "H"), pf.create(6, "H"), new Metadata(MOVE)));
        assertFalse(stateAnalyzer.isCheckMate(context));
        moveMaker.make(context, new Move(board.getPiece(pf.create(1, "F")), pf.create(1, "F"), pf.create(4, "C"), new Metadata(MOVE)));
        assertFalse(stateAnalyzer.isCheckMate(context));
        moveMaker.make(context, new Move(board.getPiece(pf.create(7, "B")), pf.create(7, "B"), pf.create(6, "B"), new Metadata(MOVE)));
        assertFalse(stateAnalyzer.isCheckMate(context));
        moveMaker.make(context, new Move(board.getPiece(pf.create(1, "D")), pf.create(1, "D"), pf.create(3, "F"), new Metadata(MOVE)));
        assertFalse(stateAnalyzer.isCheckMate(context));
        moveMaker.make(context, new Move(board.getPiece(pf.create(7, "A")), pf.create(7, "A"), pf.create(6, "A"), new Metadata(MOVE)));
        assertFalse(stateAnalyzer.isCheckMate(context));
        moveMaker.make(context, new Move(board.getPiece(pf.create(3, "F")), pf.create(3, "F"), pf.create(7, "F"), new Metadata(CAPTURE).put(Metadata.Key.CAPTURE_PIECE, board.getPiece(pf.create(7, "F")))));
        assertTrue(stateAnalyzer.isCheckMate(context));
    }
}