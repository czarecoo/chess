package org.czareg.move.piece.king;

import org.czareg.move.piece.shared.AllDirectional;
import org.czareg.move.piece.shared.JumpMoveMoveGenerator;

public class KingMoveMoveGenerator extends JumpMoveMoveGenerator implements AllDirectional {
    // no validation for moves that would put king in check, this is handled by KingMoveValidator
    // the reason is during move generation we cannot check for isUnderAttack because that requires move generation
}
