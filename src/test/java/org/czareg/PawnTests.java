package org.czareg;

import org.czareg.board.Board;
import org.czareg.game.ClassicGame;
import org.czareg.game.Game;
import org.czareg.game.LegalMove;
import org.czareg.piece.Pawn;
import org.czareg.piece.move.MoveGenerator;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class PawnTests {

    @Test
    void givenWhitePawnDidNotMoveYet_whenGettingPossibleMoves_thenCanMoveByOneOrTwoRows() {
        Game game = new ClassicGame();
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Position start = positionFactory.create(2, "A");
        Pawn pawn = new Pawn(WHITE);
        board.placePiece(start, pawn);

        MoveGenerator moveGenerator = game.getMoveGenerator();
        Set<LegalMove> actualLegalMoves = moveGenerator.generate(game, start);

        Set<LegalMove> expectedLegalMoves = Set.of(
                new LegalMove(pawn, start, positionFactory.create(3, "A")),
                new LegalMove(pawn, start, positionFactory.create(4, "A"))
        );
        assertEquals(expectedLegalMoves, actualLegalMoves);
    }

    @Test
    void givenWhitePawnDidMoveBefore_whenGettingPossibleMoves_thenCanMoveByOneRow() {
        Game game = new ClassicGame();
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Pawn pawn = new Pawn(WHITE);
        board.placePiece(positionFactory.create(2, "D"), pawn);
        game.makeMove(new LegalMove(pawn, positionFactory.create(2, "D"), positionFactory.create(3, "D")));

        MoveGenerator moveGenerator = game.getMoveGenerator();
        Set<LegalMove> actualLegalMoves = moveGenerator.generate(game, positionFactory.create(3, "D"));

        Set<LegalMove> expectedLegalMoves = Set.of(
                new LegalMove(pawn, positionFactory.create(3, "D"), positionFactory.create(4, "D"))
        );
        assertEquals(expectedLegalMoves, actualLegalMoves);
    }

    @Test
    void givenWhitePawnOnLastRank_whenGettingPossibleMoves_thenNoLegalMoves() {
        Game game = new ClassicGame();
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Pawn pawn = new Pawn(WHITE);
        board.placePiece(positionFactory.create(8, "H"), pawn);

        MoveGenerator moveGenerator = game.getMoveGenerator();
        Set<LegalMove> actualLegalMoves = moveGenerator.generate(game, positionFactory.create(8, "H"));

        assertEquals(Set.of(), actualLegalMoves);
    }

    @Test
    void givenWhitePawnOnSeventhRank_whenGettingPossibleMoves_thenOneLegalMovesByOneRank() {
        Game game = new ClassicGame();
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Pawn pawn = new Pawn(WHITE);
        Position pawnStartingPosition = positionFactory.create(7, "B");
        board.placePiece(pawnStartingPosition, pawn);

        MoveGenerator moveGenerator = game.getMoveGenerator();
        Set<LegalMove> actualLegalMoves = moveGenerator.generate(game, pawnStartingPosition);

        assertEquals(Set.of(
                new LegalMove(pawn, pawnStartingPosition, positionFactory.create(8, "B"))
        ), actualLegalMoves);
    }

    /*
        cannot land on promotion rank from double forward move
     */
    @Test
    void givenWhitePawnOnSixthRank_whenGettingPossibleMoves_thenOneLegalMovesByOneRank() {

        Game game = new ClassicGame();
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Pawn pawn = new Pawn(WHITE);
        Position pawnStartingPosition = positionFactory.create(6, "E");
        board.placePiece(pawnStartingPosition, pawn);

        MoveGenerator moveGenerator = game.getMoveGenerator();
        Set<LegalMove> actualLegalMoves = moveGenerator.generate(game, pawnStartingPosition);

        assertEquals(Set.of(
                new LegalMove(pawn, pawnStartingPosition, positionFactory.create(7, "E"))
        ), actualLegalMoves);
    }

    @Test
    void givenEmptyBoard_whenBlackPawnIsMakingMove_thenExceptionIsThrown() {
        Game game = new ClassicGame();
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();

        LegalMove legalMove = new LegalMove(new Pawn(BLACK), positionFactory.create(1, "A"), positionFactory.create(2, "A"));
        assertThrows(IllegalArgumentException.class, () -> game.makeMove(legalMove));
    }

    @Test
    void givenEmptyBoard_whenWhitePawnIsMakingMove_thenExceptionIsThrown() {
        Game game = new ClassicGame();
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();

        LegalMove legalMove = new LegalMove(new Pawn(WHITE), positionFactory.create(1, "A"), positionFactory.create(2, "A"));
        assertThrows(IllegalArgumentException.class, () -> game.makeMove(legalMove));
    }

    @Test
    void givenTwoPlayers_whenPlayersAreMovingTheirPawnsOnTheSameFileUntilTheyFaceEachOther_thenThereAreNoMoreMoves() {
        Game game = new ClassicGame();
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(positionFactory.create(1, "C"), whitePawn);
        Pawn blackPawn = new Pawn(BLACK);
        board.placePiece(positionFactory.create(8, "C"), blackPawn);

        game.makeMove(new LegalMove(whitePawn, positionFactory.create(1, "C"), positionFactory.create(3, "C")));
        game.makeMove(new LegalMove(blackPawn, positionFactory.create(8, "C"), positionFactory.create(6, "C")));
        game.makeMove(new LegalMove(whitePawn, positionFactory.create(3, "C"), positionFactory.create(4, "C")));
        game.makeMove(new LegalMove(blackPawn, positionFactory.create(6, "C"), positionFactory.create(5, "C")));

        MoveGenerator moveGenerator = game.getMoveGenerator();
        assertEquals(Set.of(), moveGenerator.generate(game, positionFactory.create(4, "C")));
        assertEquals(Set.of(), moveGenerator.generate(game, positionFactory.create(5, "C")));
    }

    @Test
    void givenWhitePawnAndBlackPawnOnDiagonalRight_whenWhiteMoves_thenItCanCapture() {
        Game game = new ClassicGame();
        Board board = game.getBoard();
        PositionFactory pf = board.getPositionFactory();
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(pf.create(4, "C"), whitePawn);
        Pawn blackPawn = new Pawn(BLACK);
        board.placePiece(pf.create(5, "D"), blackPawn);

        MoveGenerator moveGenerator = game.getMoveGenerator();
        Set<LegalMove> whiteMoves = moveGenerator.generate(game, pf.create(4, "C"));

        Position expectedCapturePosition = pf.create(5, "D");
        assertTrue(whiteMoves.stream()
                .anyMatch(move -> move.getEnd().equals(expectedCapturePosition)
                        && move.getPiece().equals(whitePawn)));
    }

    @Test
    void givenBlackPawnAndWhitePawnOnDiagonalLeft_whenBlackMoves_thenItCanCapture() {
        Game game = new ClassicGame();
        Board board = game.getBoard();
        PositionFactory pf = board.getPositionFactory();
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(pf.create(2, "A"), whitePawn);
        Pawn blackPawn = new Pawn(BLACK);
        board.placePiece(pf.create(3, "B"), blackPawn);


        MoveGenerator moveGenerator = game.getMoveGenerator();
        Set<LegalMove> blackMoves = moveGenerator.generate(game, pf.create(3, "B"));

        Position expectedCapturePosition = pf.create(2, "A");
        assertTrue(blackMoves.stream()
                .anyMatch(move -> move.getEnd().equals(expectedCapturePosition)
                        && move.getPiece().equals(blackPawn)));
    }
}
