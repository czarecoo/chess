package org.czareg.move;

import lombok.extern.slf4j.Slf4j;
import org.czareg.game.Context;
import org.czareg.game.Move;
import org.czareg.game.ThreatAnalyzer;
import org.czareg.piece.Player;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import static org.czareg.game.Metadata.Key.MOVE_TYPE;
import static org.czareg.game.MoveType.CASTLING;

@Slf4j
public class ClassicLegalMoveFilter implements LegalMoveFilter {

    @Override
    public boolean isLegal(Context context, Move move) {
        Player currentPlayer = move.getPiece().getPlayer();

        Context duplicatedContext = context.duplicate();
        MoveExecutor moveExecutor = duplicatedContext.getMoveExecutor();
        moveExecutor.execute(duplicatedContext, move);

        ThreatAnalyzer threatAnalyzer = duplicatedContext.getThreatAnalyzer();

        if (threatAnalyzer.isKingUnderAttack(duplicatedContext, currentPlayer)) {
            log.debug("Move {} would leave {} in check.", move, currentPlayer);
            return false;
        }

        if (isCastleMove(move)) {
            return isCastlingPathSafe(context, move, currentPlayer);
        }

        return true;
    }

    private boolean isCastleMove(Move move) {
        return move.getMetadata().isExactly(MOVE_TYPE, CASTLING);
    }

    private boolean isCastlingPathSafe(Context context, Move move, Player player) {
        ThreatAnalyzer threatAnalyzer = context.getThreatAnalyzer();
        PositionFactory pf = context.getBoard().getPositionFactory();

        Position start = move.getStart();
        Position end = move.getEnd();
        int dir = Integer.signum(pf.create(end).getFile() - pf.create(start).getFile());

        for (int step = pf.create(start).getFile(); step != pf.create(end).getFile() + dir; step += dir) {
            Position position = pf.create(step, pf.create(start).getRank());
            if (threatAnalyzer.isPositionUnderAttack(context, position, player)) {
                log.debug("Castling path square {} is under attack", position);
                return false;
            }
        }
        return true;
    }
}
