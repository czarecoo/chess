package org.czareg.move.piece.pawn;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.*;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.shared.PromotionRankChecker;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.position.Index;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class PawnDoubleForwardMoveGenerator implements PieceMoveGenerator, PromotionRankChecker {

    @Override
    public Stream<Move> generate(Context context, Piece piece, Position currentPosition) {
        Player player = piece.getPlayer();
        IndexChange endPositionIndexChange = getEndPositionIndexChange(player);
        return generate(context, piece, currentPosition, endPositionIndexChange).stream();
    }

    @Override
    public Optional<Move> generate(Context context, Piece piece, Position currentPosition, IndexChange endPositionIndexChange) {
        log.debug("Generating move for {} at {} and {}.", piece, currentPosition, endPositionIndexChange);
        History history = context.getHistory();
        if (history.hasPieceMovedBefore(piece)) {
            log.debug("Rejecting move because it was already moved.");
            return Optional.empty();
        }
        Board board = context.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
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
        Player player = piece.getPlayer();
        if (isOnPromotionRank(endPosition, positionFactory, player)) {
            log.debug("Rejecting move because end {} is on promotion rank.", endPosition);
            return Optional.empty();
        }
        IndexChange middlePositionIndexChange = getMiddlePositionIndexChange(player);
        Optional<Position> optionalMiddlePosition = positionFactory.create(currentPositionIndex, middlePositionIndexChange);
        Position middlePosition = optionalMiddlePosition.orElseThrow(); // we checked end position and we know it is on board so the middle position has to be on board too
        if (board.hasPiece(middlePosition)) {
            Piece middlePositionOccupyingPiece = board.getPiece(middlePosition);
            log.debug("Rejecting move, because middle {} is occupied by {}.", middlePosition, middlePositionOccupyingPiece);
            return Optional.empty();
        }
        Metadata metadata = new Metadata(getMoveType());
        Move move = new Move(piece, currentPosition, endPosition, metadata);
        log.debug("Accepted move {}.", move);
        return Optional.of(move);
    }

    private IndexChange getEndPositionIndexChange(Player player) {
        return switch (player) {
            case WHITE -> new IndexChange(2, 0);
            case BLACK -> new IndexChange(-2, 0);
        };
    }

    private IndexChange getMiddlePositionIndexChange(Player player) {
        return switch (player) {
            case WHITE -> new IndexChange(1, 0);
            case BLACK -> new IndexChange(-1, 0);
        };
    }

    @Override
    public MoveType getMoveType() {
        return MoveType.INITIAL_DOUBLE_FORWARD;
    }
}
