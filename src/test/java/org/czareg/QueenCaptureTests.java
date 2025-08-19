package org.czareg;

import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.queen.QueenCaptureMoveGenerator;
import org.czareg.piece.Pawn;
import org.czareg.piece.Piece;
import org.czareg.piece.Queen;
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

class QueenCaptureTests extends BaseTests {

    private PieceMoveGenerator pieceMoveGenerator;

    @BeforeEach
    void setUp() {
        pieceMoveGenerator = new QueenCaptureMoveGenerator();
    }

    @Test
    void givenWhiteQueenAloneOnTheBoard_whenGeneratingMoves_thenNoMovesAreGenerated() {
        Queen queen = new Queen(WHITE);
        Position queenPosition = pf.create("D", 4);
        board.placePiece(queenPosition, queen);

        Set<Move> moves = pieceMoveGenerator
                .generate(context, queen, queenPosition)
                .collect(Collectors.toSet());

        assertEquals(Set.of(), moves);
    }

    @Test
    void givenWhiteQueenSurroundedByWhitePawns_whenGeneratingMoves_thenNoMovesAreGenerated() {
        Queen queen = new Queen(WHITE);
        Position queenPosition = pf.create("D", 4);
        board.placePiece(queenPosition, queen);
        board.placePiece(pf.create("C", 3), new Pawn(WHITE));
        board.placePiece(pf.create("D", 3), new Pawn(WHITE));
        board.placePiece(pf.create("E", 3), new Pawn(WHITE));
        board.placePiece(pf.create("C", 4), new Pawn(WHITE));
        board.placePiece(pf.create("E", 4), new Pawn(WHITE));
        board.placePiece(pf.create("C", 5), new Pawn(WHITE));
        board.placePiece(pf.create("D", 5), new Pawn(WHITE));
        board.placePiece(pf.create("E", 5), new Pawn(WHITE));

        Set<Move> moves = pieceMoveGenerator
                .generate(context, queen, queenPosition)
                .collect(Collectors.toSet());

        assertEquals(Set.of(), moves);
    }

    @Test
    void givenWhiteQueenSurroundedByBlackPawns_whenGeneratingMoves_thenThereAreEightCaptureMoves() {
        Queen queen = new Queen(WHITE);
        Position queenPosition = pf.create("D", 4);
        board.placePiece(queenPosition, queen);
        board.placePiece(pf.create("C", 3), new Pawn(BLACK));
        board.placePiece(pf.create("D", 3), new Pawn(BLACK));
        board.placePiece(pf.create("E", 3), new Pawn(BLACK));
        board.placePiece(pf.create("C", 4), new Pawn(BLACK));
        board.placePiece(pf.create("E", 4), new Pawn(BLACK));
        board.placePiece(pf.create("C", 5), new Pawn(BLACK));
        board.placePiece(pf.create("D", 5), new Pawn(BLACK));
        board.placePiece(pf.create("E", 5), new Pawn(BLACK));

        Set<Move> moves = pieceMoveGenerator
                .generate(context, queen, queenPosition)
                .collect(Collectors.toSet());

        assertEquals(8, moves.size());
        assertEquals(8, moves.stream().map(Move::getEnd).distinct().count());
        assertEquals(8, moves.stream().map(Move::getMetadata)
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
    void givenWhiteQueenHasEightPossibleCaptures_whenGeneratingMoves_thenThereAreEightCaptureMoves() {
        Queen queen = new Queen(WHITE);
        Position queenPosition = pf.create("D", 4);
        board.placePiece(queenPosition, queen);
        board.placePiece(pf.create("D", 1), new Pawn(BLACK));
        board.placePiece(pf.create("D", 8), new Pawn(BLACK));
        board.placePiece(pf.create("A", 4), new Pawn(BLACK));
        board.placePiece(pf.create("H", 4), new Pawn(BLACK));
        board.placePiece(pf.create("A", 7), new Pawn(BLACK));
        board.placePiece(pf.create("G", 1), new Pawn(BLACK));
        board.placePiece(pf.create("A", 1), new Pawn(BLACK));
        board.placePiece(pf.create("H", 8), new Pawn(BLACK));

        Set<Move> moves = pieceMoveGenerator
                .generate(context, queen, queenPosition)
                .collect(Collectors.toSet());

        assertEquals(8, moves.size());
        assertEquals(8, moves.stream().map(Move::getEnd).distinct().count());
        assertEquals(8, moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(CAPTURE_PIECE, Piece.class))
                .distinct()
                .count());
        assertTrue(moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(MOVE_TYPE, MoveType.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .allMatch(moveType -> moveType == CAPTURE));
    }
}
