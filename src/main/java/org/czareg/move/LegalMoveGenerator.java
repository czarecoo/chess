package org.czareg.move;

import lombok.RequiredArgsConstructor;
import org.czareg.game.Context;
import org.czareg.game.GeneratedMoves;
import org.czareg.game.Move;
import org.czareg.piece.Player;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LegalMoveGenerator {

    private final PseudoLegalMoveGenerator pseudoLegalMoveGenerator;
    private final OnlyValidKingMoveFilter onlyValidKingMoveFilter;

    public GeneratedMoves generate(Context context) {
        GeneratedMoves generatedMoves = pseudoLegalMoveGenerator.generate(context);
        Map<Player, Set<Move>> result = generatedMoves.getPlayerMoves();
        Map<Player, Set<Move>> playerMoves = generatedMoves.getPlayerMoves();
        for (Map.Entry<Player, Set<Move>> playerSetEntry : playerMoves.entrySet()) {
            Player player = playerSetEntry.getKey();
            Set<Move> moves = playerSetEntry.getValue()
                    .stream()
                    .filter(move -> onlyValidKingMoveFilter.filter(context, move))
                    .collect(Collectors.toSet());
            result.put(player, moves);
        }
        return new GeneratedMoves(result);
    }
}
