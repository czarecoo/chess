package org.czareg.game;

public interface StateValidator {

    StateValidator NOOP = context -> {
    };

    void validate(Context context);
}
