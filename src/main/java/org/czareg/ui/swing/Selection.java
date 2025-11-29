package org.czareg.ui.swing;

import org.czareg.game.Move;
import org.czareg.position.Position;

import java.util.Objects;
import java.util.Set;

record Selection(Position selectedPosition, Set<Move> highlightedMoves) {
    Selection {
        Objects.requireNonNull(highlightedMoves);
        if (selectedPosition == null && !highlightedMoves.isEmpty()) {
            throw new IllegalArgumentException("When selected position is null highlighted moves has to be empty");
        }
    }

    static final Selection EMPTY = new Selection(null, Set.of());

    boolean isNoPositionSelected() {
        return selectedPosition == null;
    }
}
