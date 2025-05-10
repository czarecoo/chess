package org.czareg.move.piece.queen;

import lombok.extern.slf4j.Slf4j;
import org.czareg.game.MoveType;
import org.czareg.move.piece.shared.CapturePieceMoveGenerator;

@Slf4j
public class QueenCaptureMoveGenerator extends CapturePieceMoveGenerator implements QueenDirectional {

    @Override
    public MoveType getMoveType() {
        return MoveType.QUEEN_CAPTURE;
    }
}
