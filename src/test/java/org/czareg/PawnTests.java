package org.czareg;

import org.czareg.board.ClassicPieceStartingPositionPlacer;
import org.czareg.board.PiecePlacer;
import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.pawn.PawnCaptureMoveGenerator;
import org.czareg.move.piece.pawn.PawnDoubleForwardMoveGenerator;
import org.czareg.piece.*;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.czareg.game.Metadata.Key.*;
import static org.czareg.game.MoveType.*;
import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class PawnTests extends BaseTests {

    @Test
    void givenWhitePawnDidNotMoveYet_whenGettingPossibleMoves_thenCanMoveByOneOrTwoRows() {
        Pawn pawn = new Pawn(WHITE);
        Position start = pf.create("A", 2);
        board.placePiece(start, pawn);

        Set<Move> actualMoves = moveGenerators.generateLegal(context).getMovesStarting(start);

        Set<Move> expectedMoves = Set.of(
                new Move(pawn, start, pf.create("A", 3), new Metadata(MOVE)),
                new Move(pawn, start, pf.create("A", 4), new Metadata(INITIAL_DOUBLE_FORWARD))
        );
        assertEquals(expectedMoves, actualMoves);
    }

    @Test
    void givenWhitePawnDidMoveBefore_whenGettingPossibleMoves_thenCanMoveByOneRow() {
        Pawn pawn = new Pawn(WHITE);
        Position d2 = pf.create("D", 2);
        board.placePiece(d2, pawn);
        Position d3 = pf.create("D", 3);
        moveMaker.make(context, new Move(pawn, d2, d3, new Metadata(MOVE)));

        Set<Move> actualMoves = moveGenerators.generateLegal(context).getMovesStarting(d3);

        Set<Move> expectedMoves = Set.of(new Move(pawn, d3, pf.create("D", 4), new Metadata(MOVE)));
        assertEquals(expectedMoves, actualMoves);
    }

    @Test
    void givenWhitePawnOnLastRank_whenGettingPossibleMoves_thenNoGeneratedMoves() {
        Pawn pawn = new Pawn(WHITE);
        Position start = pf.create("H", 8);
        board.placePiece(start, pawn);

        Set<Move> actualMoves = moveGenerators.generateLegal(context).getMovesStarting(start);

        assertEquals(Set.of(), actualMoves);
    }

    @Test
    void givenWhitePawnOnSeventhRank_whenGettingPossibleMoves_thenManyPromotionMovesGenerated() {
        Pawn pawn = new Pawn(WHITE);
        Position start = pf.create("B", 7);
        board.placePiece(start, pawn);

        Set<Move> actualMoves = moveGenerators.generateLegal(context).getMovesStarting(start);

        assertEquals(4, actualMoves.size());
        assertEquals(1, actualMoves.stream().map(Move::getEnd).distinct().count());
        assertTrue(actualMoves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(MOVE_TYPE, MoveType.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .allMatch(moveType -> moveType == PROMOTION));
        assertEquals(
                Set.of(Knight.class, Bishop.class, Rook.class, Queen.class),
                actualMoves.stream()
                        .map(Move::getMetadata)
                        .map(metadata -> metadata.getClass(PROMOTION_PIECE_CLASS, Piece.class))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet())
        );
    }

    /*
        cannot land on promotion rank from double forward move
     */
    @Test
    void givenWhitePawnOnSixthRank_whenGettingPossibleMoves_thenOneMoveGenerated() {
        Pawn pawn = new Pawn(WHITE);
        Position start = pf.create("E", 6);
        board.placePiece(start, pawn);

        Set<Move> actualMoves = moveGenerators.generateLegal(context).getMovesStarting(start);

        Set<Move> expectedMoves = Set.of(new Move(pawn, start, pf.create("E", 7), new Metadata(MOVE)));
        assertEquals(expectedMoves, actualMoves);
    }

    @Test
    void givenClassicStartingPosition_whenBlackPawnIsMakingMove_thenExceptionIsThrown() {
        PiecePlacer piecePlacer = new ClassicPieceStartingPositionPlacer();
        piecePlacer.place(board);
        Move move = new Move(new Pawn(BLACK), pf.create("A", 1), pf.create("A", 2), new Metadata(MOVE));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> moveMaker.make(context, move));
        assertEquals("Now moving player WHITE not player BLACK", e.getMessage());
    }

    @Test
    void givenClassicStartingPosition_whenWhitePawnIsMakingMove_thenExceptionIsThrown() {
        PiecePlacer piecePlacer = new ClassicPieceStartingPositionPlacer();
        piecePlacer.place(board);
        Move move = new Move(new Pawn(WHITE), pf.create("A", 1), pf.create("A", 2), new Metadata(MOVE));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> moveMaker.make(context, move));
        assertEquals("Move is not legal Move(piece=Pawn(player=WHITE), start=Position(file=A, rank=1), end=Position(file=A, rank=2), metadata=Metadata(data={MOVE_TYPE=MOVE}))", e.getMessage());
    }

    @Test
    void givenEmptyBoard_whenGeneratingMovesForPositionWithoutPiece_thenNoMovesAreReturned() {
        Position positionWithoutPiece = pf.create("A", 1);

        Set<Move> moves = moveGenerators.generateLegal(context).getMovesStarting(positionWithoutPiece);

        assertTrue(moves.isEmpty());
    }

    @Test
    void givenWhiteAndBlackPawns_whenWhiteIsDoingDoubleForwardMoveToTheEndPositionOccupiedByBlackPawn_thenNoMovesAreReturned() {
        Position whitePosition = pf.create("E", 2);
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(whitePosition, whitePawn);
        Position blackPosition = pf.create("E", 4);
        board.placePiece(blackPosition, new Pawn(BLACK));
        PieceMoveGenerator pieceMoveGenerator = new PawnDoubleForwardMoveGenerator();

        Optional<Move> move = pieceMoveGenerator.generate(context, whitePawn, whitePosition, new IndexChange(0, 2));

        assertTrue(move.isEmpty());
    }

    @Test
    void givenWhiteAndBlackPawns_whenWhiteIsDoingDoubleForwardMoveAndBlackPawnIsOnTheWay_thenNoMovesAreReturned() {
        Position whiteStartPosition = pf.create("E", 2);
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(whiteStartPosition, whitePawn);
        Position blackPosition = pf.create("E", 3);
        board.placePiece(blackPosition, new Pawn(BLACK));
        PieceMoveGenerator pieceMoveGenerator = new PawnDoubleForwardMoveGenerator();

        Optional<Move> move = pieceMoveGenerator.generate(context, whitePawn, whiteStartPosition, new IndexChange(0, 2));

        assertTrue(move.isEmpty());
    }

    @Test
    void givenTwoWhitePawns_whenOnePawnIsTryingToCaptureAnother_thenNoMovesAreReturned() {
        Position firstWhitePosition = pf.create("B", 3);
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(firstWhitePosition, whitePawn);
        Position secondWhitePosition = pf.create("A", 4);
        board.placePiece(secondWhitePosition, new Pawn(WHITE));
        PieceMoveGenerator pieceMoveGenerator = new PawnCaptureMoveGenerator();

        Optional<Move> move = pieceMoveGenerator.generate(context, whitePawn, firstWhitePosition, new IndexChange(-1, 1));

        assertTrue(move.isEmpty());
    }

    @Test
    void givenTwoPlayers_whenPlayersAreMovingTheirPawnsOnTheSameFileUntilTheyFaceEachOther_thenThereAreNoMoreMoves() {
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(pf.create("C", 1), whitePawn);
        Pawn blackPawn = new Pawn(BLACK);
        board.placePiece(pf.create("C", 8), blackPawn);

        moveMaker.make(context, new Move(whitePawn, pf.create("C", 1), pf.create("C", 3), new Metadata(INITIAL_DOUBLE_FORWARD)));
        moveMaker.make(context, new Move(blackPawn, pf.create("C", 8), pf.create("C", 6), new Metadata(INITIAL_DOUBLE_FORWARD)));
        Position c4 = pf.create("C", 4);
        moveMaker.make(context, new Move(whitePawn, pf.create("C", 3), c4, new Metadata(MOVE)));
        Position c5 = pf.create("C", 5);
        moveMaker.make(context, new Move(blackPawn, pf.create("C", 6), c5, new Metadata(MOVE)));

        Set<Move> whiteMoves = moveGenerators.generateLegal(context).getMovesStarting(c4);
        assertEquals(Set.of(), whiteMoves);
        Set<Move> blackMoves = moveGenerators.generateLegal(context).getMovesStarting(c5);
        assertEquals(Set.of(), blackMoves);
    }

    @Test
    void givenWhitePawnAndBlackPawnOnDiagonalRight_whenWhiteMoves_thenItCanCapture() {
        Pawn whitePawn = new Pawn(WHITE);
        Position whitePosition = pf.create("C", 4);
        board.placePiece(whitePosition, whitePawn);
        Pawn blackPawn = new Pawn(BLACK);
        Position blackPosition = pf.create("D", 5);
        board.placePiece(blackPosition, blackPawn);

        Set<Move> whiteMoves = moveGenerators.generateLegal(context).getMovesStarting(whitePosition);

        Metadata expectedMetadata = new Metadata(CAPTURE)
                .put(CAPTURE_PIECE, blackPawn);
        Move expectedMove = new Move(whitePawn, whitePosition, blackPosition, expectedMetadata);
        assertTrue(whiteMoves.contains(expectedMove));
    }

    @Test
    void givenBlackPawnAndWhitePawnOnDiagonalLeft_whenBlackMoves_thenItCanCapture() {
        Pawn whitePawn = new Pawn(WHITE);
        Position whitePosition = pf.create("A", 2);
        board.placePiece(whitePosition, whitePawn);
        Pawn blackPawn = new Pawn(BLACK);
        Position blackPosition = pf.create("B", 3);
        board.placePiece(blackPosition, blackPawn);

        Set<Move> blackMoves = moveGenerators.generateLegal(context).getMovesStarting(blackPosition);

        Metadata expectedMetadata = new Metadata(CAPTURE)
                .put(CAPTURE_PIECE, whitePawn);
        Move expectedMove = new Move(blackPawn, blackPosition, whitePosition, expectedMetadata);
        assertTrue(blackMoves.contains(expectedMove));
    }
}
