package org.czareg.game;

import lombok.extern.slf4j.Slf4j;
import org.czareg.move.MoveGenerator;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ClassicMoveLegalityValidator implements MoveLegalityValidator {

    private final KingMoveValidator kingMoveValidator;

    public ClassicMoveLegalityValidator(KingMoveValidator kingMoveValidator) {
        this.kingMoveValidator = kingMoveValidator;
    }

    @Override
    public void validate(Context context, Move move) {
        MoveGenerator moveGenerator = context.getMoveGenerator();
        Set<Move> moves = moveGenerator.generate(context, move.getStart())
                .collect(Collectors.toSet());

        if (moves.isEmpty()) {
            throw new IllegalArgumentException("No legal moves were generated");
        }
        if (!moves.contains(move)) {
            throw new IllegalArgumentException("%s does not match any generated legal moves %s".formatted(move, moves));
        }
        kingMoveValidator.validate(context, move);
    }
}
