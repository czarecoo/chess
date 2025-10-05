package org.czareg.game;

import lombok.Value;
import org.czareg.position.Position;

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
}
