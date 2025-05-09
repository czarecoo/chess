package org.czareg.piece.move.shared;

import org.czareg.position.IndexChange;

import java.util.stream.Stream;

public interface Directional {

    Stream<IndexChange> getDirections();
}
