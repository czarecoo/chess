package org.czareg.game;

import org.czareg.move.MoveGenerators;
import org.czareg.piece.Player;

public class ClassicMoveLegalityValidator implements MoveLegalityValidator {

    @Override
    public void validate(Context context, Move move) {
        History history = context.getHistory();
        Player nowMovingPlayer = history.getCurrentPlayer();
        Player requestingPlayer = move.getPlayer();
        if (requestingPlayer != nowMovingPlayer) {
            throw new IllegalArgumentException("Now moving player %s not player %s".formatted(nowMovingPlayer, requestingPlayer));
        }
        MoveGenerators moveGenerators = context.getMoveGenerators();
        GeneratedMoves generatedMoves = moveGenerators.getOrGenerateLegal(context);
        if (!generatedMoves.contains(move)) {
            throw new IllegalArgumentException("Move is not legal %s".formatted(move));
        }
    }
}
