package org.czareg.piece.move.rook;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.Game;
import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.piece.Piece;
import org.czareg.position.Index;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.czareg.game.Metadata.Key.MOVE_TYPE;

@Slf4j
public class RookMoveMoveGenerator implements RookMoveGenerator {

    @Override
    public Stream<Move> generate(Game game, Piece piece, Position currentPosition) {
        List<Move> moves = new ArrayList<>();
        for (IndexChange direction : getDirections().toList()) {
            moves.addAll(searchMoves(game, piece, currentPosition, direction));
        }
        return moves.stream();
    }

    @Override
    public Optional<Move> generate(Game game, Piece piece, Position currentPosition, IndexChange endPositionIndexChange) {
        log.debug("Generating move for {} at {} and {}.", piece, currentPosition, endPositionIndexChange);
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Index currentPositionIndex = positionFactory.create(currentPosition);
        Optional<Position> optionalEndPosition = positionFactory.create(currentPositionIndex, endPositionIndexChange);
        if (optionalEndPosition.isEmpty()) {
            log.debug("Rejecting move because end position is not valid on the board ({}, {}).", currentPositionIndex, endPositionIndexChange);
            return Optional.empty();
        }
        Position endPosition = optionalEndPosition.get();
        if (board.hasPiece(endPosition)) {
            Piece targetPiece = board.getPiece(endPosition);
            log.debug("Rejecting move because end {} is occupied by {}.", endPosition, targetPiece);
            return Optional.empty();
        }
        Metadata metadata = new Metadata(MOVE_TYPE, getMoveType());
        Move move = new Move(piece, currentPosition, endPosition, metadata);
        log.debug("Accepted move {}.", move);
        return Optional.of(move);
    }

    @Override
    public MoveType getMoveType() {
        return MoveType.ROOK_MOVE;
    }

    private List<Move> searchMoves(Game game, Piece piece, Position currentPosition, IndexChange direction) {
        List<Move> moves = new ArrayList<>();
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Index checkedPositionIndex = positionFactory.create(currentPosition);
        while (true) {
            Optional<Position> optionalEndPosition = positionFactory.create(checkedPositionIndex, direction);
            if (optionalEndPosition.isEmpty()) {
                log.debug("Rejecting move because end position is not valid on the board ({}, {}).", checkedPositionIndex, direction);
                return moves;
            }
            Position endPosition = optionalEndPosition.get();
            if (board.hasPiece(endPosition)) {
                Piece targetPiece = board.getPiece(endPosition);
                log.debug("Rejecting move because end {} is occupied by {}.", endPosition, targetPiece);
                return moves;
            }
            checkedPositionIndex = positionFactory.create(endPosition);
            IndexChange endPositionIndexChange = positionFactory.create(currentPosition, endPosition);
            generate(game, piece, currentPosition, endPositionIndexChange).ifPresent(moves::add);
        }
    }
}
