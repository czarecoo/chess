package org.czareg.piece.move;

import org.czareg.game.Game;
import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.game.SpecialMoveType;

public interface SpecialMoveExecutor {

    default boolean isSpecialMove(Move move) {
        Metadata metadata = move.getMetadata();
        return metadata.containsKey(Metadata.Key.SPECIAL_MOVE_TYPE);
    }

    default SpecialMoveType getSpecialMoveType(Move move) {
        Metadata metadata = move.getMetadata();
        return metadata.get(Metadata.Key.SPECIAL_MOVE_TYPE, SpecialMoveType.class).orElseThrow();
    }

    void execute(Move move, Game game);
}
