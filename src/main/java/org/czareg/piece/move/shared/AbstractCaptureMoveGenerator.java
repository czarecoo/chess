package org.czareg.piece.move.shared;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.Game;
import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.piece.move.PieceMoveGenerator;
import org.czareg.position.Index;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.Optional;
import java.util.stream.Stream;

import static org.czareg.game.Metadata.Key.*;

@Slf4j
public abstract class AbstractCaptureMoveGenerator implements PieceMoveGenerator, Directional {

    @Override
    public Stream<Move> generate(Game game, Piece piece, Position currentPosition) {
        return getDirections()
                .map(direction -> searchCapture(game, piece, currentPosition, direction))
                .flatMap(Optional::stream);
    }

    @Override
    public Optional<Move> generate(Game game, Piece piece, Position currentPosition, IndexChange endPositionIndexChange) {
        log.debug("Generating move for {} at {} and {}.", piece, currentPosition, endPositionIndexChange);
        Board board = game.getBoard();
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
        Metadata metadata = new Metadata(MOVE_TYPE, getMoveType())
                .put(CAPTURE_PIECE, targetPiece)
                .put(CAPTURE_PIECE_POSITION, endPosition);

        Move move = new Move(piece, currentPosition, endPosition, metadata);
        log.debug("Accepted move {}.", move);
        return Optional.of(move);
    }

    private Optional<Move> searchCapture(Game game, Piece piece, Position currentPosition, IndexChange direction) {
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Index checkedPositionIndex = positionFactory.create(currentPosition);
        while (true) {
            Optional<Position> optionalEndPosition = positionFactory.create(checkedPositionIndex, direction);
            if (optionalEndPosition.isEmpty()) {
                log.debug("Rejecting move because end position is not valid on the board ({}, {}).", checkedPositionIndex, direction);
                return Optional.empty();
            }
            Position endPosition = optionalEndPosition.get();
            if (!board.hasPiece(endPosition)) {
                checkedPositionIndex = positionFactory.create(endPosition);
                log.debug("Continuing search since {} is empty.", endPosition);
                continue;
            }
            IndexChange endPositionIndexChange = positionFactory.create(currentPosition, endPosition);
            return generate(game, piece, currentPosition, endPositionIndexChange);
        }
    }
}
