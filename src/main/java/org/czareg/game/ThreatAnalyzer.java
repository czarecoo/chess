package org.czareg.game;

import org.czareg.piece.Player;
import org.czareg.position.Position;

public interface ThreatAnalyzer {

    boolean isUnderAttack(Context context, Position position, Player player);

    boolean isInCheck(Context context, Player player);

    default boolean isNotInCheck(Context context, Player player) {
        return !isInCheck(context, player);
    }
}
