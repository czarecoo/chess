package org.czareg.piece.move.pawn;

import org.czareg.game.Game;
import org.czareg.game.LegalMove;
import org.czareg.piece.Pawn;
import org.czareg.position.Position;

import java.util.Set;

public interface PawnMoveGenerator {

    Set<LegalMove> generate(Game game, Pawn pawn, Position currentPosition);
}
