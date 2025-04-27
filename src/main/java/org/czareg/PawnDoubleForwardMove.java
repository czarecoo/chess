package org.czareg;

import lombok.Value;

@Value
class PawnDoubleForwardMove implements Move {

    Player player;

    @Override
    public IndexChange getIndexChange() {
        return switch (player){
            case WHITE -> new IndexChange(2,0);
            case BLACK -> new IndexChange(-2,0);
        };
    }
}
