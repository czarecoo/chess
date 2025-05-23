package org.czareg.move.piece.shared;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.Context;
import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.move.piece.Directional;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.position.Index;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.stream.Stream;

import static org.czareg.game.Metadata.Key.CAPTURE_PIECE;

@Slf4j
public abstract class JumpCaptureMoveGenerator implements PieceMoveGenerator, Directional {

    public boolean isInvalid(Context context, Logger log, Move move) {
        return false;
    }

    @Override
    public Stream<Move> generate(Context context, Piece piece, Position currentPosition) {
        return getDirections().map(endPositionIndexChange -> generate(context, piece, currentPosition, endPositionIndexChange))
                .filter(Optional::isPresent)
                .map(Optional::get);
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
        if (!board.hasPiece(endPosition)) {
            log.debug("Rejecting move because target {} is empty.", endPosition);
            return Optional.empty();
        }
        Piece targetPiece = board.getPiece(endPosition);
        if (targetPiece.getPlayer() == player) {
            log.debug("Rejecting move because target {} is occupied by friendly {}.", endPosition, targetPiece);
            return Optional.empty();
        }
        Metadata metadata = new Metadata(getMoveType())
                .put(CAPTURE_PIECE, targetPiece);
        Move move = new Move(piece, currentPosition, endPosition, metadata);
        if (isInvalid(context, log, move)) {
            return Optional.empty();
        }
        log.debug("Accepted move {}", move);
        return Optional.of(move);
    }

    @Override
    public MoveType getMoveType() {
        return MoveType.CAPTURE;
    }
}
