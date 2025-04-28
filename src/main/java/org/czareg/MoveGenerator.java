package org.czareg;

import org.czareg.position.Position;

import java.util.Set;

interface MoveGenerator {

    Set<LegalMove> generate(Game game, Board board, Piece movingPiece, Position currentPosition);
}
