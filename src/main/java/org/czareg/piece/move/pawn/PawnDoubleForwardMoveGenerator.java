package org.czareg.piece.move.pawn;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.board.BoardSize;
import org.czareg.game.*;
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
public class PawnDoubleForwardMoveGenerator implements PawnMoveGenerator {

    @Override
    public Stream<Move> generate(Game game, Pawn pawn, Position currentPosition) {
        log.debug("Generating moves for {} at {}.", pawn, currentPosition);
        History history = game.getHistory();
        Board board = game.getBoard();
        if (history.hasPieceMovedBefore(pawn)) {
            log.debug("Rejecting move because it was already moved.");
            return Stream.empty();
        }
        PositionFactory positionFactory = board.getPositionFactory();
        Player player = pawn.getPlayer();
        IndexChange endPositionIndexChange = getEndPositionIndexChange(player);
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
        BoardSize boardSize = board.getBoardSize();
        if (isOnPromotionRank(endPosition, boardSize, player)) {
            log.debug("Rejecting move because end {} is a promotion rank.", endPosition);
            return Stream.empty();
        }
        IndexChange middlePositionIndexChange = getMiddlePositionIndexChange(player);
        Optional<Position> optionalMiddlePosition = positionFactory.create(currentPositionIndex, middlePositionIndexChange);
        Position middlePosition = optionalMiddlePosition.orElseThrow(); // we checked end position and we know it is on board, the middle position has to be on board too
        if (board.hasPiece(middlePosition)) {
            Piece middlePositionOccupyingPiece = board.getPiece(middlePosition);
            log.debug("Rejecting move, because middle {} is occupied by {}.", middlePosition, middlePositionOccupyingPiece);
            return Stream.empty();
        }
        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.MOVE_TYPE, MoveType.PAWN_DOUBLE_FORWARD);
        Move move = new Move(pawn, currentPosition, endPosition, metadata);
        log.debug("Accepted move: {}.", move);
        return Stream.of(move);
    }

    private IndexChange getEndPositionIndexChange(Player player) {
        return switch (player) {
            case WHITE -> new IndexChange(2, 0);
            case BLACK -> new IndexChange(-2, 0);
        };
    }

    private boolean isOnPromotionRank(Position position, BoardSize boardSize, Player player) {
        int rank = position.getRank();
        return switch (player) {
            case WHITE -> rank == boardSize.getRanks();
            case BLACK -> rank == 1;
        };
    }

    private IndexChange getMiddlePositionIndexChange(Player player) {
        return switch (player) {
            case WHITE -> new IndexChange(1, 0);
            case BLACK -> new IndexChange(-1, 0);
        };
    }
}
