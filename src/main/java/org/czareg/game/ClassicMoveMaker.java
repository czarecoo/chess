package org.czareg.game;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.czareg.game.state.StateValidator;
import org.czareg.move.MoveExecutor;
import org.czareg.move.MoveGenerators;

@Slf4j
@Getter
public class ClassicMoveMaker implements MoveMaker {

    @Override
    public void make(Context context, Move move) {
        StateValidator stateValidator = context.getStateValidator();
        stateValidator.validate(context);

        MoveLegalityValidator moveLegalityValidator = context.getMoveLegalityValidator();
        moveLegalityValidator.validate(context, move);

        MoveExecutor moveExecutor = context.getMoveExecutor();
        moveExecutor.execute(context, move);
        log.info("Made {}", move);

        MoveGenerators moveGenerators = context.getMoveGenerators();
        moveGenerators.getOrGenerateLegal(context);
    }
}
