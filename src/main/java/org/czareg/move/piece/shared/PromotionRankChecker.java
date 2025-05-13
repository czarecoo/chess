package org.czareg.move.piece.shared;

import org.czareg.piece.Player;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

public interface PromotionRankChecker {

    default boolean isOnPromotionRank(Position position, PositionFactory positionFactory, Player player) {
        int rank = position.getRank();
        return switch (player) {
            case WHITE -> rank == positionFactory.getMaxRank();
            case BLACK -> rank == positionFactory.getMinRank();
        };
    }
}
