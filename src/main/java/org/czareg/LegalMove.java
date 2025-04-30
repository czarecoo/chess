package org.czareg;

import lombok.Value;
import org.czareg.position.Position;

@Value
class LegalMove {

    Piece piece;
    Position start;
    Position end;

    LegalMove(Piece piece, Position start, Position end) {
        if (start.equals(end)) {
            throw new IllegalArgumentException("Start and end positions cannot be equal: " + start);
        }
        this.piece = piece;
        this.end = end;
        this.start = start;
    }
}
