package org.czareg.game;

import org.czareg.piece.Player;
import org.czareg.position.Position;

public interface ThreatAnalyzer {

    boolean isInCheck(Context context, Player player);

    boolean isUnderAttack(Context context, Position position, Player player);

    boolean isUnderAttack(GeneratedMoves generatedMoves, Position position, Player player);
}
