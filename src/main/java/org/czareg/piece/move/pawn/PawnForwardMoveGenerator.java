package org.czareg.piece.move.pawn;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.Game;
import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.piece.Pawn;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.position.Index;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class PawnForwardMoveGenerator implements PawnMoveGenerator {

    @Override
    public Stream<Move> generate(Game game, Pawn pawn, Position currentPosition) {
        log.debug("Generating moves for {} at {}.", pawn, currentPosition);
        Board board = game.getBoard();
        Player player = pawn.getPlayer();
        IndexChange endPositionIndexChange = getEndPositionIndexChange(player);
        PositionFactory positionFactory = board.getPositionFactory();
        Index currentPositionIndex = positionFactory.create(currentPosition);
        Optional<Position> optionalEndPosition = positionFactory.create(currentPositionIndex, endPositionIndexChange);
        if (optionalEndPosition.isEmpty()) {
            log.debug("Rejecting move because end position is not valid on the board ({}, {}).", currentPositionIndex, endPositionIndexChange);
            return Stream.empty();
        }
        Position endPosition = optionalEndPosition.get();
        if (board.hasPiece(endPosition)) {
            Piece endPositionOccupyingPiece = board.getPiece(endPosition);
            log.debug("Rejecting move because end {} is occupied by {}.", endPosition, endPositionOccupyingPiece);
            return Stream.empty();
        }
        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.MOVE_TYPE, MoveType.PAWN_FORWARD);
        Move move = new Move(pawn, currentPosition, endPosition, metadata);
        log.debug("Accepted move: {}.", move);
        return Stream.of(move);
    }

    private IndexChange getEndPositionIndexChange(Player player) {
        return switch (player) {
            case WHITE -> new IndexChange(1, 0);
            case BLACK -> new IndexChange(-1, 0);
        };
    }
}
