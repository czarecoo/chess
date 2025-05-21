package org.czareg;

import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.rook.RookMoveMoveGenerator;
import org.czareg.piece.Pawn;
import org.czareg.piece.Piece;
import org.czareg.piece.Rook;
import org.czareg.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RookMoveTests extends BaseTests {

    private PieceMoveGenerator pieceMoveGenerator;

    @BeforeEach
    void setUp() {
        pieceMoveGenerator = new RookMoveMoveGenerator();
    }

    @Test
    void givenWhiteRookAloneOnTheBoard_whenGeneratingMoves_thenManyMovesAreGenerated() {
        Piece piece = new Rook(WHITE);
        Position position = pf.create(4, "D");
        board.placePiece(position, piece);

        Set<Move> moves = pieceMoveGenerator
                .generate(context, piece, position)
                .collect(Collectors.toSet());

        assertEquals(14, moves.size());
        assertEquals(14, moves.stream().map(Move::getEnd).distinct().count());
        assertTrue(moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(Metadata.Key.MOVE_TYPE, MoveType.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .allMatch(moveType -> moveType == MoveType.MOVE));
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
                .generate(context, piece, position)
                .collect(Collectors.toSet());

        assertEquals(Set.of(), moves);
    }

    @Test
    void givenWhiteRookSurroundedByBlackPawns_whenGeneratingMoves_thenNoMovesAreGenerated() {
        Piece piece = new Rook(WHITE);
        Position Position = pf.create(4, "D");
        board.placePiece(Position, piece);
        board.placePiece(pf.create(3, "C"), new Pawn(BLACK));
        board.placePiece(pf.create(3, "D"), new Pawn(BLACK));
        board.placePiece(pf.create(3, "E"), new Pawn(BLACK));
        board.placePiece(pf.create(4, "C"), new Pawn(BLACK));
        board.placePiece(pf.create(4, "E"), new Pawn(BLACK));
        board.placePiece(pf.create(5, "C"), new Pawn(BLACK));
        board.placePiece(pf.create(5, "D"), new Pawn(BLACK));
        board.placePiece(pf.create(5, "E"), new Pawn(BLACK));

        Set<Move> moves = pieceMoveGenerator
                .generate(context, piece, Position)
                .collect(Collectors.toSet());

        assertEquals(Set.of(), moves);
    }

    @Test
    void givenWhiteRookHasManyPossibleMoves_whenGeneratingMoves_thenManyMovesAreGenerated() {
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
                .generate(context, piece, position)
                .collect(Collectors.toSet());

        assertEquals(10, moves.size());
        assertEquals(10, moves.stream().map(Move::getEnd).distinct().count());
        assertTrue(moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(Metadata.Key.MOVE_TYPE, MoveType.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .allMatch(moveType -> moveType == MoveType.MOVE));
    }
}
