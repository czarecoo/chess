package org.czareg.game;

import lombok.extern.slf4j.Slf4j;
import org.czareg.move.MoveExecutor;
import org.czareg.move.MoveGenerator;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class ClassicMoveLegalityValidator implements MoveLegalityValidator {

    @Override
    public void validate(Context context, Move move) {
        MoveGenerator moveGenerator = context.getMoveGenerator();
        Set<Move> moves = moveGenerator.generate(context, move.getStart())
                .filter(generatedMove -> generatedMove.equals(move))
                .filter(isKingNotInCheckAfterMove(context))
                .collect(Collectors.toSet());
        if (moves.isEmpty()) {
            throw new IllegalArgumentException("%s does not match any generated legal moves".formatted(move));
        }
        if (moves.size() > 1) {
            throw new IllegalArgumentException("%s matches multiple generated legal moves %s".formatted(move, moves));
        }
    }

    private Predicate<Move> isKingNotInCheckAfterMove(Context context) {
        return move -> {
            Context duplicatedContext = context.duplicate();
            MoveExecutor executor = duplicatedContext.getMoveExecutor();
            executor.execute(duplicatedContext, move);
            ThreatAnalyzer threatAnalyzer = duplicatedContext.getThreatAnalyzer();
            return !threatAnalyzer.isInCheck(duplicatedContext, move.getPiece().getPlayer());
        };
    }
}
