package org.czareg.move.piece.bishop;

import org.czareg.move.piece.Directional;
import org.czareg.position.IndexChange;

import java.util.stream.Stream;

public interface DiagonalDirectional extends Directional {

    @Override
    default Stream<IndexChange> getDirections() {
        return Stream.of(
                new IndexChange(-1, -1),
                new IndexChange(-1, 1),
                new IndexChange(1, -1),
                new IndexChange(1, 1)
        );
    }
}
