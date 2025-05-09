package org.czareg.piece.move.queen;

import lombok.extern.slf4j.Slf4j;
import org.czareg.game.MoveType;
import org.czareg.piece.move.shared.AbstractMoveMoveGenerator;

@Slf4j
public class QueenMoveMoveGenerator extends AbstractMoveMoveGenerator implements QueenDirectional {

    @Override
    public MoveType getMoveType() {
        return MoveType.QUEEN_MOVE;
    }
}
