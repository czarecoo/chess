package org.czareg.move;

import lombok.extern.slf4j.Slf4j;
import org.czareg.game.Context;
import org.czareg.game.GeneratedMoves;
import org.czareg.game.Move;
import org.czareg.game.ThreatAnalyzer;
import org.czareg.piece.King;
import org.czareg.piece.Player;
import org.czareg.position.Position;

import static org.czareg.game.Metadata.Key.CASTLING_ROOK_END_POSITION;
import static org.czareg.game.Metadata.Key.MOVE_TYPE;
import static org.czareg.game.MoveType.CASTLING;

@Slf4j
public class ClassicOnlyValidKingMoveFilter implements OnlyValidKingMoveFilter {

    @Override
    public boolean filter(Context context, Move move) {
        if (move.getPiece().getClass() != King.class) {
            return true;
        }
        Position kingEndPosition = move.getEnd();
        if (simulateTheMoveAndCheckIfUnderAttack(context, move, kingEndPosition)) {
            log.debug("King would be in check after moving to {}.", kingEndPosition);
            return false;
        }
        boolean isCastleMove = move.getMetadata().isExactly(MOVE_TYPE, CASTLING);
        if (isCastleMove) {
            Position rookEndPosition = move.getMetadata().get(CASTLING_ROOK_END_POSITION, Position.class).orElseThrow();
            if (simulateTheMoveAndCheckIfUnderAttack(context, move, rookEndPosition)) {
                log.debug("King would pass through {} that is under attack.", rookEndPosition);
                return false;
            }
        }
        return true;
    }

    private boolean simulateTheMoveAndCheckIfUnderAttack(Context context, Move move, Position position) {
        Player player = move.getPiece().getPlayer();
        Context duplicatedContext = context.duplicate();
        MoveExecutor moveExecutor = duplicatedContext.getMoveExecutor();
        moveExecutor.execute(duplicatedContext, move);
        MoveGenerators moveGenerators = duplicatedContext.getMoveGenerators();
        GeneratedMoves generatedMoves = moveGenerators.generatePseudoLegal(duplicatedContext);
        ThreatAnalyzer threatAnalyzer = duplicatedContext.getThreatAnalyzer();
        return threatAnalyzer.isUnderAttack(generatedMoves, position, player);
    }
}
