package org.czareg.piece.move;

import org.czareg.piece.Piece;

import java.util.stream.Stream;

public interface PieceMoveGeneratorFactory {

    Stream<PieceMoveGenerator> getMoveGenerators(Piece piece);
}
