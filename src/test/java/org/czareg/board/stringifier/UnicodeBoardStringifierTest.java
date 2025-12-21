package org.czareg.board.stringifier;

import org.czareg.board.Board;
import org.czareg.board.BoardStringifier;
import org.czareg.board.PiecePlacer;
import org.czareg.board.placers.ClassicPieceStartingPositionPlacer;
import org.czareg.board.placers.PromotionTestingPieceStartingPositionPlacer;
import org.czareg.game.ClassicContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnicodeBoardStringifierTest {

    @Test
    void shouldPrintAllPiecesOnClassingStartingPlaces() {
        ClassicContext classicContext = ClassicContext.create();
        Board board = classicContext.getBoard();
        PiecePlacer piecePlacer = new ClassicPieceStartingPositionPlacer();
        piecePlacer.place(board);
        BoardStringifier boardStringifier = new UnicodeBoardStringifier();

        String stringifiedBoard = boardStringifier.stringify(board);

        String expected = """
                ♜ ♞ ♝ ♛ ♚ ♝ ♞ ♜\s
                ♟ ♟ ♟ ♟ ♟ ♟ ♟ ♟\s
                　 　 　 　 　 　 　 　\s
                　 　 　 　 　 　 　 　\s
                　 　 　 　 　 　 　 　\s
                　 　 　 　 　 　 　 　\s
                ♙ ♙ ♙ ♙ ♙ ♙ ♙ ♙\s
                ♖ ♘ ♗ ♕ ♔ ♗ ♘ ♖\s
                """;
        assertEquals(expected, stringifiedBoard);
        System.out.println(stringifiedBoard);
    }

    @Test
    void shouldPrintAllPiecesOnPromotionTestingStartingPlaces() {
        ClassicContext classicContext = ClassicContext.create();
        Board board = classicContext.getBoard();
        PiecePlacer piecePlacer = new PromotionTestingPieceStartingPositionPlacer();
        piecePlacer.place(board);
        BoardStringifier boardStringifier = new UnicodeBoardStringifier();

        String stringifiedBoard = boardStringifier.stringify(board);

        String expected = """
                ♜ ♞ ♝ ♛ 　 ♝ ♞ ♜\s
                ♙ ♙ ♙ ♙ ♙ ♙ ♙ ♙\s
                　 　 　 　 　 　 　 　\s
                　 　 　 　 　 　 　 ♚\s
                ♔ 　 　 　 　 　 　 　\s
                　 　 　 　 　 　 　 　\s
                ♟ ♟ ♟ ♟ ♟ ♟ ♟ ♟\s
                ♖ ♘ ♗ ♕ 　 ♗ ♘ ♖\s
                """;
        assertEquals(expected, stringifiedBoard);
        System.out.println(stringifiedBoard);
    }
}