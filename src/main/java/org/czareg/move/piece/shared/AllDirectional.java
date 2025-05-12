package org.czareg.move.piece.shared;

import org.czareg.move.piece.Directional;
import org.czareg.position.IndexChange;

import java.util.stream.Stream;

public interface AllDirectional extends Directional {

    @Override
    default Stream<IndexChange> getDirections() {
        return Stream.of(
                new IndexChange(-1, 0),
                new IndexChange(1, 0),
                new IndexChange(0, -1),
                new IndexChange(0, 1),
                new IndexChange(-1, -1),
                new IndexChange(-1, 1),
                new IndexChange(1, -1),
                new IndexChange(1, 1)
        );
    }
}
