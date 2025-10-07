package org.czareg.game;

import org.czareg.piece.Player;
import org.czareg.position.Position;

public interface ThreatAnalyzer {

    boolean isKingUnderAttack(Context context, Player player);

    boolean isPositionUnderAttack(Context context, Position square, Player owner);
}
