package org.czareg.move.piece.shared;

import org.czareg.piece.Player;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

public interface StartingRankChecker {

    default boolean isOnStartingRank(Position position, PositionFactory positionFactory, Player player) {
        int rank = position.getRank();
        return switch (player) {
            case WHITE -> rank == positionFactory.getMinRank();
            case BLACK -> rank == positionFactory.getMaxRank();
        };
    }
}
