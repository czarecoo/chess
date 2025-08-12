package org.czareg.game;

import lombok.extern.slf4j.Slf4j;
import org.czareg.move.MoveExecutor;
import org.czareg.piece.Player;

import java.util.List;

@Slf4j
public class ClassicStateAnalyzer implements StateAnalyzer {

    @Override
    public boolean isOver(Context context) {
        return isCheckMate(context) || isStalemate(context) || isDrawnBy75MoveRule(context) || isInsufficientMaterial(context);
    }

    @Override
    public boolean isCheckMate(Context context) {
        Player currentPlayer = context.getHistory().getLastMovingPlayer().map(Player::getOpponent).orElse(Player.WHITE);
        ThreatAnalyzer threatAnalyzer = context.getThreatAnalyzer();

        if (!threatAnalyzer.isInCheck(context, currentPlayer)) {
            log.debug("Not in check so it can't be checkmate");
            return false;
        }

        List<Move> movesThatEscapeCheckMate = context.getMoveGenerator()
                .generate(context, currentPlayer)
                .filter(move -> {
                    Context cloned = context.duplicate();
                    MoveExecutor moveExecutor = cloned.getMoveExecutor();
                    moveExecutor.execute(cloned, move);
                    return !threatAnalyzer.isInCheck(cloned, currentPlayer);
                }).toList();
        log.debug("Found {} moves after which {} would not be in check anymore. Moves: {}", movesThatEscapeCheckMate.size(), currentPlayer, movesThatEscapeCheckMate);
        return movesThatEscapeCheckMate.isEmpty();
    }

    @Override
    public boolean isStalemate(Context context) {
        return false;
    }

    @Override
    public boolean isDrawnBy75MoveRule(Context context) {
        return false;
    }

    @Override
    public boolean isInsufficientMaterial(Context context) {
        return false;
    }
}
