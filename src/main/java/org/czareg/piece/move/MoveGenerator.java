package org.czareg.piece.move;

import org.czareg.game.Game;
import org.czareg.game.LegalMove;
import org.czareg.position.Position;

import java.util.Set;

public interface MoveGenerator {

    Set<LegalMove> generate(Game game, Position currentPosition);
}
