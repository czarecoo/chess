package org.czareg;

import org.czareg.board.Board;
import org.czareg.game.*;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.queen.QueenMoveMoveGenerator;
import org.czareg.piece.Pawn;
import org.czareg.piece.Queen;
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

class QueenMoveTests {

    private Context context;
    private Board board;
    private PositionFactory pf;
    private PieceMoveGenerator pieceMoveGenerator;

    @BeforeEach
    void setUp() {
        context = new ClassicContext();
        board = context.getBoard();
        pf = board.getPositionFactory();
        pieceMoveGenerator = new QueenMoveMoveGenerator();
    }

    @Test
    void givenWhiteQueenAloneOnTheBoard_whenGeneratingMoves_thenManyMovesAreGenerated() {
        Queen queen = new Queen(WHITE);
        Position queenPosition = pf.create(4, "D");
        board.placePiece(queenPosition, queen);

        Set<Move> moves = pieceMoveGenerator
                .generate(context, queen, queenPosition)
                .collect(Collectors.toSet());

        assertEquals(27, moves.size());
        assertEquals(27, moves.stream().map(Move::getEnd).distinct().count());
        assertTrue(moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(Metadata.Key.MOVE_TYPE, MoveType.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .allMatch(moveType -> moveType == MoveType.MOVE));
    }

    @Test
    void givenWhiteQueenSurroundedByWhitePawns_whenGeneratingMoves_thenNoMovesAreGenerated() {
        Queen queen = new Queen(WHITE);
        Position queenPosition = pf.create(4, "D");
        board.placePiece(queenPosition, queen);
        board.placePiece(pf.create(3, "C"), new Pawn(WHITE));
        board.placePiece(pf.create(3, "D"), new Pawn(WHITE));
        board.placePiece(pf.create(3, "E"), new Pawn(WHITE));
        board.placePiece(pf.create(4, "C"), new Pawn(WHITE));
        board.placePiece(pf.create(4, "E"), new Pawn(WHITE));
        board.placePiece(pf.create(5, "C"), new Pawn(WHITE));
        board.placePiece(pf.create(5, "D"), new Pawn(WHITE));
        board.placePiece(pf.create(5, "E"), new Pawn(WHITE));

        Set<Move> moves = pieceMoveGenerator
                .generate(context, queen, queenPosition)
                .collect(Collectors.toSet());

        assertEquals(Set.of(), moves);
    }

    @Test
    void givenWhiteQueenSurroundedByBlackPawns_whenGeneratingMoves_thenNoMovesAreGenerated() {
        Queen queen = new Queen(WHITE);
        Position queenPosition = pf.create(4, "D");
        board.placePiece(queenPosition, queen);
        board.placePiece(pf.create(3, "C"), new Pawn(BLACK));
        board.placePiece(pf.create(3, "D"), new Pawn(BLACK));
        board.placePiece(pf.create(3, "E"), new Pawn(BLACK));
        board.placePiece(pf.create(4, "C"), new Pawn(BLACK));
        board.placePiece(pf.create(4, "E"), new Pawn(BLACK));
        board.placePiece(pf.create(5, "C"), new Pawn(BLACK));
        board.placePiece(pf.create(5, "D"), new Pawn(BLACK));
        board.placePiece(pf.create(5, "E"), new Pawn(BLACK));

        Set<Move> moves = pieceMoveGenerator
                .generate(context, queen, queenPosition)
                .collect(Collectors.toSet());

        assertEquals(Set.of(), moves);
    }

    @Test
    void givenWhiteQueenHasManyPossibleMoves_whenGeneratingMoves_thenManyMovesAreGenerated() {
        Queen queen = new Queen(WHITE);
        Position queenPosition = pf.create(4, "D");
        board.placePiece(queenPosition, queen);
        board.placePiece(pf.create(1, "D"), new Pawn(BLACK));
        board.placePiece(pf.create(8, "D"), new Pawn(BLACK));
        board.placePiece(pf.create(4, "A"), new Pawn(BLACK));
        board.placePiece(pf.create(4, "H"), new Pawn(BLACK));
        board.placePiece(pf.create(7, "A"), new Pawn(BLACK));
        board.placePiece(pf.create(1, "G"), new Pawn(BLACK));
        board.placePiece(pf.create(1, "A"), new Pawn(BLACK));
        board.placePiece(pf.create(8, "H"), new Pawn(BLACK));

        Set<Move> moves = pieceMoveGenerator
                .generate(context, queen, queenPosition)
                .collect(Collectors.toSet());

        assertEquals(19, moves.size());
        assertEquals(19, moves.stream().map(Move::getEnd).distinct().count());
        assertTrue(moves.stream().map(Move::getMetadata)
                .map(metadata -> metadata.get(Metadata.Key.MOVE_TYPE, MoveType.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .allMatch(moveType -> moveType == MoveType.MOVE));
    }
}
