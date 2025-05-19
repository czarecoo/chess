package org.czareg.piece;

public enum Player {

    WHITE, BLACK;

    public Player getOpponent() {
        return switch (this) {
            case WHITE -> BLACK;
            case BLACK -> WHITE;
        };
    }
}
