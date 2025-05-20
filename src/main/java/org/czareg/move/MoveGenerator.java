package org.czareg.move;

import org.czareg.game.Game;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.move.piece.PieceMoveGeneratorFactory;
import org.czareg.piece.Player;
import org.czareg.position.Position;

import java.util.Optional;
import java.util.stream.Stream;

public interface MoveGenerator {

    PieceMoveGeneratorFactory getPieceMoveGeneratorFactory();

    Stream<Move> generate(Game game, Position currentPosition);

    Optional<Move> generate(Game game, Position currentPosition, Position endPosition, MoveType moveType);

    Stream<Move> generate(Game game, Player attacker);
}
