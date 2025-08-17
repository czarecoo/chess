package org.czareg.game;

import lombok.Value;
import org.czareg.piece.Player;
import org.czareg.position.Position;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Value
public class GeneratedMoves {

    Map<Player, Set<Move>> playerMoves;

    public Set<Move> getMoves(Player player) {
        return Optional.ofNullable(playerMoves.get(player)).orElseThrow();
    }

    public Set<Move> getMovesStarting(Position start) {
        return playerMoves.values()
                .stream()
                .flatMap(Set::stream)
                .filter(move -> move.getStart().equals(start))
                .collect(Collectors.toSet());
    }
}
