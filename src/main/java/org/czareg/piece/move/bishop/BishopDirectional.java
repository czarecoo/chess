package org.czareg.piece.move.bishop;

import org.czareg.piece.move.shared.Directional;
import org.czareg.position.IndexChange;

import java.util.stream.Stream;

public interface BishopDirectional extends Directional {

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
