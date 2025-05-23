package org.czareg.game;

import org.czareg.piece.Player;
import org.czareg.position.Position;

public interface Game {

    void makeMove(Context context, Move move);

    boolean isUnderAttack(Context context, Position position, Player defender, Player attacker);

    boolean isInCheck(Context context, Player player);
}
