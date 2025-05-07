package org.czareg.piece;

public sealed interface Piece permits Pawn, Queen, Rook {

    Player getPlayer();
}
