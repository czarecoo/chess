package org.czareg.move;

import org.czareg.game.Context;
import org.czareg.game.Move;

public interface LegalMoveFilter {

    boolean isLegal(Context context, Move move);
}
