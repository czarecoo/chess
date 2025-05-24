package org.czareg.game;

import org.czareg.piece.Player;

import static org.czareg.piece.Player.WHITE;

public class ClassicOrder implements Order {

    @Override
    public Player getNowMovingPlayer(Player lastMovingPlayer) {
        return lastMovingPlayer.getOpponent();
    }

    @Override
    public Player startingPlayer() {
        return WHITE;
    }

    @Override
    public void checkIfPlayersTurn(Context context, Move move) {
        History history = context.getHistory();
        Order order = context.getOrder();
        Player nowMovingPlayer = history.getLastMovingPlayer()
                .map(order::getNowMovingPlayer)
                .orElse(order.startingPlayer());
        Player requestingPlayer = move.getPiece().getPlayer();
        if (requestingPlayer != nowMovingPlayer) {
            throw new IllegalArgumentException("Now moving player %s not player %s".formatted(nowMovingPlayer, requestingPlayer));
        }
    }
}
