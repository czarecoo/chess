package org.czareg;

import java.util.Set;

interface MoveGenerator {

    Set<LegalMove> generate(Game game, Board board, Piece movingPiece, ClassicPosition currentClassicPosition);
}
