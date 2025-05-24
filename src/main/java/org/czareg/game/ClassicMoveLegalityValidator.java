package org.czareg.game;

import lombok.extern.slf4j.Slf4j;
import org.czareg.move.MoveExecutor;
import org.czareg.move.MoveGenerator;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class ClassicMoveLegalityValidator implements MoveLegalityValidator {

    @Override
    public void validate(Context context, Move move) {
        MoveGenerator moveGenerator = context.getMoveGenerator();
        Set<Move> moves = moveGenerator.generate(context, move.getStart())
                .filter(isKingNotInCheckAfterMove(context))
                .collect(Collectors.toSet());
        log.debug("Generated {} legal moves.", moves.size());
        boolean noMatchingGeneratedMoveFound = moves.stream()
                .filter(generatedMove -> Objects.equals(move.getStart(), generatedMove.getStart()))
                .filter(generatedMove -> Objects.equals(move.getEnd(), generatedMove.getEnd()))
                .filter(generatedMove -> move.getPiece() == generatedMove.getPiece())
                .filter(generatedMove -> move.getMetadata().equals(generatedMove.getMetadata()))
                .findAny()
                .isEmpty();
        if (noMatchingGeneratedMoveFound) {
            throw new IllegalArgumentException("%s is not one of the generated legal moves %s".formatted(move, moves));
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
