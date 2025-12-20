package org.czareg.game.state;

import org.czareg.game.Context;

public interface StateValidator {

    StateValidator NOOP = context -> {
    };

    void validate(Context context);
}
