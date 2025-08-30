package org.czareg.move;

import org.czareg.game.Context;
import org.czareg.game.GeneratedMoves;

public interface MoveGenerators {

    GeneratedMoves generateLegal(Context context);

    GeneratedMoves generatePseudoLegal(Context context);
}
