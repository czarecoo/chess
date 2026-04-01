package org.czareg.move.piece.pawn;

import org.czareg.piece.Player;
import org.czareg.position.Position;

public interface StartingRankChecker {

    default boolean isOnStartingRank(Position position, Player player) {
        return position.getRank() == getStartingRank(player);
    }

    private int getStartingRank(Player player) {
        return switch (player) {
            case WHITE -> 2;
            case BLACK -> 7;
        };
    }
}
