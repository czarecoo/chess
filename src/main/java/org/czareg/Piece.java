package org.czareg;

import java.util.Set;

interface Piece {

    Player getPlayer();

    Set<Move> getPotentialMoves(Position currentPosition);
}
