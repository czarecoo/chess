package org.czareg.move;

import org.czareg.game.Context;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.piece.Player;
import org.czareg.position.Position;

import java.util.Optional;
import java.util.stream.Stream;

public interface MoveGenerator {

    Stream<Move> generate(Context context, Position currentPosition);

    Optional<Move> generate(Context context, Position currentPosition, Position endPosition, MoveType moveType);

    Stream<Move> generate(Context context, Player attacker);
}
