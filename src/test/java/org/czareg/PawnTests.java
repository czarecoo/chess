package org.czareg;

import org.czareg.position.Position;
import org.czareg.position.PositionFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.czareg.Player.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PawnTests {

    @Test
    void givenWhitePawnDidNotMoveYet_whenGettingPossibleMoves_thenCanMoveByOneOrTwoRows() {
        // Given
        Game game = new ClassicGame();
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Position start = positionFactory.create(2, "A");
        Pawn pawn = new Pawn(WHITE);
        board.placePiece(start, pawn);

        // When
        MoveGenerator moveGenerator = new PawnMoveGenerator();
        Set<LegalMove> actualLegalMoves = moveGenerator.generate(game, start);

        // Then
        Set<LegalMove> expectedLegalMoves = Set.of(
                new LegalMove(pawn, start, positionFactory.create(3, "A")),
                new LegalMove(pawn, start, positionFactory.create(4, "A"))
        );
        assertEquals(expectedLegalMoves, actualLegalMoves);
    }

    @Test
    void givenWhitePawnDidMoveBefore_whenGettingPossibleMoves_thenCanMoveByOneRow() {
        // Given
        Game game = new ClassicGame();
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Pawn pawn = new Pawn(WHITE);
        board.placePiece(positionFactory.create(2, "D"), pawn);
        game.makeMove(new LegalMove(pawn, positionFactory.create(2, "D"), positionFactory.create(3, "D")));

        // When
        MoveGenerator moveGenerator = new PawnMoveGenerator();
        Set<LegalMove> actualLegalMoves = moveGenerator.generate(game, positionFactory.create(3, "D"));

        // Then
        Set<LegalMove> expectedLegalMoves = Set.of(
                new LegalMove(pawn, positionFactory.create(3, "D"), positionFactory.create(4, "D"))
        );
        assertEquals(expectedLegalMoves, actualLegalMoves);
    }

    @Test
    void givenWhitePawnOnLastRank_whenGettingPossibleMoves_thenNoLegalMoves() {
        // Given
        Game game = new ClassicGame();
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Pawn pawn = new Pawn(WHITE);
        board.placePiece(positionFactory.create(8, "H"), pawn);

        // When
        MoveGenerator moveGenerator = new PawnMoveGenerator();
        Set<LegalMove> actualLegalMoves = moveGenerator.generate(game, positionFactory.create(8, "H"));

        // Then
        assertEquals(Set.of(), actualLegalMoves);
    }
}
