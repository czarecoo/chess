package org.czareg;

import java.util.Set;

sealed interface Piece permits Pawn {

    Player getPlayer();

    Set<Move> getPotentialMoves();
}
