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
class PawnForwardMoveGenerator implements PawnMoveGenerator {

    @Override
    public Set<LegalMove> generate(Game game, Pawn pawn, Position currentPosition) {
        log.debug("Generating moves for {} at {}.", pawn, currentPosition);
        Set<LegalMove> legalMoves = new HashSet<>();
        Board board = game.getBoard();
        Player player = pawn.getPlayer();
        IndexChange endPositionIndexChange = getEndPositionIndexChange(player);
        PositionFactory positionFactory = board.getPositionFactory();
        Index currentPositionIndex = positionFactory.create(currentPosition);
        Optional<Position> optionalEndPosition = positionFactory.create(currentPositionIndex, endPositionIndexChange);
        if (optionalEndPosition.isEmpty()) {
            log.debug("Rejecting move because end position is not valid on the board ({}, {}).", currentPositionIndex, endPositionIndexChange);
            return legalMoves;
        }
        Position endPosition = optionalEndPosition.get();
        if (board.hasPiece(endPosition)) {
            Piece endPositionOccupyingPiece = board.getPiece(endPosition);
            log.debug("Rejecting move because end {} is occupied by {}.", endPosition, endPositionOccupyingPiece);
            return legalMoves;
        }
        LegalMove legalMove = new LegalMove(pawn, currentPosition, endPosition);
        legalMoves.add(legalMove);
        log.debug("Accepted move: {}.", legalMove);
        return legalMoves;
    }

    private IndexChange getEndPositionIndexChange(Player player) {
        return switch (player) {
            case WHITE -> new IndexChange(1, 0);
            case BLACK -> new IndexChange(-1, 0);
        };
    }
}
