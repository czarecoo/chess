package org.czareg.piece;

public sealed interface Piece permits Pawn, Knight, Bishop, Rook, Queen, King {

    Player getPlayer();
}
