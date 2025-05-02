package org.czareg.piece.move;

import org.czareg.game.Game;
import org.czareg.game.Move;
import org.czareg.position.Position;

import java.util.Set;

public interface MoveGenerator {

    Set<Move> generate(Game game, Position currentPosition);
}
