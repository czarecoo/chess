package org.czareg.game.state;

import org.czareg.piece.Player;

public sealed interface State permits State.InProgress, State.Draw, State.Win {

    record Win(Player winner) implements State {
    }

    record InProgress(Player moving) implements State {
    }

    record Draw(String reason) implements State {
    }
}
