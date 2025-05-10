package org.czareg.move.piece.bishop;

import lombok.extern.slf4j.Slf4j;
import org.czareg.game.MoveType;
import org.czareg.move.piece.shared.MovePieceMoveGenerator;

@Slf4j
public class BishopMoveMoveGenerator extends MovePieceMoveGenerator implements BishopDirectional {

    @Override
    public MoveType getMoveType() {
        return MoveType.BISHOP_MOVE;
    }
}
