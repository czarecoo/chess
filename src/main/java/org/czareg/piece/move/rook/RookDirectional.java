package org.czareg.piece.move.rook;

import org.czareg.piece.move.shared.Directional;
import org.czareg.position.IndexChange;

import java.util.stream.Stream;

public interface RookDirectional extends Directional {

    default Stream<IndexChange> getDirections() {
        return Stream.of(
                new IndexChange(-1, 0),
                new IndexChange(1, 0),
                new IndexChange(0, -1),
                new IndexChange(0, 1)
        );
    }
}
