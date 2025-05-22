package org.czareg.move;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.Context;
import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.piece.Piece;
import org.czareg.position.Position;

import static org.czareg.game.Metadata.Key.*;

@Slf4j
public class ClassicMoveExecutor implements MoveExecutor {

    @Override
    public void execute(Context context, Move move) {
        Board board = context.getBoard();
        Metadata metadata = move.getMetadata();
        MoveType moveType = metadata.get(MOVE_TYPE, MoveType.class).orElseThrow();
        Runnable runnable = switch (moveType) {
            case PROMOTION -> () -> executePromotion(move, board);
            case EN_PASSANT -> () -> executeEnPassant(move, board);
            case CASTLING -> () -> executeCastling(move, board);
            case CAPTURE -> () -> executeCapture(move, board);
            case MOVE, INITIAL_DOUBLE_FORWARD -> () -> executeMove(move, board);
        };
        runnable.run();
    }

    private void executePromotion(Move move, Board board) {
        Piece promotedPiece = move.getMetadata()
                .get(PROMOTION_PIECE, Piece.class)
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
                .get(EN_PASSANT_CAPTURE_PIECE_POSITION, Position.class)
                .orElseThrow(() -> new IllegalStateException("En passant move missing captured piece position."));
        log.debug("Executing en passant {}", move);
        board.removePiece(attackingPawnStart);
        board.removePiece(capturedPawnPosition);
        board.placePiece(attackingPawnEnd, move.getPiece());
    }

    private void executeCastling(Move move, Board board) {
        Metadata metadata = move.getMetadata();
        Position rookStart = metadata
                .get(CASTLING_ROOK_START_POSITION, Position.class)
                .orElseThrow(() -> new IllegalStateException("Castling move missing rook start position."));
        Position rookEnd = metadata
                .get(CASTLING_ROOK_END_POSITION, Position.class)
                .orElseThrow(() -> new IllegalStateException("Castling move missing rook end position."));
        Position kingStart = move.getStart();
        Position kingEnd = move.getEnd();
        log.debug("Executing castling {}", move);
        board.removePiece(kingStart);
        board.placePiece(kingEnd, move.getPiece());
        Piece rook = board.removePiece(rookStart);
        board.placePiece(rookEnd, rook);
    }

    private void executeCapture(Move move, Board board) {
        log.debug("Executing capture {}", move);
        Piece pieceToCaptureOnBoard = board.getPiece(move.getEnd());
        Piece pieceToCaptureInMove = move.getMetadata().get(CAPTURE_PIECE, Piece.class)
                .orElseThrow(() -> new IllegalStateException("Capture move missing capture piece"));
        if (pieceToCaptureOnBoard != pieceToCaptureInMove) {
            String message = "Piece to capture on board %s is different than the one declared in move %s".formatted(pieceToCaptureOnBoard, pieceToCaptureInMove);
            throw new IllegalStateException(message);
        }
        board.removePiece(move.getEnd());
        board.movePiece(move.getStart(), move.getEnd());
    }

    private void executeMove(Move move, Board board) {
        log.debug("Executing move {}", move);
        board.movePiece(move.getStart(), move.getEnd());
    }
}
