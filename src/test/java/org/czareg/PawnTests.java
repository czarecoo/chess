package org.czareg;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.czareg.Player.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PawnTests {

    @Test
    void givenWhitePawnDidNotMoveYet_whenGettingPossibleMoves_thenCanMoveByOneOrTwoRows() {
        // Given
        Board board = new ClassicBoard();
        Game game = new ClassicGame(board);
        ClassicPosition start = ClassicPosition.from(2,"A");
        Pawn pawn = new Pawn(WHITE);
        board.placePiece(start, pawn);

        // When
        MoveGenerator moveGenerator = new PawnMoveGenerator();
        Set<LegalMove> actualLegalMoves = moveGenerator.generate(game, board, pawn, start);

        // Then
        Set<LegalMove> expectedLegalMoves = Set.of(
                new LegalMove(pawn, start, ClassicPosition.from(3,"A")),
                new LegalMove(pawn, start, ClassicPosition.from(4,"A"))
        );
        assertEquals(expectedLegalMoves, actualLegalMoves);
    }

    @Test
    void givenWhitePawnDidMoveBefore_whenGettingPossibleMoves_thenCanMoveByOneRow() {
        // Given
        Board board = new ClassicBoard();
        Game game = new ClassicGame(board);
        Pawn pawn = new Pawn(WHITE);
        board.placePiece(ClassicPosition.from(2,"D"), pawn);
        game.makeMove(new LegalMove(pawn, ClassicPosition.from(2,"D"), ClassicPosition.from(3,"D")));

        // When
        MoveGenerator moveGenerator = new PawnMoveGenerator();
        Set<LegalMove> actualLegalMoves = moveGenerator.generate(game, board, pawn, ClassicPosition.from(3,"D"));

        // Then
        Set<LegalMove> expectedLegalMoves = Set.of(
                new LegalMove(pawn, ClassicPosition.from(3,"D"), ClassicPosition.from(4,"D"))
        );
        assertEquals(expectedLegalMoves, actualLegalMoves);
    }

    @Test
    void givenWhitePawnOnLastRank_whenGettingPossibleMoves_thenNoLegalMoves() {
        // Given
        Board board = new ClassicBoard();
        Game game = new ClassicGame(board);
        Pawn pawn = new Pawn(WHITE);
        board.placePiece(ClassicPosition.from(8,"H"), pawn);

        // When
        MoveGenerator moveGenerator = new PawnMoveGenerator();
        Set<LegalMove> actualLegalMoves = moveGenerator.generate(game, board, pawn, ClassicPosition.from(8,"H"));

        // Then
        assertEquals(Set.of(), actualLegalMoves);
    }
}
