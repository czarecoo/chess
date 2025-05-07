package org.czareg.piece;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public final class Rook implements Piece {

    private final Player player;
}
