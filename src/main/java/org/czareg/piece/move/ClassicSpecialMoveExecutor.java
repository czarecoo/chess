package org.czareg.piece.move;

import org.czareg.board.Board;
import org.czareg.game.Game;
import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.piece.Piece;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

public class ClassicSpecialMoveExecutor implements SpecialMoveExecutor {

    @Override
    public void execute(Move move, Game game) {
        Board board = game.getBoard();
        switch (getSpecialMoveType(move)) {
            case PROMOTION -> handlePromotion(move, board);
            case EN_PASSANT -> handleEnPassant(move, board);
            case CASTLING -> handleCastling(move, board);
        }
    }

    private void handlePromotion(Move move, Board board) {
        Piece promotedPiece = move.getMetadata()
                .get(Metadata.Key.PROMOTION_PIECE, Piece.class)
                .orElseThrow(() -> new IllegalStateException("Promotion move missing chosen piece."));
        board.removePiece(move.getStart());
        board.placePiece(move.getEnd(), promotedPiece);
    }

    private void handleEnPassant(Move move, Board board) {
        PositionFactory positionFactory = board.getPositionFactory();
        Position attackingPawnStart = move.getStart();
        Position attackingPawnEnd = move.getEnd();
        int capturedPawnRank = attackingPawnStart.getRank();
        String capturedPawnFile = attackingPawnEnd.getFile();
        Position capturedPawnPosition = positionFactory.create(capturedPawnRank, capturedPawnFile);
        board.removePiece(attackingPawnStart);
        board.removePiece(capturedPawnPosition);
        board.placePiece(attackingPawnEnd, move.getPiece());
    }

    private void handleCastling(Move move, Board board) {
        Metadata metadata = move.getMetadata();
        Position rookStart = metadata
                .get(Metadata.Key.CASTLING_ROOK_START_POSITION, Position.class)
                .orElseThrow(() -> new IllegalStateException("Castling move missing rook start"));
        Position rookEnd = metadata
                .get(Metadata.Key.CASTLING_ROOK_END_POSITION, Position.class)
                .orElseThrow(() -> new IllegalStateException("Castling move missing rook end"));
        Position kingStart = move.getStart();
        Position kingEnd = move.getEnd();
        board.removePiece(kingStart);
        board.placePiece(kingEnd, move.getPiece());
        Piece rook = board.removePiece(rookStart);
        board.placePiece(rookEnd, rook);
    }
}
