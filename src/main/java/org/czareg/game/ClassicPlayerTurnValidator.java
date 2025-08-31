package org.czareg.game;

import org.czareg.piece.Player;

public class ClassicPlayerTurnValidator implements PlayerTurnValidator {

    @Override
    public void validate(Context context, Player requestingPlayer) {
        History history = context.getHistory();
        Player nowMovingPlayer = history.getCurrentPlayer();
        if (requestingPlayer != nowMovingPlayer) {
            throw new IllegalArgumentException("Now moving player %s not player %s".formatted(nowMovingPlayer, requestingPlayer));
        }
    }
}
