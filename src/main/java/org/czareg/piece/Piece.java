package org.czareg.piece;

public sealed interface Piece permits Pawn, Bishop, Rook, Queen {

    Player getPlayer();
}
