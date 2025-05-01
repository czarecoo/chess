package org.czareg.piece;

public sealed interface Piece permits Pawn {

    Player getPlayer();
}
