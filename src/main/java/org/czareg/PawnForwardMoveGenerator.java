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
final class PawnForwardMoveGenerator implements PawnMoveGenerator {

    @Override
    public Set<LegalMove> generate(Game game, Pawn pawn, Position currentPosition) {
        Set<LegalMove> legalMoves = new HashSet<>();
        Board board = game.getBoard();
        Player player = pawn.getPlayer();
        IndexChange indexChange = getIndexChange(player);
        PositionFactory positionFactory = board.getPositionFactory();
        Index currentPositionIndex = positionFactory.create(currentPosition);
        Optional<Position> optionalEndPosition = positionFactory.create(currentPositionIndex, indexChange);
        if (optionalEndPosition.isEmpty()) {
            log.debug("Rejecting move because it end position is not valid on the board, index: {}, indexChange: {}", currentPositionIndex, indexChange);
            return legalMoves;
        }
        Position endPosition = optionalEndPosition.get();
        if (board.hasPiece(endPosition)) {
            Piece targetPositionPiece = board.getPiece(endPosition);
            log.debug("Rejecting move because it end position: {} is occupied by piece: {}", endPosition, targetPositionPiece);
            return legalMoves;
        }
        legalMoves.add(new LegalMove(pawn, currentPosition, endPosition));
        return legalMoves;
    }

    private IndexChange getIndexChange(Player player) {
        return switch (player) {
            case WHITE -> new IndexChange(1, 0);
            case BLACK -> new IndexChange(-1, 0);
        };
    }
}
