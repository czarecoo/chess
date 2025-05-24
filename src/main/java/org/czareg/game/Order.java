package org.czareg.game;

import org.czareg.piece.Player;

public interface Order {

    Player getNowMovingPlayer(Player lastMovingPlayer);

    Player startingPlayer();

    void checkIfPlayersTurn(Context context, Move move);
}
