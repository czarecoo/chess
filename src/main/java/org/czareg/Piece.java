package org.czareg;

sealed interface Piece permits Pawn {

    Player getPlayer();
}
