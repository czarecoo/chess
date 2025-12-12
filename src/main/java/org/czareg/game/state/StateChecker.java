package org.czareg.game.state;

import org.czareg.game.Context;

public interface StateChecker {

    State check(Context context);
}
