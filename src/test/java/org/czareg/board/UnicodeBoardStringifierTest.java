package org.czareg.board;

import org.czareg.game.ClassicContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnicodeBoardStringifierTest {

    @Test
    void shouldPrintAllPiecesOnStartingPlacesOnBoard() {
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
}