package org.czareg.game;

import org.czareg.piece.Player;

import static org.czareg.piece.Player.WHITE;

public class ClassicPlayerTurnValidator implements PlayerTurnValidator {

    @Override
    public void validate(Context context, Player requestingPlayer) {
        History history = context.getHistory();
        Player nowMovingPlayer = history.getLastMovingPlayer()
                .map(this::getNowMovingPlayer)
                .orElse(this.startingPlayer());
        if (requestingPlayer != nowMovingPlayer) {
            throw new IllegalArgumentException("Now moving player %s not player %s".formatted(nowMovingPlayer, requestingPlayer));
        }
    }

    public Player getNowMovingPlayer(Player lastMovingPlayer) {
        return lastMovingPlayer.getOpponent();
    }

    public Player startingPlayer() {
        return WHITE;
    }
}
