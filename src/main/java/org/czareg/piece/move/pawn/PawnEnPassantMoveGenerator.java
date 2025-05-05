package org.czareg.piece.move.pawn;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.*;
import org.czareg.piece.Pawn;
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

@Slf4j
public class PawnEnPassantMoveGenerator implements PawnMoveGenerator {

    @Override
    public Stream<Move> generate(Game game, Pawn pawn, Position currentPosition) {
        List<Move> moves = new ArrayList<>();
        Player player = pawn.getPlayer();
        for (IndexChange captureTargetIndexChange : getEndPositionIndexChanges(player)) {
            generate(game, pawn, currentPosition, captureTargetIndexChange).ifPresent(moves::add);
        }
        return moves.stream();
    }

    @Override
    public Optional<Move> generate(Game game, Pawn pawn, Position currentPosition, IndexChange endPositionIndexChange) {
        log.debug("Generating move for {} at {} and {}.", pawn, currentPosition, endPositionIndexChange);
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Player player = pawn.getPlayer();
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
        if (targetPiece.getPlayer() == player || !(targetPiece instanceof Pawn)) {
            log.debug("Rejecting move because target {} is occupied by friendly {}.", targetPosition, targetPiece);
            return Optional.empty();
        }
        History history = game.getHistory();
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
        if (!lastPlayedMoveMetadata.isExactly(Metadata.Key.MOVE_TYPE, MoveType.PAWN_DOUBLE_FORWARD)) {
            log.debug("Rejecting move because last played move done by {} was not double forward move.", lastMovedPiece);
            return Optional.empty();
        }
        Metadata metadata = new Metadata()
                .put(Metadata.Key.CAPTURE_PIECE, targetPiece)
                .put(Metadata.Key.CAPTURE_PIECE_POSITION, targetPosition)
                .put(Metadata.Key.MOVE_TYPE, MoveType.EN_PASSANT);
        Move move = new Move(pawn, currentPosition, endPosition, metadata);
        log.debug("Accepted move {}", move);
        return Optional.of(move);
    }

    private IndexChange getCaptureTargetIndexChanges(IndexChange endPositionIndexChange) {
        return new IndexChange(0, endPositionIndexChange.getFile());
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
