package org.czareg.piece.move.bishop;

import lombok.extern.slf4j.Slf4j;
import org.czareg.game.MoveType;
import org.czareg.piece.move.shared.AbstractCaptureMoveGenerator;

@Slf4j
public class BishopCaptureMoveGenerator extends AbstractCaptureMoveGenerator implements BishopDirectional {

    @Override
    public MoveType getMoveType() {
        return MoveType.BISHOP_CAPTURE;
    }
}
