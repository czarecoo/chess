package org.czareg.move.piece.king;

import org.czareg.game.Context;
import org.czareg.game.Game;
import org.czareg.game.Move;
import org.czareg.move.MoveExecutor;
import org.czareg.piece.Player;
import org.czareg.position.Position;
import org.slf4j.Logger;

public interface KingMoveValidator {

    default boolean isInvalid(Context context, Logger log, Move move) {
        Game game = context.getGame();
        Context duplicatedContext = context.duplicate();
        MoveExecutor moveExecutor = context.getMoveExecutor();
        moveExecutor.execute(duplicatedContext, move);
        Position end = move.getEnd();
        Player player = move.getPiece().getPlayer();
        Player opponent = player.getOpponent();
        if (game.isUnderAttack(duplicatedContext, end, player, opponent)) {
            log.debug("Rejecting move because king end {} is under attack by {}.", end, opponent);
            return true;
        }
        return false;
    }
}
