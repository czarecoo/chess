package org.czareg.game;

import lombok.Value;
import org.czareg.position.Position;
import org.czareg.util.RandomUtils;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Value
public class GeneratedMoves {

    Set<Move> moves;

    public Set<Move> getMovesStarting(Position start) {
        return moves
                .stream()
                .filter(move -> move.getStart().equals(start))
                .collect(Collectors.toSet());
    }

    public Optional<Move> findRandom() {
        if (moves.isEmpty()) {
            return Optional.empty();
        }
        int index = RandomUtils.betweenZeroInclusiveAndEndExclusive(moves.size());
        return Optional.of(moves.stream().toList().get(index));
    }
}
