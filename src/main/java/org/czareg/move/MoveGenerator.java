package org.czareg.move;

import org.czareg.game.Context;
import org.czareg.game.Move;
import org.czareg.piece.Player;
import org.czareg.position.Position;

import java.util.stream.Stream;

public interface MoveGenerator {

    Stream<Move> generate(Context context, Position currentPosition);

    Stream<Move> generate(Context context, Player attacker);
}
