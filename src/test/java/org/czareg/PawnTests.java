package org.czareg;

import org.czareg.board.Board;
import org.czareg.game.ClassicGame;
import org.czareg.game.Game;
import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.piece.Pawn;
import org.czareg.piece.move.MoveGenerator;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.czareg.game.Metadata.Key.*;
import static org.czareg.game.MoveType.*;
import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class PawnTests {

    private Game game;
    private Board board;
    private PositionFactory pf;
    private MoveGenerator moveGenerator;

    @BeforeEach
    void setUp() {
        game = new ClassicGame();
        board = game.getBoard();
        pf = board.getPositionFactory();
        moveGenerator = game.getMoveGenerator();
    }

    @Test
    void givenWhitePawnDidNotMoveYet_whenGettingPossibleMoves_thenCanMoveByOneOrTwoRows() {
        Pawn pawn = new Pawn(WHITE);
        Position start = pf.create(2, "A");
        board.placePiece(start, pawn);

        Set<Move> actualMoves = moveGenerator.generate(game, start).collect(Collectors.toSet());

        Set<Move> expectedMoves = Set.of(
                new Move(pawn, start, pf.create(3, "A"), new Metadata().put(MOVE_TYPE, PAWN_FORWARD)),
                new Move(pawn, start, pf.create(4, "A"), new Metadata().put(MOVE_TYPE, PAWN_DOUBLE_FORWARD))
        );
        assertEquals(expectedMoves, actualMoves);
    }

    @Test
    void givenWhitePawnDidMoveBefore_whenGettingPossibleMoves_thenCanMoveByOneRow() {
        Pawn pawn = new Pawn(WHITE);
        Position whiteStart = pf.create(2, "D");
        board.placePiece(whiteStart, pawn);
        game.makeMove(new Move(pawn, whiteStart, pf.create(3, "D"), new Metadata().put(MOVE_TYPE, PAWN_FORWARD)));

        MoveGenerator moveGenerator = game.getMoveGenerator();
        Set<Move> actualMoves = moveGenerator.generate(game, pf.create(3, "D")).collect(Collectors.toSet());

        Set<Move> expectedMoves = Set.of(new Move(pawn, pf.create(3, "D"), pf.create(4, "D"), new Metadata().put(MOVE_TYPE, PAWN_FORWARD)));
        assertEquals(expectedMoves, actualMoves);
    }

    @Test
    void givenWhitePawnOnLastRank_whenGettingPossibleMoves_thenNoLegalMoves() {
        Pawn pawn = new Pawn(WHITE);
        Position start = pf.create(8, "H");
        board.placePiece(start, pawn);

        MoveGenerator moveGenerator = game.getMoveGenerator();
        Set<Move> actualMoves = moveGenerator.generate(game, start).collect(Collectors.toSet());

        assertEquals(Set.of(), actualMoves);
    }

    @Test
    void givenWhitePawnOnSeventhRank_whenGettingPossibleMoves_thenOneLegalMovesByOneRank() {
        Pawn pawn = new Pawn(WHITE);
        Position start = pf.create(7, "B");
        board.placePiece(start, pawn);

        MoveGenerator moveGenerator = game.getMoveGenerator();
        Set<Move> actualMoves = moveGenerator.generate(game, start).collect(Collectors.toSet());

        Set<Move> expectedMoves = Set.of(new Move(pawn, start, pf.create(8, "B"), new Metadata().put(MOVE_TYPE, PAWN_FORWARD)));
        assertEquals(expectedMoves, actualMoves);
    }

    /*
        cannot land on promotion rank from double forward move
     */
    @Test
    void givenWhitePawnOnSixthRank_whenGettingPossibleMoves_thenOneLegalMovesByOneRank() {
        Pawn pawn = new Pawn(WHITE);
        Position start = pf.create(6, "E");
        board.placePiece(start, pawn);

        MoveGenerator moveGenerator = game.getMoveGenerator();
        Set<Move> actualMoves = moveGenerator.generate(game, start).collect(Collectors.toSet());

        Set<Move> expectedMoves = Set.of(new Move(pawn, start, pf.create(7, "E"), new Metadata().put(MOVE_TYPE, PAWN_FORWARD)));
        assertEquals(expectedMoves, actualMoves);
    }

    @Test
    void givenEmptyBoard_whenBlackPawnIsMakingMove_thenExceptionIsThrown() {
        Move move = new Move(new Pawn(BLACK), pf.create(1, "A"), pf.create(2, "A"));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> game.makeMove(move));
        assertEquals(e.getMessage(), "Now moving player WHITE not player BLACK");
    }

    @Test
    void givenEmptyBoard_whenWhitePawnIsMakingMove_thenExceptionIsThrown() {
        Move move = new Move(new Pawn(WHITE), pf.create(1, "A"), pf.create(2, "A"));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> game.makeMove(move));
        assertTrue(e.getMessage().contains("is not one of the legal moves []"));
    }

    @Test
    void givenTwoPlayers_whenPlayersAreMovingTheirPawnsOnTheSameFileUntilTheyFaceEachOther_thenThereAreNoMoreMoves() {
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(pf.create(1, "C"), whitePawn);
        Pawn blackPawn = new Pawn(BLACK);
        board.placePiece(pf.create(8, "C"), blackPawn);

        game.makeMove(new Move(whitePawn, pf.create(1, "C"), pf.create(3, "C"), new Metadata().put(MOVE_TYPE, PAWN_DOUBLE_FORWARD)));
        game.makeMove(new Move(blackPawn, pf.create(8, "C"), pf.create(6, "C"), new Metadata().put(MOVE_TYPE, PAWN_DOUBLE_FORWARD)));
        game.makeMove(new Move(whitePawn, pf.create(3, "C"), pf.create(4, "C"), new Metadata().put(MOVE_TYPE, PAWN_FORWARD)));
        game.makeMove(new Move(blackPawn, pf.create(6, "C"), pf.create(5, "C"), new Metadata().put(MOVE_TYPE, PAWN_FORWARD)));

        MoveGenerator moveGenerator = game.getMoveGenerator();
        assertEquals(Set.of(), moveGenerator.generate(game, pf.create(4, "C")).collect(Collectors.toSet()));
        assertEquals(Set.of(), moveGenerator.generate(game, pf.create(5, "C")).collect(Collectors.toSet()));
    }

    @Test
    void givenWhitePawnAndBlackPawnOnDiagonalRight_whenWhiteMoves_thenItCanCapture() {
        Pawn whitePawn = new Pawn(WHITE);
        Position whitePosition = pf.create(4, "C");
        board.placePiece(whitePosition, whitePawn);
        Pawn blackPawn = new Pawn(BLACK);
        Position blackPosition = pf.create(5, "D");
        board.placePiece(blackPosition, blackPawn);

        MoveGenerator moveGenerator = game.getMoveGenerator();
        Set<Move> whiteMoves = moveGenerator.generate(game, whitePosition).collect(Collectors.toSet());

        Metadata expectedMetadata = new Metadata()
                .put(MOVE_TYPE, PAWN_CAPTURE)
                .put(CAPTURED_PIECE, blackPawn)
                .put(CAPTURED_PIECE_POSITION, blackPosition);
        Move expectedMove = new Move(whitePawn, whitePosition, blackPosition, expectedMetadata);
        assertTrue(whiteMoves.contains(expectedMove));
    }

    @Test
    void givenBlackPawnAndWhitePawnOnDiagonalLeft_whenBlackMoves_thenItCanCapture() {
        Pawn whitePawn = new Pawn(WHITE);
        Position whitePosition = pf.create(2, "A");
        board.placePiece(whitePosition, whitePawn);
        Pawn blackPawn = new Pawn(BLACK);
        Position blackPosition = pf.create(3, "B");
        board.placePiece(blackPosition, blackPawn);

        MoveGenerator moveGenerator = game.getMoveGenerator();
        Set<Move> blackMoves = moveGenerator.generate(game, blackPosition).collect(Collectors.toSet());

        Metadata expectedMetadata = new Metadata()
                .put(MOVE_TYPE, PAWN_CAPTURE)
                .put(CAPTURED_PIECE, whitePawn)
                .put(CAPTURED_PIECE_POSITION, whitePosition);
        Move expectedMove = new Move(blackPawn, blackPosition, whitePosition, expectedMetadata);
        assertTrue(blackMoves.contains(expectedMove));
    }
}
