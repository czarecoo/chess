package org.czareg.move.piece.pawn;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.*;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.position.Index;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.czareg.game.Metadata.Key.*;
import static org.czareg.game.MoveType.INITIAL_DOUBLE_FORWARD;

@Slf4j
public class PawnEnPassantMoveGenerator implements PieceMoveGenerator {

    @Override
    public Stream<Move> generate(Context context, Piece piece, Position currentPosition) {
        List<Move> moves = new ArrayList<>();
        Player player = piece.getPlayer();
        for (IndexChange captureTargetIndexChange : getEndPositionIndexChanges(player)) {
            generate(context, piece, currentPosition, captureTargetIndexChange).ifPresent(moves::add);
        }
        return moves.stream();
    }

    @Override
    public Optional<Move> generate(Context context, Piece piece, Position currentPosition, IndexChange endPositionIndexChange) {
        log.debug("Generating move for {} at {} and {}.", piece, currentPosition, endPositionIndexChange);
        Board board = context.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Player player = piece.getPlayer();
        Index currentPositionIndex = positionFactory.create(currentPosition);
        Optional<Position> optionalEndPosition = positionFactory.create(currentPositionIndex, endPositionIndexChange);
        if (optionalEndPosition.isEmpty()) {
            log.debug("Rejecting move because end position is not valid on the board ({}, {}).", currentPositionIndex, endPositionIndexChange);
            return Optional.empty();
        }
        Position endPosition = optionalEndPosition.get();
        if (board.hasPiece(endPosition)) {
            Piece endPositionOccupyingPiece = board.getPiece(endPosition);
            log.debug("Rejecting move because end {} is occupied by {}.", endPosition, endPositionOccupyingPiece);
            return Optional.empty();
        }
        IndexChange captureTargetIndexChange = getCaptureTargetIndexChanges(endPositionIndexChange);
        Optional<Position> optionalTargetPosition = positionFactory.create(currentPositionIndex, captureTargetIndexChange);
        if (optionalTargetPosition.isEmpty()) {
            log.debug("Rejecting move because target position is not valid on the board ({}, {}).", currentPositionIndex, captureTargetIndexChange);
            return Optional.empty();
        }
        Position targetPosition = optionalTargetPosition.get();
        if (!board.hasPiece(targetPosition)) {
            log.debug("Rejecting move because target {} is empty.", targetPosition);
            return Optional.empty();
        }
        Piece targetPiece = board.getPiece(targetPosition);
        if (targetPiece.getPlayer() == player) {
            log.debug("Rejecting move because target {} is occupied by friendly {}.", targetPosition, targetPiece);
            return Optional.empty();
        }
        if (piece.getClass() != targetPiece.getClass()) {
            log.debug("Rejecting move because target {} is not of the same type than moving {}.", targetPiece, piece);
            return Optional.empty();
        }
        History history = context.getHistory();
        Optional<Move> optionalLastPlayedMove = history.getLastPlayedMove();
        if (optionalLastPlayedMove.isEmpty()) {
            log.debug("Rejecting move because there is no previous move.");
            return Optional.empty();
        }
        Move lastPlayedMove = optionalLastPlayedMove.orElseThrow();
        Piece lastMovedPiece = lastPlayedMove.getPiece();
        if (lastMovedPiece != targetPiece) {
            log.debug("Rejecting move because last played move was done by {} and not target {}.", lastMovedPiece, targetPiece);
            return Optional.empty();
        }
        Position lastPlayedMoveEndPosition = lastPlayedMove.getEnd();
        if (!targetPosition.equals(lastPlayedMoveEndPosition)) {
            log.debug("Rejecting move because last played move end {} does not equal target {}.", lastPlayedMoveEndPosition, targetPosition);
            return Optional.empty();
        }
        Metadata lastPlayedMoveMetadata = lastPlayedMove.getMetadata();
        if (!lastPlayedMoveMetadata.isExactly(MOVE_TYPE, INITIAL_DOUBLE_FORWARD)) {
            log.debug("Rejecting move because last played move done by {} was not double forward move.", lastMovedPiece);
            return Optional.empty();
        }
        Metadata metadata = new Metadata(getMoveType())
                .put(CAPTURE_PIECE, targetPiece)
                .put(EN_PASSANT_CAPTURE_PIECE_POSITION, targetPosition);
        Move move = new Move(piece, currentPosition, endPosition, metadata);
        log.debug("Accepted move {}", move);
        return Optional.of(move);
    }

    private IndexChange getCaptureTargetIndexChanges(IndexChange endPositionIndexChange) {
        return new IndexChange(0, endPositionIndexChange.getFileChange());
    }

    private List<IndexChange> getEndPositionIndexChanges(Player player) {
        return switch (player) {
            case WHITE -> List.of(
                    new IndexChange(1, -1),
                    new IndexChange(1, 1)
            );
            case BLACK -> List.of(
                    new IndexChange(-1, -1),
                    new IndexChange(-1, 1)
            );
        };
    }

    @Override
    public MoveType getMoveType() {
        return MoveType.EN_PASSANT;
    }
}
