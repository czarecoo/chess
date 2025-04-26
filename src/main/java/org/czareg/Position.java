package org.czareg;

import java.util.Objects;

record Position(Rank rank, File file) {

    Position {
        Objects.requireNonNull(file);
        Objects.requireNonNull(rank);
    }

    IndexPosition with(int rankChange, int fileChange) {
        return new IndexPosition(rank().getIndex() + rankChange, file().getIndex() + fileChange);
    }
}
