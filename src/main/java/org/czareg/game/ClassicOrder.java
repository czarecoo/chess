package org.czareg.game;

import org.czareg.piece.Player;

import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;

public class ClassicOrder implements Order {

    @Override
    public Player getNowMovingPlayer(Player lastMovingPlayer) {
        return switch (lastMovingPlayer) {
            case WHITE -> BLACK;
            case BLACK -> WHITE;
        };
    }

    @Override
    public Player startingPlayer() {
        return WHITE;
    }
}
