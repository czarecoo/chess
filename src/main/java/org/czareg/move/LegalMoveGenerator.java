package org.czareg.move;

import lombok.RequiredArgsConstructor;
import org.czareg.game.Context;
import org.czareg.game.GeneratedMoves;
import org.czareg.game.Move;
import org.czareg.piece.Player;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LegalMoveGenerator {

    private final PseudoLegalMoveGenerator pseudoLegalMoveGenerator;
    private final OnlyValidKingMoveFilter onlyValidKingMoveFilter;

    public GeneratedMoves generate(Context context) {
        GeneratedMoves generatedMoves = pseudoLegalMoveGenerator.generate(context);
        Map<Player, Set<Move>> playerMoves = generatedMoves.getPlayerMoves();
        Map<Player, Set<Move>> result = new EnumMap<>(Player.class);
        for (Map.Entry<Player, Set<Move>> playerSetEntry : playerMoves.entrySet()) {
            Player player = playerSetEntry.getKey();
            Set<Move> moves = filterMoves(context, playerSetEntry);
            result.put(player, moves);
        }
        return new GeneratedMoves(result);
    }

    private Set<Move> filterMoves(Context context, Map.Entry<Player, Set<Move>> playerSetEntry) {
        return playerSetEntry.getValue()
                .stream()
                .filter(move -> onlyValidKingMoveFilter.filter(context, move))
                .collect(Collectors.toSet());
    }
}
