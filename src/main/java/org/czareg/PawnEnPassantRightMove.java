package org.czareg;

import lombok.Value;
import org.czareg.position.IndexChange;

@Value
class PawnEnPassantRightMove implements Move {

    Player player;

    @Override
    public IndexChange getIndexChange() {
        return switch (player){
            case WHITE -> new IndexChange(1,1);
            case BLACK -> new IndexChange(-1,1);
        };
    }
}
