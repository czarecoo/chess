package org.czareg.move.piece.king;

import org.czareg.game.Context;
import org.czareg.game.Move;
import org.czareg.move.piece.shared.AllDirectional;
import org.czareg.move.piece.shared.JumpMoveMoveGenerator;
import org.slf4j.Logger;

public class KingMoveMoveGenerator extends JumpMoveMoveGenerator implements AllDirectional, KingMoveValidator {

    @Override
    public boolean isInvalid(Context context, Logger log, Move move) {
        return KingMoveValidator.super.isInvalid(context, log, move);
    }
}
