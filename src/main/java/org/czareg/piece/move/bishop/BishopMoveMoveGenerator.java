package org.czareg.piece.move.bishop;

import lombok.extern.slf4j.Slf4j;
import org.czareg.game.MoveType;
import org.czareg.piece.move.shared.AbstractMoveMoveGenerator;

@Slf4j
public class BishopMoveMoveGenerator extends AbstractMoveMoveGenerator implements BishopDirectional {

    @Override
    public MoveType getMoveType() {
        return MoveType.BISHOP_MOVE;
    }
}
