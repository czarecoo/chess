package org.czareg.piece.move.pawn;

import org.czareg.game.Game;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.piece.Pawn;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;

import java.util.Optional;
import java.util.stream.Stream;

public interface PawnMoveGenerator {

    Stream<Move> generate(Game game, Pawn pawn, Position currentPosition);

    Optional<Move> generate(Game game, Pawn pawn, Position currentPosition, IndexChange indexChange);

    MoveType getMoveType();
}
