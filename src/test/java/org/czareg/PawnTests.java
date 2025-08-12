package org.czareg;

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

import static org.czareg.game.Metadata.Key.CAPTURE_PIECE;
import static org.czareg.game.Metadata.Key.PROMOTION_PIECE_CLASS;
import static org.czareg.game.MoveType.*;
import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class PawnTests extends BaseTests {

    @Test
    void givenWhitePawnDidNotMoveYet_whenGettingPossibleMoves_thenCanMoveByOneOrTwoRows() {
        Pawn pawn = new Pawn(WHITE);
        Position start = pf.create(2, "A");
        board.placePiece(start, pawn);

        Set<Move> actualMoves = moveGenerator.generate(context, start).collect(Collectors.toSet());

        Set<Move> expectedMoves = Set.of(
                new Move(pawn, start, pf.create(3, "A"), new Metadata(MOVE)),
                new Move(pawn, start, pf.create(4, "A"), new Metadata(INITIAL_DOUBLE_FORWARD))
        );
        assertEquals(expectedMoves, actualMoves);
    }

    @Test
    void givenWhitePawnDidMoveBefore_whenGettingPossibleMoves_thenCanMoveByOneRow() {
        Pawn pawn = new Pawn(WHITE);
        Position whiteStart = pf.create(2, "D");
        board.placePiece(whiteStart, pawn);
        moveMaker.make(context, new Move(pawn, whiteStart, pf.create(3, "D"), new Metadata(MOVE)));

        Set<Move> actualMoves = moveGenerator.generate(context, pf.create(3, "D")).collect(Collectors.toSet());

        Set<Move> expectedMoves = Set.of(new Move(pawn, pf.create(3, "D"), pf.create(4, "D"), new Metadata(MOVE)));
        assertEquals(expectedMoves, actualMoves);
    }

    @Test
    void givenWhitePawnOnLastRank_whenGettingPossibleMoves_thenNoGeneratedMoves() {
        Pawn pawn = new Pawn(WHITE);
        Position start = pf.create(8, "H");
        board.placePiece(start, pawn);

        Set<Move> actualMoves = moveGenerator.generate(context, start).collect(Collectors.toSet());

        assertEquals(Set.of(), actualMoves);
    }

    @Test
    void givenWhitePawnOnSeventhRank_whenGettingPossibleMoves_thenManyPromotionMovesGenerated() {
        Pawn pawn = new Pawn(WHITE);
        Position start = pf.create(7, "B");
        board.placePiece(start, pawn);

        Set<Move> actualMoves = moveGenerator.generate(context, start).collect(Collectors.toSet());

        assertEquals(4, actualMoves.size());
        assertEquals(1, actualMoves.stream().map(Move::getEnd).distinct().count());
        assertTrue(actualMoves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(Metadata.Key.MOVE_TYPE, MoveType.class))
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
        Position start = pf.create(6, "E");
        board.placePiece(start, pawn);

        Set<Move> actualMoves = moveGenerator.generate(context, start).collect(Collectors.toSet());

        Set<Move> expectedMoves = Set.of(new Move(pawn, start, pf.create(7, "E"), new Metadata(MOVE)));
        assertEquals(expectedMoves, actualMoves);
    }

    @Test
    void givenEmptyBoard_whenBlackPawnIsMakingMove_thenExceptionIsThrown() {
        Move move = new Move(new Pawn(BLACK), pf.create(1, "A"), pf.create(2, "A"), new Metadata(MOVE));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> moveMaker.make(context, move));
        assertEquals("Now moving player WHITE not player BLACK", e.getMessage());
    }

    @Test
    void givenEmptyBoard_whenWhitePawnIsMakingMove_thenExceptionIsThrown() {
        Move move = new Move(new Pawn(WHITE), pf.create(1, "A"), pf.create(2, "A"), new Metadata(MOVE));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> moveMaker.make(context, move));
        assertTrue(e.getMessage().contains("No legal moves were generated"));
    }

    @Test
    void givenEmptyBoard_whenGeneratingMovesForPositionWithoutPiece_thenNoMovesAreReturned() {
        Position positionWithoutPiece = pf.create(1, "A");

        Set<Move> moves = moveGenerator.generate(context, positionWithoutPiece).collect(Collectors.toSet());

        assertTrue(moves.isEmpty());
    }

    @Test
    void givenWhiteAndBlackPawns_whenWhiteIsDoingDoubleForwardMoveToTheEndPositionOccupiedByBlackPawn_thenNoMovesAreReturned() {
        Position whitePosition = pf.create(2, "E");
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(whitePosition, whitePawn);
        Position blackPosition = pf.create(4, "E");
        board.placePiece(blackPosition, new Pawn(BLACK));
        PieceMoveGenerator pieceMoveGenerator = new PawnDoubleForwardMoveGenerator();

        Optional<Move> move = pieceMoveGenerator.generate(context, whitePawn, whitePosition, new IndexChange(2, 0));

        assertTrue(move.isEmpty());
    }

    @Test
    void givenWhiteAndBlackPawns_whenWhiteIsDoingDoubleForwardMoveAndBlackPawnIsOnTheWay_thenNoMovesAreReturned() {
        Position whiteStartPosition = pf.create(2, "E");
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(whiteStartPosition, whitePawn);
        Position blackPosition = pf.create(3, "E");
        board.placePiece(blackPosition, new Pawn(BLACK));
        PieceMoveGenerator pieceMoveGenerator = new PawnDoubleForwardMoveGenerator();

        Optional<Move> move = pieceMoveGenerator.generate(context, whitePawn, whiteStartPosition, new IndexChange(2, 0));

        assertTrue(move.isEmpty());
    }

    @Test
    void givenTwoWhitePawns_whenOnePawnIsTryingToCaptureAnother_thenNoMovesAreReturned() {
        Position firstWhitePosition = pf.create(3, "B");
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(firstWhitePosition, whitePawn);
        Position secondWhitePosition = pf.create(4, "A");
        board.placePiece(secondWhitePosition, new Pawn(WHITE));
        PieceMoveGenerator pieceMoveGenerator = new PawnCaptureMoveGenerator();

        Optional<Move> move = pieceMoveGenerator.generate(context, whitePawn, firstWhitePosition, new IndexChange(1, -1));

        assertTrue(move.isEmpty());
    }

    @Test
    void givenTwoPlayers_whenPlayersAreMovingTheirPawnsOnTheSameFileUntilTheyFaceEachOther_thenThereAreNoMoreMoves() {
        Pawn whitePawn = new Pawn(WHITE);
        board.placePiece(pf.create(1, "C"), whitePawn);
        Pawn blackPawn = new Pawn(BLACK);
        board.placePiece(pf.create(8, "C"), blackPawn);

        moveMaker.make(context, new Move(whitePawn, pf.create(1, "C"), pf.create(3, "C"), new Metadata(INITIAL_DOUBLE_FORWARD)));
        moveMaker.make(context, new Move(blackPawn, pf.create(8, "C"), pf.create(6, "C"), new Metadata(INITIAL_DOUBLE_FORWARD)));
        moveMaker.make(context, new Move(whitePawn, pf.create(3, "C"), pf.create(4, "C"), new Metadata(MOVE)));
        moveMaker.make(context, new Move(blackPawn, pf.create(6, "C"), pf.create(5, "C"), new Metadata(MOVE)));

        assertEquals(Set.of(), moveGenerator.generate(context, pf.create(4, "C")).collect(Collectors.toSet()));
        assertEquals(Set.of(), moveGenerator.generate(context, pf.create(5, "C")).collect(Collectors.toSet()));
    }

    @Test
    void givenWhitePawnAndBlackPawnOnDiagonalRight_whenWhiteMoves_thenItCanCapture() {
        Pawn whitePawn = new Pawn(WHITE);
        Position whitePosition = pf.create(4, "C");
        board.placePiece(whitePosition, whitePawn);
        Pawn blackPawn = new Pawn(BLACK);
        Position blackPosition = pf.create(5, "D");
        board.placePiece(blackPosition, blackPawn);

        Set<Move> whiteMoves = moveGenerator.generate(context, whitePosition).collect(Collectors.toSet());

        Metadata expectedMetadata = new Metadata(CAPTURE)
                .put(CAPTURE_PIECE, blackPawn);
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

        Set<Move> blackMoves = moveGenerator.generate(context, blackPosition).collect(Collectors.toSet());

        Metadata expectedMetadata = new Metadata(CAPTURE)
                .put(CAPTURE_PIECE, whitePawn);
        Move expectedMove = new Move(blackPawn, blackPosition, whitePosition, expectedMetadata);
        assertTrue(blackMoves.contains(expectedMove));
    }
}
