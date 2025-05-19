package org.czareg.game;

import org.czareg.piece.Player;

import static org.czareg.piece.Player.WHITE;

public class ClassicOrder implements Order {

    @Override
    public Player getNowMovingPlayer(Player lastMovingPlayer) {
        return lastMovingPlayer.getOpponent();
    }

    @Override
    public Player startingPlayer() {
        return WHITE;
    }
}
