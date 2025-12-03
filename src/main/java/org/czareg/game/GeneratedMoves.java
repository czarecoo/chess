package org.czareg.game;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.czareg.position.Position;
import org.czareg.util.RandomUtils;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class GeneratedMoves {

    private final Set<Move> moves;

    public boolean contains(Move move) {
        return moves.contains(move);
    }

    public boolean isEmpty() {
        return moves.isEmpty();
    }

    public int size() {
        return moves.size();
    }

    public Stream<Move> stream() {
        return moves.stream();
    }

    public Set<Move> getMovesStarting(Position start) {
        return stream()
                .filter(move -> move.getStart().equals(start))
                .collect(Collectors.toSet());
    }

    public Optional<Move> findRandom() {
        if (isEmpty()) {
            return Optional.empty();
        }
        int index = RandomUtils.betweenZeroInclusiveAndEndExclusive(size());
        return Optional.of(stream().toList().get(index));
    }
}
