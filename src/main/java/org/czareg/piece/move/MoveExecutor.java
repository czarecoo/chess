package org.czareg.piece.move;

import org.czareg.game.Game;
import org.czareg.game.Move;

public interface MoveExecutor {

    void execute(Move move, Game game);
}
