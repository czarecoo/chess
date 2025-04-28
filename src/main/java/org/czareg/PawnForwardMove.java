package org.czareg;

import lombok.Value;
import org.czareg.position.IndexChange;

@Value
class PawnForwardMove implements Move {

    Player player;

    PawnForwardMove(Player player) {
        this.player = player;
    }

    @Override
    public IndexChange getIndexChange() {
        return switch (player){
            case WHITE -> new IndexChange(1,0);
            case BLACK -> new IndexChange(-1,0);
        };
    }
}
