package org.czareg.move;

import org.czareg.game.Context;
import org.czareg.game.GeneratedMoves;

public interface MoveGenerators {

    GeneratedMoves getOrGenerateLegal(Context context);

    GeneratedMoves getOrGeneratePseudoLegal(Context context);
}
