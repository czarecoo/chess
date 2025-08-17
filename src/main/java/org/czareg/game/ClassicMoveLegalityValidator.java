package org.czareg.game;

import org.czareg.move.MoveGenerator;
import org.czareg.piece.Player;

import java.util.Set;

public class ClassicMoveLegalityValidator implements MoveLegalityValidator {

    @Override
    public void validate(Context context, Move move) {
        Player player = move.getPiece().getPlayer();
        MoveGenerator moveGenerator = context.getMoveGenerator();
        Set<Move> moves = moveGenerator.generateLegal(context)
                .getMoves(player);
        if (!moves.contains(move)) {
            throw new IllegalArgumentException("Move is not legal %s".formatted(move));
        }
    }
}
