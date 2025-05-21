package org.czareg;

import org.czareg.board.Board;
import org.czareg.game.*;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.king.KingCastlingMoveGenerator;
import org.czareg.piece.King;
import org.czareg.piece.Pawn;
import org.czareg.piece.Piece;
import org.czareg.piece.Rook;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.czareg.game.MoveType.MOVE;
import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class KingCastlingTests {

    private Context context;
    private Board board;
    private PositionFactory pf;
    private PieceMoveGenerator pieceMoveGenerator;
    private Game game;

    @BeforeEach
    void setUp() {
        context = new ClassicContext();
        game = context.getGame();
        board = context.getBoard();
        pf = board.getPositionFactory();
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

        game.makeMove(context, moves.getFirst());

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

        game.makeMove(context, moves.getFirst());

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
        game.makeMove(context, new Move(pawn, pawnStartPosition, pawnEndPosition, new Metadata(MOVE)));
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


        game.makeMove(context, moves.getFirst());

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
        game.makeMove(context, new Move(pawn, pawnStartPosition, pawnEndPosition, new Metadata(MOVE)));
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

        game.makeMove(context, moves.getFirst());

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

        assertEquals(0, moves.size());
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

        assertEquals(0, moves.size());
    }
}
