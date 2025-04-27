package org.czareg;

import lombok.Value;

@Value
class PawnPromotionMove implements Move {

    Player player;

    @Override
    public IndexChange getIndexChange() {
        return switch (player){
            case WHITE -> new IndexChange(1,0);
            case BLACK -> new IndexChange(-1,0);
        };
    }
}
