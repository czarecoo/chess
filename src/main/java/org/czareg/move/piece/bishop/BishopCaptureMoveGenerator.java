package org.czareg.move.piece.bishop;

import lombok.extern.slf4j.Slf4j;
import org.czareg.game.MoveType;
import org.czareg.move.piece.shared.CapturePieceMoveGenerator;

@Slf4j
public class BishopCaptureMoveGenerator extends CapturePieceMoveGenerator implements BishopDirectional {

    @Override
    public MoveType getMoveType() {
        return MoveType.BISHOP_CAPTURE;
    }
}
