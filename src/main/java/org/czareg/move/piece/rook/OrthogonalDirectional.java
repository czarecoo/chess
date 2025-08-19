package org.czareg.move.piece.rook;

import org.czareg.move.piece.Directional;
import org.czareg.position.IndexChange;

import java.util.stream.Stream;

public interface OrthogonalDirectional extends Directional {

    @Override
    default Stream<IndexChange> getDirections() {
        return Stream.of(
                new IndexChange(0, -1),
                new IndexChange(0, 1),
                new IndexChange(-1, 0),
                new IndexChange(1, 0)
        );
    }
}
