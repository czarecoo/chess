package org.czareg.piece.move.knight;

import org.czareg.piece.move.shared.Directional;
import org.czareg.position.IndexChange;

import java.util.stream.Stream;

public interface KnightDirectional extends Directional {

    @Override
    default Stream<IndexChange> getDirections() {
        return Stream.of(
                new IndexChange(-2, -1),
                new IndexChange(-2, 1),
                new IndexChange(-1, -2),
                new IndexChange(-1, 2),
                new IndexChange(1, -2),
                new IndexChange(1, 2),
                new IndexChange(2, -1),
                new IndexChange(2, 1)
        );
    }
}
