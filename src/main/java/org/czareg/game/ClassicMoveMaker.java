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
//        BoardValidator boardValidator = context.getBoardValidator();
//        Board board = context.getBoard();
//        boardValidator.validate(board);

        StateValidator stateValidator = context.getStateValidator();
        stateValidator.validate(context);

        PlayerTurnValidator playerTurnValidator = context.getPlayerTurnValidator();
        Player player = move.getPiece().getPlayer();
        playerTurnValidator.validate(context, player);

        MoveLegalityValidator moveLegalityValidator = context.getMoveLegalityValidator();
        moveLegalityValidator.validate(context, move);

        MoveExecutor moveExecutor = context.getMoveExecutor();
        moveExecutor.execute(context, move);
        log.info("Made {}", move);
    }
}
