package org.czareg.piece.move.queen;

import org.czareg.game.Game;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.piece.Queen;
import org.czareg.piece.move.pawn.QueenMoveGenerator;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;

import java.util.Optional;
import java.util.stream.Stream;

public class QueenMoveMoveGenerator implements QueenMoveGenerator {

    @Override
    public Stream<Move> generate(Game game, Queen queen, Position currentPosition) {
        return Stream.empty();
    }

    @Override
    public Optional<Move> generate(Game game, Queen queen, Position currentPosition, IndexChange endPositionIndexChange) {
        return Optional.empty();
    }

    @Override
    public MoveType getMoveType() {
        return MoveType.QUEEN_MOVE;
    }
}
