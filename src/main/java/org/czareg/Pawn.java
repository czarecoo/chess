package org.czareg;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
final class Pawn implements Piece {

    private final Player player;

    @Override
    public String toString() {
        return "%s %s".formatted(player, this.getClass().getSimpleName());
    }
}
