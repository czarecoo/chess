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
public class ClassicOnlyValidKingMoveFilter implements OnlyValidKingMoveFilter {

    @Override
    public boolean filter(Context context, Move move) {
        Player currentPlayer = move.getPiece().getPlayer();

        // 1. Duplicate context and execute move
        Context simulation = context.duplicate();
        simulation.getMoveExecutor().execute(simulation, move);

        // 2. Get the threat analyzer — must use *pure pseudo-attack* logic
        ThreatAnalyzer threatAnalyzer = simulation.getThreatAnalyzer();

        // 3. If the player's king is attacked in this new position → illegal move
        if (threatAnalyzer.isKingUnderAttack(simulation, currentPlayer)) {
            log.debug("Move {} would leave {} in check.", move, currentPlayer);
            return false;
        }

        // 4. Special handling for castling (check if path is safe)
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
            Position pos = pf.create(step, pf.create(start).getRank());
            if (threatAnalyzer.isSquareUnderAttack(context, pos, player)) {
                log.debug("Castling path square {} is under attack", pos);
                return false;
            }
        }
        return true;
    }
}
