package org.czareg.game;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.czareg.move.MoveExecutor;
import org.czareg.piece.Player;

@Slf4j
@Getter
public class ClassicMoveMaker implements MoveMaker {

    @Override
    public void make(Context context, Move move) {
        PlayerTurnValidator playerTurnValidator = context.getPlayerTurnValidator();
        Player player = move.getPiece().getPlayer();
        playerTurnValidator.validate(context, player);

        MoveLegalityValidator moveLegalityValidator = context.getMoveLegalityValidator();
        moveLegalityValidator.validate(context, move);

        MoveExecutor moveExecutor = context.getMoveExecutor();
        moveExecutor.execute(context, move);

        History history = context.getHistory();
        history.save(move);
    }
}
