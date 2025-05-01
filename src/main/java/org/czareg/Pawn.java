package org.czareg;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
final class Pawn implements Piece {

    private final Player player;
}
