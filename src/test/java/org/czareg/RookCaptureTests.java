package org.czareg;

import org.czareg.board.Board;
import org.czareg.game.*;
import org.czareg.piece.Pawn;
import org.czareg.piece.Piece;
import org.czareg.piece.Rook;
import org.czareg.piece.move.PieceMoveGenerator;
import org.czareg.piece.move.rook.RookCaptureMoveGenerator;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RookCaptureTests {

    private Game game;
    private Board board;
    private PositionFactory pf;
    private PieceMoveGenerator pieceMoveGenerator;

    @BeforeEach
    void setUp() {
        game = new ClassicGame();
        board = game.getBoard();
        pf = board.getPositionFactory();
        pieceMoveGenerator = new RookCaptureMoveGenerator();
    }

    @Test
    void givenWhiteRookAloneOnTheBoard_whenGeneratingMoves_thenNoMovesAreGenerated() {
        Piece piece = new Rook(WHITE);
        Position position = pf.create(4, "D");
        board.placePiece(position, piece);

        Set<Move> moves = pieceMoveGenerator
                .generate(game, piece, position)
                .collect(Collectors.toSet());

        assertEquals(Set.of(), moves);
    }

    @Test
    void givenWhiteRookSurroundedByWhitePawns_whenGeneratingMoves_thenNoMovesAreGenerated() {
        Piece piece = new Rook(WHITE);
        Position position = pf.create(4, "D");
        board.placePiece(position, piece);
        board.placePiece(pf.create(3, "C"), new Pawn(WHITE));
        board.placePiece(pf.create(3, "D"), new Pawn(WHITE));
        board.placePiece(pf.create(3, "E"), new Pawn(WHITE));
        board.placePiece(pf.create(4, "C"), new Pawn(WHITE));
        board.placePiece(pf.create(4, "E"), new Pawn(WHITE));
        board.placePiece(pf.create(5, "C"), new Pawn(WHITE));
        board.placePiece(pf.create(5, "D"), new Pawn(WHITE));
        board.placePiece(pf.create(5, "E"), new Pawn(WHITE));

        Set<Move> moves = pieceMoveGenerator
                .generate(game, piece, position)
                .collect(Collectors.toSet());

        assertEquals(Set.of(), moves);
    }

    @Test
    void givenWhiteRookSurroundedByBlackPawns_whenGeneratingMoves_thenThereAreFourCaptureMoves() {
        Piece piece = new Rook(WHITE);
        Position position = pf.create(4, "D");
        board.placePiece(position, piece);
        board.placePiece(pf.create(3, "C"), new Pawn(BLACK));
        board.placePiece(pf.create(3, "D"), new Pawn(BLACK));
        board.placePiece(pf.create(3, "E"), new Pawn(BLACK));
        board.placePiece(pf.create(4, "C"), new Pawn(BLACK));
        board.placePiece(pf.create(4, "E"), new Pawn(BLACK));
        board.placePiece(pf.create(5, "C"), new Pawn(BLACK));
        board.placePiece(pf.create(5, "D"), new Pawn(BLACK));
        board.placePiece(pf.create(5, "E"), new Pawn(BLACK));

        Set<Move> moves = pieceMoveGenerator
                .generate(game, piece, position)
                .collect(Collectors.toSet());

        assertEquals(4, moves.size());
        assertEquals(Set.of(
                pf.create(3, "D"),
                pf.create(5, "D"),
                pf.create(4, "C"),
                pf.create(4, "E")
        ), moves.stream().map(Move::getEnd).collect(Collectors.toSet()));
        assertEquals(4, moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(Metadata.Key.CAPTURE_PIECE, Piece.class))
                .distinct()
                .count());
        assertTrue(moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(Metadata.Key.MOVE_TYPE, MoveType.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .allMatch(moveType -> moveType == MoveType.ROOK_CAPTURE));
    }

    @Test
    void givenWhiteRookHasFourPossibleCaptures_whenGeneratingMoves_thenThereAreFourCaptureMoves() {
        Piece piece = new Rook(WHITE);
        Position position = pf.create(4, "D");
        board.placePiece(position, piece);
        board.placePiece(pf.create(1, "D"), new Pawn(BLACK));
        board.placePiece(pf.create(8, "D"), new Pawn(BLACK));
        board.placePiece(pf.create(4, "A"), new Pawn(BLACK));
        board.placePiece(pf.create(4, "H"), new Pawn(BLACK));
        board.placePiece(pf.create(7, "A"), new Pawn(BLACK));
        board.placePiece(pf.create(1, "G"), new Pawn(BLACK));
        board.placePiece(pf.create(1, "A"), new Pawn(BLACK));
        board.placePiece(pf.create(8, "H"), new Pawn(BLACK));

        Set<Move> moves = pieceMoveGenerator
                .generate(game, piece, position)
                .collect(Collectors.toSet());

        assertEquals(4, moves.size());
        assertEquals(Set.of(
                pf.create(1, "D"),
                pf.create(8, "D"),
                pf.create(4, "A"),
                pf.create(4, "H")
        ), moves.stream().map(Move::getEnd).collect(Collectors.toSet()));
        assertEquals(4, moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(Metadata.Key.CAPTURE_PIECE, Piece.class))
                .distinct()
                .count());
        assertTrue(moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(Metadata.Key.MOVE_TYPE, MoveType.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .allMatch(moveType -> moveType == MoveType.ROOK_CAPTURE));
    }
}
