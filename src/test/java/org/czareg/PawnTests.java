package org.czareg;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.czareg.Player.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PawnTests {

    @Test
    void givenWhitePawnOnStartingRow_whenGettingPossibleMoves_thenCanMoveOneOrTwoSquaresForward() {
        // Given
        Board board = new ClassicBoard();
        Position start = new Position(new Rank(2),new File("D"));
        Pawn pawn = new Pawn(WHITE, start);
        board.placePiece(pawn, start);

        // When
        Set<Move> actualMoves = pawn.getPotentialMoves(start);

        // Then
        Set<Move> expectedMoves = Set.of(
                new PawnForwardMove(new IndexPosition(2,3)),
                new PawnForwardMove(new IndexPosition(3,3))
        );
        assertEquals(expectedMoves, actualMoves);

        // When
        MoveGenerator moveGenerator = new PawnMoveGenerator();
        Set<LegalMove> actualLegalMoves = moveGenerator.generate(board, pawn, start);

        // Then
        Set<LegalMove> expectedLegalMoves = Set.of(
                new LegalMove(pawn, start, new Position(new Rank(3), new File("D"))),
                new LegalMove(pawn, start, new Position(new Rank(4), new File("D")))
        );
        assertEquals(expectedLegalMoves, actualLegalMoves);
    }
}
