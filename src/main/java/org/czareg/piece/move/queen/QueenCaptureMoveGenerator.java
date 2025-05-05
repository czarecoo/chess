package org.czareg.piece.move.queen;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.Game;
import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.piece.Queen;
import org.czareg.piece.move.pawn.QueenMoveGenerator;
import org.czareg.position.Index;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.czareg.game.Metadata.Key.*;

@Slf4j
public class QueenCaptureMoveGenerator implements QueenMoveGenerator {

    private static final List<IndexChange> DIRECTIONS = List.of(
            new IndexChange(-1, 0), // up
            new IndexChange(1, 0),  // down
            new IndexChange(0, -1), // left
            new IndexChange(0, 1),  // right
            new IndexChange(-1, -1), // up-left
            new IndexChange(-1, 1),  // up-right
            new IndexChange(1, -1),  // down-left
            new IndexChange(1, 1)    // down-right
    );

    @Override
    public Stream<Move> generate(Game game, Queen queen, Position currentPosition) {
        return DIRECTIONS.stream()
                .map(direction -> searchCapture(game, queen, currentPosition, direction))
                .flatMap(Optional::stream);
    }

    @Override
    public Optional<Move> generate(Game game, Queen queen, Position currentPosition, IndexChange endPositionIndexChange) {
        log.debug("Generating move for {} at {} and {}.", queen, currentPosition, endPositionIndexChange);
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Player player = queen.getPlayer();
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
        Metadata metadata = new Metadata()
                .put(MOVE_TYPE, MoveType.QUEEN_CAPTURE)
                .put(CAPTURE_PIECE, targetPiece)
                .put(CAPTURE_PIECE_POSITION, endPosition);

        Move move = new Move(queen, currentPosition, endPosition, metadata);
        log.debug("Accepted move {}.", move);
        return Optional.of(move);
    }

    @Override
    public MoveType getMoveType() {
        return MoveType.QUEEN_CAPTURE;
    }

    private Optional<Move> searchCapture(Game game, Queen queen, Position currentPosition, IndexChange direction) {
        log.debug("Searching capture moves for {} at {} and {}.", queen, currentPosition, direction);
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
            return generate(game, queen, currentPosition, endPositionIndexChange);
        }
    }
}
