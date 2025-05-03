package org.czareg.piece.move;

import org.czareg.game.Game;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.position.Position;

import java.util.Optional;
import java.util.stream.Stream;

public interface MoveGenerator {

    Stream<Move> generate(Game game, Position currentPosition);

    Optional<Move> generate(Game game, Position currentPosition, Position endPosition, MoveType moveType);
}
