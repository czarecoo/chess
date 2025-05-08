package org.czareg.piece.move.rook;

import org.czareg.piece.move.Directional;
import org.czareg.piece.move.PieceMoveGenerator;
import org.czareg.position.IndexChange;

import java.util.stream.Stream;

public interface RookMoveGenerator extends PieceMoveGenerator, Directional {

    default Stream<IndexChange> getDirections() {
        return Stream.of(
                new IndexChange(-1, 0),
                new IndexChange(1, 0),
                new IndexChange(0, -1),
                new IndexChange(0, 1)
        );
    }
}
