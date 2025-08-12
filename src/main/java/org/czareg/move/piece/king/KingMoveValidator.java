package org.czareg.move.piece.king;

import org.czareg.game.Context;
import org.czareg.game.Move;
import org.czareg.game.ThreatAnalyzer;
import org.czareg.move.MoveExecutor;
import org.czareg.piece.Player;
import org.czareg.position.Position;
import org.slf4j.Logger;

@Deprecated // TODO is this needed?
public interface KingMoveValidator {

    default boolean isInvalid(Context context, Logger log, Move move) {
        Context duplicatedContext = context.duplicate();
        MoveExecutor moveExecutor = duplicatedContext.getMoveExecutor();
        moveExecutor.execute(duplicatedContext, move);
        Position end = move.getEnd();
        Player player = move.getPiece().getPlayer();
        Player opponent = player.getOpponent();
        ThreatAnalyzer threatAnalyzer = duplicatedContext.getThreatAnalyzer();
        if (threatAnalyzer.isUnderAttack(duplicatedContext, end, player, opponent)) {
            log.debug("Rejecting move because king end {} is under attack by {}.", end, opponent);
            return true;
        }
        return false;
    }
}
