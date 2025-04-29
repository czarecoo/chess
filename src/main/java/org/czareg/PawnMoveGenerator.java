package org.czareg;

import org.czareg.position.Position;

import java.util.Set;

sealed interface PawnMoveGenerator permits PawnForwardMoveGenerator, PawnDoubleForwardMoveGenerator {

    Set<LegalMove> generate(Game game, Pawn pawn, Position currentPosition);
}
