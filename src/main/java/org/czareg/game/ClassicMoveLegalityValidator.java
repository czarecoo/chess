package org.czareg.game;

import org.czareg.move.MoveGenerators;

import java.util.Set;

public class ClassicMoveLegalityValidator implements MoveLegalityValidator {

    @Override
    public void validate(Context context, Move move) {
        MoveGenerators moveGenerators = context.getMoveGenerators();
        Set<Move> moves = moveGenerators.generateLegal(context)
                .getMoves();
        if (!moves.contains(move)) {
            throw new IllegalArgumentException("Move is not legal %s".formatted(move));
        }
    }
}
