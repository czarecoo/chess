package org.czareg;

import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.bishop.BishopCaptureMoveGenerator;
import org.czareg.piece.Bishop;
import org.czareg.piece.Pawn;
import org.czareg.piece.Piece;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.czareg.game.Metadata.Key.CAPTURE_PIECE;
import static org.czareg.game.Metadata.Key.MOVE_TYPE;
import static org.czareg.game.MoveType.CAPTURE;
import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BishopCaptureTests extends BaseTests {

    private PieceMoveGenerator pieceMoveGenerator;

    @BeforeEach
    void setUp() {
        pieceMoveGenerator = new BishopCaptureMoveGenerator();
    }

    @Test
    void givenWhiteBishopAloneOnTheBoard_whenGeneratingMoves_thenNoMovesAreGenerated() {
        Piece piece = new Bishop(WHITE);
        Position position = pf.create("D", 4);
        board.placePiece(position, piece);

        Set<Move> moves = pieceMoveGenerator
                .generate(context, piece, position)
                .collect(Collectors.toSet());

        assertEquals(Set.of(), moves);
    }

    @Test
    void givenWhiteBishopSurroundedByWhitePawns_whenGeneratingMoves_thenNoMovesAreGenerated() {
        Piece piece = new Bishop(WHITE);
        Position position = pf.create("D", 4);
        board.placePiece(position, piece);
        board.placePiece(pf.create("C", 3), new Pawn(WHITE));
        board.placePiece(pf.create("D", 3), new Pawn(WHITE));
        board.placePiece(pf.create("E", 3), new Pawn(WHITE));
        board.placePiece(pf.create("C", 4), new Pawn(WHITE));
        board.placePiece(pf.create("E", 4), new Pawn(WHITE));
        board.placePiece(pf.create("C", 5), new Pawn(WHITE));
        board.placePiece(pf.create("D", 5), new Pawn(WHITE));
        board.placePiece(pf.create("E", 5), new Pawn(WHITE));

        Set<Move> moves = pieceMoveGenerator
                .generate(context, piece, position)
                .collect(Collectors.toSet());

        assertEquals(Set.of(), moves);
    }

    @Test
    void givenWhiteBishopSurroundedByBlackPawns_whenGeneratingMoves_thenThereAreFourCaptureMoves() {
        Piece piece = new Bishop(WHITE);
        Position position = pf.create("D", 4);
        board.placePiece(position, piece);
        board.placePiece(pf.create("C", 3), new Pawn(BLACK));
        board.placePiece(pf.create("D", 3), new Pawn(BLACK));
        board.placePiece(pf.create("E", 3), new Pawn(BLACK));
        board.placePiece(pf.create("C", 4), new Pawn(BLACK));
        board.placePiece(pf.create("E", 4), new Pawn(BLACK));
        board.placePiece(pf.create("C", 5), new Pawn(BLACK));
        board.placePiece(pf.create("D", 5), new Pawn(BLACK));
        board.placePiece(pf.create("E", 5), new Pawn(BLACK));

        Set<Move> moves = pieceMoveGenerator
                .generate(context, piece, position)
                .collect(Collectors.toSet());

        assertEquals(4, moves.size());
        assertEquals(Set.of(
                pf.create("C", 5),
                pf.create("E", 5),
                pf.create("C", 3),
                pf.create("E", 3)
        ), moves.stream().map(Move::getEnd).collect(Collectors.toSet()));
        assertEquals(4, moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(CAPTURE_PIECE, Piece.class))
                .distinct()
                .count());
        assertTrue(moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(MOVE_TYPE, MoveType.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .allMatch(moveType -> moveType == CAPTURE));
    }

    @Test
    void givenWhiteBishopHasFourPossibleCaptures_whenGeneratingMoves_thenThereAreFourCaptureMoves() {
        Piece piece = new Bishop(WHITE);
        Position position = pf.create("D", 4);
        board.placePiece(position, piece);
        board.placePiece(pf.create("D", 1), new Pawn(BLACK));
        board.placePiece(pf.create("D", 8), new Pawn(BLACK));
        board.placePiece(pf.create("A", 4), new Pawn(BLACK));
        board.placePiece(pf.create("H", 4), new Pawn(BLACK));
        board.placePiece(pf.create("A", 7), new Pawn(BLACK));
        board.placePiece(pf.create("G", 1), new Pawn(BLACK));
        board.placePiece(pf.create("A", 1), new Pawn(BLACK));
        board.placePiece(pf.create("H", 8), new Pawn(BLACK));

        Set<Move> moves = pieceMoveGenerator
                .generate(context, piece, position)
                .collect(Collectors.toSet());

        assertEquals(4, moves.size());
        assertEquals(Set.of(
                pf.create("A", 1),
                pf.create("G", 1),
                pf.create("A", 7),
                pf.create("H", 8)
        ), moves.stream().map(Move::getEnd).collect(Collectors.toSet()));
        assertEquals(4, moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(CAPTURE_PIECE, Piece.class))
                .distinct()
                .count());
        assertTrue(moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(MOVE_TYPE, MoveType.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .allMatch(moveType -> moveType == CAPTURE));
    }

    @Test
    void givenWhiteBishopAndBlackPawn_whenWhiteBishopCapturesBlackPawn_thenBlackPawnIsNotOnTheBoard() {
        Piece piece = new Bishop(WHITE);
        Position position = pf.create("A", 6);
        board.placePiece(position, piece);
        board.placePiece(pf.create("B", 5), new Pawn(BLACK));
        assertEquals(1, board.getAllPiecePositions(WHITE).size());
        assertEquals(1, board.getAllPiecePositions(BLACK).size());

        Optional<Move> bishopCaptureDownAndRight = pieceMoveGenerator.generate(context, piece, position, new IndexChange(1, -1));
        assertTrue(bishopCaptureDownAndRight.isPresent());
        moveMaker.make(context, bishopCaptureDownAndRight.get());

        assertEquals(1, board.getAllPiecePositions(WHITE).size());
        assertEquals(0, board.getAllPiecePositions(BLACK).size());
    }
}
