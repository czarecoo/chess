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
        Position kingPosition = pf.create(1, "E");
        board.placePiece(kingPosition, king);
        Piece rook = new Rook(WHITE);
        Position rookPosition = pf.create(1, "A");
        board.placePiece(rookPosition, rook);
        List<Move> moves = pieceMoveGenerator
                .generate(context, king, kingPosition)
                .toList();
        assertEquals(1, moves.size());

        moveMaker.make(context, moves.getFirst());

        Position expectedRookPosition = pf.create(1, "D");
        assertTrue(board.hasPiece(expectedRookPosition));
        Piece actualRook = board.getPiece(expectedRookPosition);
        assertInstanceOf(Rook.class, actualRook);
        assertEquals(WHITE, actualRook.getPlayer());
        Position expectedKingPosition = pf.create(1, "C");
        assertTrue(board.hasPiece(expectedKingPosition));
        Piece actualKing = board.getPiece(expectedKingPosition);
        assertInstanceOf(King.class, actualKing);
        assertEquals(WHITE, actualKing.getPlayer());
    }

    @Test
    void testWhiteShortCastle() {
        Piece king = new King(WHITE);
        Position kingPosition = pf.create(1, "E");
        board.placePiece(kingPosition, king);
        Piece rook = new Rook(WHITE);
        Position rookPosition = pf.create(1, "H");
        board.placePiece(rookPosition, rook);
        List<Move> moves = pieceMoveGenerator
                .generate(context, king, kingPosition)
                .toList();
        assertEquals(1, moves.size());

        moveMaker.make(context, moves.getFirst());

        Position expectedRookPosition = pf.create(1, "F");
        assertTrue(board.hasPiece(expectedRookPosition));
        Piece actualRook = board.getPiece(expectedRookPosition);
        assertInstanceOf(Rook.class, actualRook);
        assertEquals(WHITE, actualRook.getPlayer());
        Position expectedKingPosition = pf.create(1, "G");
        assertTrue(board.hasPiece(expectedKingPosition));
        Piece actualKing = board.getPiece(expectedKingPosition);
        assertInstanceOf(King.class, actualKing);
        assertEquals(WHITE, actualKing.getPlayer());
    }

    @Test
    void testBlackLongCastle() {
        Pawn pawn = new Pawn(WHITE);
        Position pawnStartPosition = pf.create(1, "A");
        Position pawnEndPosition = pf.create(2, "A");
        board.placePiece(pawnStartPosition, pawn);
        moveMaker.make(context, new Move(pawn, pawnStartPosition, pawnEndPosition, new Metadata(MOVE)));
        Piece king = new King(BLACK);
        Position kingPosition = pf.create(8, "E");
        board.placePiece(kingPosition, king);
        Piece rook = new Rook(BLACK);
        Position rookPosition = pf.create(8, "A");
        board.placePiece(rookPosition, rook);
        List<Move> moves = pieceMoveGenerator
                .generate(context, king, kingPosition)
                .toList();
        assertEquals(1, moves.size());


        moveMaker.make(context, moves.getFirst());

        Position expectedRookPosition = pf.create(8, "D");
        assertTrue(board.hasPiece(expectedRookPosition));
        Piece actualRook = board.getPiece(expectedRookPosition);
        assertInstanceOf(Rook.class, actualRook);
        assertEquals(BLACK, actualRook.getPlayer());
        Position expectedKingPosition = pf.create(8, "C");
        assertTrue(board.hasPiece(expectedKingPosition));
        Piece actualKing = board.getPiece(expectedKingPosition);
        assertInstanceOf(King.class, actualKing);
        assertEquals(BLACK, actualKing.getPlayer());
    }

    @Test
    void testBlackShortCastle() {
        Pawn pawn = new Pawn(WHITE);
        Position pawnStartPosition = pf.create(1, "A");
        Position pawnEndPosition = pf.create(2, "A");
        board.placePiece(pawnStartPosition, pawn);
        moveMaker.make(context, new Move(pawn, pawnStartPosition, pawnEndPosition, new Metadata(MOVE)));
        Piece king = new King(BLACK);
        Position kingPosition = pf.create(8, "E");
        board.placePiece(kingPosition, king);
        Piece rook = new Rook(BLACK);
        Position rookPosition = pf.create(8, "H");
        board.placePiece(rookPosition, rook);
        List<Move> moves = pieceMoveGenerator
                .generate(context, king, kingPosition)
                .toList();
        assertEquals(1, moves.size());

        moveMaker.make(context, moves.getFirst());

        Position expectedRookPosition = pf.create(8, "F");
        assertTrue(board.hasPiece(expectedRookPosition));
        Piece actualRook = board.getPiece(expectedRookPosition);
        assertInstanceOf(Rook.class, actualRook);
        assertEquals(BLACK, actualRook.getPlayer());
        Position expectedKingPosition = pf.create(8, "G");
        assertTrue(board.hasPiece(expectedKingPosition));
        Piece actualKing = board.getPiece(expectedKingPosition);
        assertInstanceOf(King.class, actualKing);
        assertEquals(BLACK, actualKing.getPlayer());
    }

    @Test
    void testWhiteLongCastleImpossibleBecauseKingEndPositionIsUnderAttack() {
        board.placePiece(pf.create(8, "C"), new Rook(BLACK));
        Piece king = new King(WHITE);
        Position kingPosition = pf.create(1, "E");
        board.placePiece(kingPosition, king);
        Piece rook = new Rook(WHITE);
        Position rookPosition = pf.create(1, "A");
        board.placePiece(rookPosition, rook);
        List<Move> moves = pieceMoveGenerator
                .generate(context, king, kingPosition)
                .toList();
        assertEquals(1, moves.size());

        Exception e = assertThrows(IllegalArgumentException.class, () -> moveMaker.make(context, moves.getFirst()));

        assertEquals("Move is not legal Move(piece=King(player=WHITE), start=Position(rank=1, file=E), end=Position(rank=1, file=C), metadata=Metadata(data={MOVE_TYPE=CASTLING, CASTLING_ROOK_START_POSITION=Position(rank=1, file=A), CASTLING_ROOK_END_POSITION=Position(rank=1, file=D)}))", e.getMessage());
    }

    @Test
    void testWhiteLongCastleImpossibleBecauseRookEndPositionIsUnderAttack() {
        board.placePiece(pf.create(8, "D"), new Rook(BLACK));
        Piece king = new King(WHITE);
        Position kingPosition = pf.create(1, "E");
        board.placePiece(kingPosition, king);
        Piece rook = new Rook(WHITE);
        Position rookPosition = pf.create(1, "A");
        board.placePiece(rookPosition, rook);

        List<Move> moves = pieceMoveGenerator
                .generate(context, king, kingPosition)
                .toList();

        assertEquals(1, moves.size());

        Exception e = assertThrows(IllegalArgumentException.class, () -> moveMaker.make(context, moves.getFirst()));

        assertEquals("Move is not legal Move(piece=King(player=WHITE), start=Position(rank=1, file=E), end=Position(rank=1, file=C), metadata=Metadata(data={MOVE_TYPE=CASTLING, CASTLING_ROOK_START_POSITION=Position(rank=1, file=A), CASTLING_ROOK_END_POSITION=Position(rank=1, file=D)}))", e.getMessage());
    }
}
