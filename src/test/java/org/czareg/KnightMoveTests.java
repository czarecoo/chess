package org.czareg;

import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.knight.KnightMoveMoveGenerator;
import org.czareg.piece.Knight;
import org.czareg.piece.Pawn;
import org.czareg.piece.Piece;
import org.czareg.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.czareg.game.Metadata.Key.MOVE_TYPE;
import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KnightMoveTests extends BaseTests {

    private PieceMoveGenerator pieceMoveGenerator;

    @BeforeEach
    void setUp() {
        pieceMoveGenerator = new KnightMoveMoveGenerator();
    }

    @Test
    void givenWhiteKnightAloneOnTheBoard_whenGeneratingMoves_thenManyMovesAreGenerated() {
        Piece piece = new Knight(WHITE);
        Position position = pf.create("D", 4);
        board.placePiece(position, piece);

        Set<Move> moves = pieceMoveGenerator
                .generate(context, piece, position)
                .collect(Collectors.toSet());

        assertEquals(8, moves.size());
        assertEquals(8, moves.stream().map(Move::getEnd).distinct().count());
        assertTrue(moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(MOVE_TYPE, MoveType.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .allMatch(moveType -> moveType == MoveType.MOVE));
    }

    @Test
    void givenWhiteKnightSurroundedByWhitePawns_whenGeneratingMoves_thenManyMovesAreGenerated() {
        Piece piece = new Knight(WHITE);
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

        assertEquals(8, moves.size());
        assertEquals(8, moves.stream().map(Move::getEnd).distinct().count());
        assertTrue(moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(MOVE_TYPE, MoveType.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .allMatch(moveType -> moveType == MoveType.MOVE));
    }

    @Test
    void givenWhiteKnightSurroundedByBlackPawns_whenGeneratingMoves_thenManyMovesAreGenerated() {
        Piece piece = new Knight(WHITE);
        Position Position = pf.create("D", 4);
        board.placePiece(Position, piece);
        board.placePiece(pf.create("C", 3), new Pawn(BLACK));
        board.placePiece(pf.create("D", 3), new Pawn(BLACK));
        board.placePiece(pf.create("E", 3), new Pawn(BLACK));
        board.placePiece(pf.create("C", 4), new Pawn(BLACK));
        board.placePiece(pf.create("E", 4), new Pawn(BLACK));
        board.placePiece(pf.create("C", 5), new Pawn(BLACK));
        board.placePiece(pf.create("D", 5), new Pawn(BLACK));
        board.placePiece(pf.create("E", 5), new Pawn(BLACK));

        Set<Move> moves = pieceMoveGenerator
                .generate(context, piece, Position)
                .collect(Collectors.toSet());

        assertEquals(8, moves.size());
        assertEquals(8, moves.stream().map(Move::getEnd).distinct().count());
        assertTrue(moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(MOVE_TYPE, MoveType.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .allMatch(moveType -> moveType == MoveType.MOVE));
    }

    @Test
    void givenWhiteKnightAndManyPawnsAtTheEdgeOfTheBoard_whenGeneratingMoves_thenManyMovesAreGenerated() {
        Piece piece = new Knight(WHITE);
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

        assertEquals(8, moves.size());
        assertEquals(8, moves.stream().map(Move::getEnd).distinct().count());
        assertTrue(moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(MOVE_TYPE, MoveType.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .allMatch(moveType -> moveType == MoveType.MOVE));
    }

    @Test
    void givenWhiteKnightBlockedByWhitePieces_whenGeneratingMoves_thenNoMovesAreGenerated() {
        Piece piece = new Knight(WHITE);
        Position position = pf.create("D", 4);
        board.placePiece(position, piece);
        board.placePiece(pf.create("E", 6), new Pawn(WHITE));
        board.placePiece(pf.create("B", 5), new Pawn(WHITE));
        board.placePiece(pf.create("C", 6), new Pawn(WHITE));
        board.placePiece(pf.create("F", 5), new Pawn(WHITE));
        board.placePiece(pf.create("B", 3), new Pawn(WHITE));
        board.placePiece(pf.create("F", 3), new Pawn(WHITE));
        board.placePiece(pf.create("C", 2), new Pawn(WHITE));
        board.placePiece(pf.create("E", 2), new Pawn(WHITE));

        Set<Move> moves = pieceMoveGenerator
                .generate(context, piece, position)
                .collect(Collectors.toSet());

        assertEquals(Set.of(), moves);
    }
}
