package org.czareg.ui.swing;

import org.czareg.game.Move;
import org.czareg.position.Position;

import java.util.Set;

record Selection(Position selectedPosition, Set<Move> highlightedMoves) {
    Selection {
        if (highlightedMoves == null) {
            throw new IllegalArgumentException("Null collection");
        }
        if (selectedPosition == null && !highlightedMoves.isEmpty()) {
            throw new IllegalArgumentException("When selected position is null highlighted moves has to be empty");
        }
    }

    static Selection EMPTY = new Selection(null, Set.of());

    boolean isEmpty() {
        return selectedPosition == null;
    }
}
