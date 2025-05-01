package org.czareg;

import org.czareg.position.Position;

import java.util.Set;

interface PawnMoveGenerator {

    Set<LegalMove> generate(Game game, Pawn pawn, Position currentPosition);
}
