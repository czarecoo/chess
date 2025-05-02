package org.czareg.piece.move.pawn;

import org.czareg.game.Game;
import org.czareg.game.Move;
import org.czareg.piece.Pawn;
import org.czareg.position.Position;

import java.util.Set;

public interface PawnMoveGenerator {

    Set<Move> generate(Game game, Pawn pawn, Position currentPosition);
}
