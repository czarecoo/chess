package org.czareg.move;

import org.czareg.game.Context;
import org.czareg.game.Move;

public interface OnlyValidKingMoveFilter {

    boolean filter(Context context, Move move);
}
