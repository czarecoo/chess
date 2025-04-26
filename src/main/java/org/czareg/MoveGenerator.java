package org.czareg;

import java.util.Set;

interface MoveGenerator {

    Set<LegalMove> generate(Board board, Piece movingPiece, Position currentPosition);
}
