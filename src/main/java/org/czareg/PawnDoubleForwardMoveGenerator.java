package org.czareg;

import lombok.extern.slf4j.Slf4j;
import org.czareg.position.Index;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
final class PawnDoubleForwardMoveGenerator implements PawnMoveGenerator {

    @Override
    public Set<LegalMove> generate(Game game, Pawn pawn, Position currentPosition) {
        Set<LegalMove> legalMoves = new HashSet<>();
        History history = game.getHistory();
        Board board = game.getBoard();
        if (history.hasPieceMovedBefore(pawn)) {
            log.debug("Rejecting move because the pawn was already moved");
            return legalMoves;
        }
        Player player = pawn.getPlayer();
        IndexChange secondStepIndexChange = getSecondStepIndexChange(player);
        PositionFactory positionFactory = board.getPositionFactory();
        Index currentPositionIndex = positionFactory.create(currentPosition);
        Optional<Position> optionalEndPosition = positionFactory.create(currentPositionIndex, secondStepIndexChange);
        if (optionalEndPosition.isEmpty()) {
            log.debug("Rejecting move because it end position is not valid on the board, index: {}, indexChange: {}", currentPositionIndex, secondStepIndexChange);
            return legalMoves;
        }
        Position endPosition = optionalEndPosition.get();
        if (board.hasPiece(endPosition)) {
            Piece targetPositionPiece = board.getPiece(endPosition);
            log.debug("Rejecting move because it end position: {} is occupied by piece: {}", endPosition, targetPositionPiece);
            return legalMoves;
        }
        IndexChange firstStepIndexChange = getFirstStepIndexChange(player);
        Optional<Position> optionalMiddlePosition = positionFactory.create(currentPositionIndex, firstStepIndexChange);
        Position middlePosition = optionalMiddlePosition.orElseThrow(); // we checked end position and its on board, the middle position has to be on board too
        if (board.hasPiece(middlePosition)) {
            Piece middlePositionPiece = board.getPiece(endPosition);
            log.debug("Rejecting move because position: {} is occupied by: {}", middlePosition, middlePositionPiece);
            return legalMoves;
        }
        legalMoves.add(new LegalMove(pawn, currentPosition, endPosition));
        return legalMoves;
    }

    private IndexChange getFirstStepIndexChange(Player player) {
        return switch (player) {
            case WHITE -> new IndexChange(1, 0);
            case BLACK -> new IndexChange(-1, 0);
        };
    }

    private IndexChange getSecondStepIndexChange(Player player) {
        return switch (player) {
            case WHITE -> new IndexChange(2, 0);
            case BLACK -> new IndexChange(-2, 0);
        };
    }
}
