package org.czareg.move.piece;

import org.czareg.position.IndexChange;

import java.util.stream.Stream;

public interface Directional {

    Stream<IndexChange> getDirections();
}
