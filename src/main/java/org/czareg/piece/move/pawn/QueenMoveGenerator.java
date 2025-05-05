package org.czareg.piece.move.pawn;

import org.czareg.game.Game;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.piece.Queen;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;

import java.util.Optional;
import java.util.stream.Stream;

public interface QueenMoveGenerator {

    Stream<Move> generate(Game game, Queen queen, Position currentPosition);

    Optional<Move> generate(Game game, Queen queen, Position currentPosition, IndexChange endPositionIndexChange);

    MoveType getMoveType();

    //TODO move directions here?
}
