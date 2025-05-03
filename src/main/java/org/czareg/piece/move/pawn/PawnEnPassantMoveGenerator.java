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
        log.debug("Generating moves for {} at {}.", pawn, currentPosition);
        List<Move> moves = new ArrayList<>();
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Player player = pawn.getPlayer();
        Index currentPositionIndex = positionFactory.create(currentPosition);
        for (IndexChange captureTargetIndexChange : getCaptureTargetIndexChanges()) {
            log.debug("Generating move for {}", captureTargetIndexChange);
            Optional<Position> optionalTargetPosition = positionFactory.create(currentPositionIndex, captureTargetIndexChange);
            if (optionalTargetPosition.isEmpty()) {
                log.debug("Rejecting move because target position is not valid on the board ({}, {}).", currentPositionIndex, captureTargetIndexChange);
                continue;
            }
            Position targetPosition = optionalTargetPosition.get();
            if (!board.hasPiece(targetPosition)) {
                log.debug("Rejecting move because target {} is empty.", targetPosition);
                continue;
            }
            Piece targetPiece = board.getPiece(targetPosition);
            if (targetPiece.getPlayer() == player || !(targetPiece instanceof Pawn)) {
                log.debug("Rejecting move because target {} is occupied by friendly {}.", targetPosition, targetPiece);
                continue;
            }
            History history = game.getHistory();
            Optional<Move> optionalLastPlayedMove = history.getLastPlayedMove();
            if (optionalLastPlayedMove.isEmpty()) {
                log.debug("Rejecting move because there is no previous move.");
                continue;
            }
            Move lastPlayedMove = optionalLastPlayedMove.orElseThrow();
            Piece lastMovedPiece = lastPlayedMove.getPiece();
            if (lastMovedPiece != targetPiece) {
                log.debug("Rejecting move because last played move was done by {} and not target {}.", lastMovedPiece, targetPiece);
                continue;
            }
            Position lastPlayedMoveEndPosition = lastPlayedMove.getEnd();
            if (!targetPosition.equals(lastPlayedMoveEndPosition)) {
                log.debug("Rejecting move because last played move end {} does not equal target {}.", lastPlayedMoveEndPosition, targetPosition);
                continue;
            }
            Metadata lastPlayedMoveMetadata = lastPlayedMove.getMetadata();
            if (!lastPlayedMoveMetadata.isExactly(Metadata.Key.MOVE_TYPE, MoveType.PAWN_DOUBLE_FORWARD)) {
                log.debug("Rejecting move because last played move done by {} was not double forward move.", lastMovedPiece);
                continue;
            }
            IndexChange endPositionIndexChange = getEndPositionIndexChanges(player, captureTargetIndexChange);
            Optional<Position> optionalEndPosition = positionFactory.create(currentPositionIndex, endPositionIndexChange);
            if (optionalEndPosition.isEmpty()) {
                log.debug("Rejecting move because end position is not valid on the board ({}, {}).", currentPositionIndex, endPositionIndexChange);
                continue;
            }
            Position endPosition = optionalEndPosition.get();
            if (board.hasPiece(endPosition)) {
                Piece endPositionOccupyingPiece = board.getPiece(endPosition);
                log.debug("Rejecting move because end {} is occupied by {}.", endPosition, endPositionOccupyingPiece);
                continue;
            }
            Metadata metadata = new Metadata();
            metadata.put(Metadata.Key.CAPTURED_PIECE, targetPiece);
            metadata.put(Metadata.Key.CAPTURED_PIECE_POSITION, targetPosition);
            metadata.put(Metadata.Key.MOVE_TYPE, MoveType.EN_PASSANT);
            Move move = new Move(pawn, currentPosition, endPosition, metadata);
            moves.add(move);
            log.debug("Accepted move: {}", move);
        }
        return moves.stream();
    }

    private List<IndexChange> getCaptureTargetIndexChanges() {
        return List.of(
                new IndexChange(0, -1),
                new IndexChange(0, 1)
        );
    }

    private IndexChange getEndPositionIndexChanges(Player player, IndexChange captureTargetIndexChange) {
        int file = captureTargetIndexChange.getFile();
        return switch (player) {
            case WHITE -> new IndexChange(1, file);
            case BLACK -> new IndexChange(-1, file);
        };
    }
}
