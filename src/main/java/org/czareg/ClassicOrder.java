package org.czareg;

import static org.czareg.Player.BLACK;
import static org.czareg.Player.WHITE;

class ClassicOrder implements Order {

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
