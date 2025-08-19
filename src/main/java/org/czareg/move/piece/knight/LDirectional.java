package org.czareg.move.piece.knight;

import org.czareg.move.piece.Directional;
import org.czareg.position.IndexChange;

import java.util.stream.Stream;

public interface LDirectional extends Directional {

    @Override
    default Stream<IndexChange> getDirections() {
        return Stream.of(
                new IndexChange(-1, -2),
                new IndexChange(1, -2),
                new IndexChange(-2, -1),
                new IndexChange(2, -1),
                new IndexChange(-2, 1),
                new IndexChange(2, 1),
                new IndexChange(-1, 2),
                new IndexChange(1, 2)
        );
    }
}
