package org.czareg;

import lombok.Getter;
import lombok.Value;

@Value
class PawnForwardMove implements Move {

    @Getter
    IndexPosition indexPosition;

    public PawnForwardMove(IndexPosition indexPosition) {
        this.indexPosition = indexPosition;
    }

    @Override
    public boolean canCapture() {
        return false;
    }

    @Override
    public boolean canJump() {
        return false;
    }
}
