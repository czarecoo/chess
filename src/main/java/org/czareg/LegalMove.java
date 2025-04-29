package org.czareg;

import org.czareg.position.Position;

record LegalMove(Piece piece, Position start, Position end) {

    LegalMove {
        if (start.equals(end)) {
            throw new IllegalArgumentException("Start and end positions cannot be equal: " + start);
        }
    }
}
