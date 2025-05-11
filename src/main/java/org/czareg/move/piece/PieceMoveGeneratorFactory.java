package org.czareg.move.piece;

import org.czareg.game.MoveType;
import org.czareg.piece.Piece;

import java.util.stream.Stream;

public interface PieceMoveGeneratorFactory {

    Stream<PieceMoveGenerator> getMoveGenerator(Piece piece);

    PieceMoveGenerator getMoveGenerator(MoveType moveType);
}
