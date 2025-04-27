package org.czareg;

import lombok.Value;

@Value
class PawnCaptureRightMove implements Move {

    Player player;

    @Override
    public IndexChange getIndexChange() {
        return switch (player){
            case WHITE -> new IndexChange(1,1);
            case BLACK -> new IndexChange(-1,1);
        };
    }
}
