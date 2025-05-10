package org.czareg.move.piece.rook;

import lombok.extern.slf4j.Slf4j;
import org.czareg.game.MoveType;
import org.czareg.move.piece.shared.CapturePieceMoveGenerator;

@Slf4j
public class RookCaptureMoveGenerator extends CapturePieceMoveGenerator implements RookDirectional {

    @Override
    public MoveType getMoveType() {
        return MoveType.ROOK_CAPTURE;
    }
}
