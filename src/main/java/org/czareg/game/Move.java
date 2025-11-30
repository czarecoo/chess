package org.czareg.game;

import lombok.Value;
import org.czareg.piece.Piece;
import org.czareg.position.Position;

@Value
public class Move implements Duplicatable<Move> {

    Piece piece;
    Position start;
    Position end;
    Metadata metadata;

    public Move(Piece piece, Position start, Position end, Metadata metadata) {
        if (start.equals(end)) {
            throw new IllegalArgumentException("Start and end positions cannot be equal " + start);
        }
        this.piece = piece;
        this.end = end;
        this.start = start;
        this.metadata = metadata;
    }

    public boolean isPromotion() {
        return metadata.isExactly(Metadata.Key.MOVE_TYPE, MoveType.PROMOTION)
                || metadata.isExactly(Metadata.Key.MOVE_TYPE, MoveType.PROMOTION_CAPTURE);
    }

    @Override
    public Move duplicate() {
        return new Move(piece, start, end, metadata.duplicate());
    }
}
