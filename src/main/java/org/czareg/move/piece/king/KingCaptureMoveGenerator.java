package org.czareg.move.piece.king;

import org.czareg.game.Context;
import org.czareg.game.Move;
import org.czareg.move.piece.shared.AllDirectional;
import org.czareg.move.piece.shared.JumpCaptureMoveGenerator;
import org.slf4j.Logger;

public class KingCaptureMoveGenerator extends JumpCaptureMoveGenerator implements AllDirectional, KingMoveValidator {

    @Override
    public boolean isInvalid(Context context, Logger log, Move move) {
        return KingMoveValidator.super.isInvalid(context, log, move);
    }
}
