package org.czareg.move;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.Game;
import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.piece.Piece;
import org.czareg.position.Position;

@Slf4j
public class ClassicMoveExecutor implements MoveExecutor {

    @Override
    public void execute(Move move, Game game) {
        Board board = game.getBoard();
        Metadata metadata = move.getMetadata();
        MoveType moveType = metadata.get(Metadata.Key.MOVE_TYPE, MoveType.class).orElseThrow();
        switch (moveType) {
            case PROMOTION -> executePromotion(move, board);
            case EN_PASSANT -> executeEnPassant(move, board);
            case CASTLING -> executeCastling(move, board);
            default -> executeDefault(move, board);
        }
    }

    private void executePromotion(Move move, Board board) {
        Piece promotedPiece = move.getMetadata()
                .get(Metadata.Key.PROMOTION_PIECE, Piece.class)
                .orElseThrow(() -> new IllegalStateException("Promotion move missing chosen piece."));
        Piece oldPiece = move.getPiece();
        if (oldPiece.getPlayer() != promotedPiece.getPlayer()) {
            throw new IllegalStateException("Cannot promote to different player %s %s".formatted(oldPiece, promotedPiece));
        }
        log.debug("Executing promotion {}", move);
        board.removePiece(move.getStart());
        board.placePiece(move.getEnd(), promotedPiece);
    }

    private void executeEnPassant(Move move, Board board) {
        Position attackingPawnStart = move.getStart();
        Position attackingPawnEnd = move.getEnd();
        Position capturedPawnPosition = move.getMetadata()
                .get(Metadata.Key.EN_PASSANT_CAPTURE_PIECE_POSITION, Position.class)
                .orElseThrow(() -> new IllegalStateException("En passant move missing captured piece position."));
        log.debug("Executing en passant {}", move);
        board.removePiece(attackingPawnStart);
        board.removePiece(capturedPawnPosition);
        board.placePiece(attackingPawnEnd, move.getPiece());
    }

    private void executeCastling(Move move, Board board) {
        Metadata metadata = move.getMetadata();
        Position rookStart = metadata
                .get(Metadata.Key.CASTLING_ROOK_START_POSITION, Position.class)
                .orElseThrow(() -> new IllegalStateException("Castling move missing rook start position."));
        Position rookEnd = metadata
                .get(Metadata.Key.CASTLING_ROOK_END_POSITION, Position.class)
                .orElseThrow(() -> new IllegalStateException("Castling move missing rook end position."));
        Position kingStart = move.getStart();
        Position kingEnd = move.getEnd();
        log.debug("Executing castling {}", move);
        board.removePiece(kingStart);
        board.placePiece(kingEnd, move.getPiece());
        Piece rook = board.removePiece(rookStart);
        board.placePiece(rookEnd, rook);
    }

    private void executeDefault(Move move, Board board) {
        log.debug("Executing default {}", move);
        board.movePiece(move.getStart(), move.getEnd());
    }
}
