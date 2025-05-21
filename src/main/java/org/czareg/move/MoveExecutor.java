package org.czareg.move;

import org.czareg.game.Context;
import org.czareg.game.Move;

public interface MoveExecutor {

    void execute(Context context, Move move);
}
