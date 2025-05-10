package org.czareg.move.piece;

import org.czareg.game.Game;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.piece.Piece;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;

import java.util.Optional;
import java.util.stream.Stream;

public interface PieceMoveGenerator {

    Stream<Move> generate(Game game, Piece piece, Position currentPosition);

    Optional<Move> generate(Game game, Piece piece, Position currentPosition, IndexChange endPositionIndexChange);

    MoveType getMoveType();
}
