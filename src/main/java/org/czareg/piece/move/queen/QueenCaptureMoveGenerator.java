package org.czareg.piece.move.queen;

import lombok.extern.slf4j.Slf4j;
import org.czareg.game.MoveType;
import org.czareg.piece.move.shared.AbstractCaptureMoveGenerator;

@Slf4j
public class QueenCaptureMoveGenerator extends AbstractCaptureMoveGenerator implements QueenDirectional {

    @Override
    public MoveType getMoveType() {
        return MoveType.QUEEN_CAPTURE;
    }
}
