package org.czareg.game;

import org.czareg.piece.Player;

public interface PlayerTurnValidator {

    void validate(Context context, Player requestingPlayer);
}
