package org.czareg;

import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.king.KingCastlingMoveGenerator;
import org.czareg.piece.King;
import org.czareg.piece.Pawn;
import org.czareg.piece.Piece;
import org.czareg.piece.Rook;
import org.czareg.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.czareg.game.MoveType.MOVE;
import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class KingCastlingTests extends BaseTests {

    private PieceMoveGenerator pieceMoveGenerator;

    @BeforeEach
    void setUp() {
        pieceMoveGenerator = new KingCastlingMoveGenerator();
    }

    @Test
    void testWhiteLongCastle() {
        Piece king = new King(WHITE);
        Position kingPosition = pf.create("E", 1);
        board.placePiece(kingPosition, king);
        Piece rook = new Rook(WHITE);
        Position rookPosition = pf.create("A", 1);
        board.placePiece(rookPosition, rook);
        List<Move> moves = pieceMoveGenerator
                .generate(context, king, kingPosition)
                .toList();
        assertEquals(1, moves.size());

        moveMaker.make(context, moves.getFirst());

        Position expectedRookPosition = pf.create("D", 1);
        assertTrue(board.hasPiece(expectedRookPosition));
        Piece actualRook = board.getPiece(expectedRookPosition);
        assertInstanceOf(Rook.class, actualRook);
        assertEquals(WHITE, actualRook.getPlayer());
        Position expectedKingPosition = pf.create("C", 1);
        assertTrue(board.hasPiece(expectedKingPosition));
        Piece actualKing = board.getPiece(expectedKingPosition);
        assertInstanceOf(King.class, actualKing);
        assertEquals(WHITE, actualKing.getPlayer());
    }

    @Test
    void testWhiteShortCastle() {
        Piece king = new King(WHITE);
        Position kingPosition = pf.create("E", 1);
        board.placePiece(kingPosition, king);
        Piece rook = new Rook(WHITE);
        Position rookPosition = pf.create("H", 1);
        board.placePiece(rookPosition, rook);
        List<Move> moves = pieceMoveGenerator
                .generate(context, king, kingPosition)
                .toList();
        assertEquals(1, moves.size());

        moveMaker.make(context, moves.getFirst());

        Position expectedRookPosition = pf.create("F", 1);
        assertTrue(board.hasPiece(expectedRookPosition));
        Piece actualRook = board.getPiece(expectedRookPosition);
        assertInstanceOf(Rook.class, actualRook);
        assertEquals(WHITE, actualRook.getPlayer());
        Position expectedKingPosition = pf.create("G", 1);
        assertTrue(board.hasPiece(expectedKingPosition));
        Piece actualKing = board.getPiece(expectedKingPosition);
        assertInstanceOf(King.class, actualKing);
        assertEquals(WHITE, actualKing.getPlayer());
    }

    @Test
    void testBlackLongCastle() {
        Pawn pawn = new Pawn(WHITE);
        Position pawnStartPosition = pf.create("A", 1);
        Position pawnEndPosition = pf.create("A", 2);
        board.placePiece(pawnStartPosition, pawn);
        moveMaker.make(context, new Move(pawn, pawnStartPosition, pawnEndPosition, new Metadata(MOVE)));
        Piece king = new King(BLACK);
        Position kingPosition = pf.create("E", 8);
        board.placePiece(kingPosition, king);
        Piece rook = new Rook(BLACK);
        Position rookPosition = pf.create("A", 8);
        board.placePiece(rookPosition, rook);
        List<Move> moves = pieceMoveGenerator
                .generate(context, king, kingPosition)
                .toList();
        assertEquals(1, moves.size());


        moveMaker.make(context, moves.getFirst());

        Position expectedRookPosition = pf.create("D", 8);
        assertTrue(board.hasPiece(expectedRookPosition));
        Piece actualRook = board.getPiece(expectedRookPosition);
        assertInstanceOf(Rook.class, actualRook);
        assertEquals(BLACK, actualRook.getPlayer());
        Position expectedKingPosition = pf.create("C", 8);
        assertTrue(board.hasPiece(expectedKingPosition));
        Piece actualKing = board.getPiece(expectedKingPosition);
        assertInstanceOf(King.class, actualKing);
        assertEquals(BLACK, actualKing.getPlayer());
    }

    @Test
    void testBlackShortCastle() {
        Pawn pawn = new Pawn(WHITE);
        Position pawnStartPosition = pf.create("A", 1);
        Position pawnEndPosition = pf.create("A", 2);
        board.placePiece(pawnStartPosition, pawn);
        moveMaker.make(context, new Move(pawn, pawnStartPosition, pawnEndPosition, new Metadata(MOVE)));
        Piece king = new King(BLACK);
        Position kingPosition = pf.create("E", 8);
        board.placePiece(kingPosition, king);
        Piece rook = new Rook(BLACK);
        Position rookPosition = pf.create("H", 8);
        board.placePiece(rookPosition, rook);
        List<Move> moves = pieceMoveGenerator
                .generate(context, king, kingPosition)
                .toList();
        assertEquals(1, moves.size());

        moveMaker.make(context, moves.getFirst());

        Position expectedRookPosition = pf.create("F", 8);
        assertTrue(board.hasPiece(expectedRookPosition));
        Piece actualRook = board.getPiece(expectedRookPosition);
        assertInstanceOf(Rook.class, actualRook);
        assertEquals(BLACK, actualRook.getPlayer());
        Position expectedKingPosition = pf.create("G", 8);
        assertTrue(board.hasPiece(expectedKingPosition));
        Piece actualKing = board.getPiece(expectedKingPosition);
        assertInstanceOf(King.class, actualKing);
        assertEquals(BLACK, actualKing.getPlayer());
    }

    @Test
    void testWhiteLongCastleImpossibleBecauseKingEndPositionIsUnderAttack() {
        board.placePiece(pf.create("C", 8), new Rook(BLACK));
        Piece king = new King(WHITE);
        Position kingPosition = pf.create("E", 1);
        board.placePiece(kingPosition, king);
        Piece rook = new Rook(WHITE);
        Position rookPosition = pf.create("A", 1);
        board.placePiece(rookPosition, rook);
        List<Move> moves = pieceMoveGenerator
                .generate(context, king, kingPosition)
                .toList();
        assertEquals(1, moves.size());

        Exception e = assertThrows(IllegalArgumentException.class, () -> moveMaker.make(context, moves.getFirst()));

        assertEquals("Move is not legal Move(piece=King(player=WHITE), start=Position(file=E, rank=1), end=Position(file=C, rank=1), metadata=Metadata(data={MOVE_TYPE=CASTLING, CASTLING_ROOK_START_POSITION=Position(file=A, rank=1), CASTLING_ROOK_END_POSITION=Position(file=D, rank=1)}))", e.getMessage());
    }

    @Test
    void testWhiteLongCastleImpossibleBecauseRookEndPositionIsUnderAttack() {
        board.placePiece(pf.create("D", 8), new Rook(BLACK));
        Piece king = new King(WHITE);
        Position kingPosition = pf.create("E", 1);
        board.placePiece(kingPosition, king);
        Piece rook = new Rook(WHITE);
        Position rookPosition = pf.create("A", 1);
        board.placePiece(rookPosition, rook);

        List<Move> moves = pieceMoveGenerator
                .generate(context, king, kingPosition)
                .toList();

        assertEquals(1, moves.size());

        Exception e = assertThrows(IllegalArgumentException.class, () -> moveMaker.make(context, moves.getFirst()));

        assertEquals("Move is not legal Move(piece=King(player=WHITE), start=Position(file=E, rank=1), end=Position(file=C, rank=1), metadata=Metadata(data={MOVE_TYPE=CASTLING, CASTLING_ROOK_START_POSITION=Position(file=A, rank=1), CASTLING_ROOK_END_POSITION=Position(file=D, rank=1)}))", e.getMessage());
    }
}
