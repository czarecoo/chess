package org.czareg.move.piece;

import org.czareg.piece.Piece;

import java.util.List;

public interface PieceMoveGeneratorFactory {

    List<PieceMoveGenerator> getPieceMoveGenerators(Piece piece);
}
