package org.czareg.piece.move.rook;

import lombok.extern.slf4j.Slf4j;
import org.czareg.game.MoveType;
import org.czareg.piece.move.shared.AbstractMoveMoveGenerator;

@Slf4j
public class RookMoveMoveGenerator extends AbstractMoveMoveGenerator implements RookDirectional {

    @Override
    public MoveType getMoveType() {
        return MoveType.ROOK_MOVE;
    }
}
