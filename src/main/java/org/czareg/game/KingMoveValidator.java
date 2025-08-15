package org.czareg.game;

import org.czareg.move.MoveExecutor;
import org.czareg.piece.King;
import org.czareg.piece.Player;
import org.czareg.position.Position;

import static org.czareg.game.Metadata.Key.CASTLING_ROOK_END_POSITION;
import static org.czareg.game.Metadata.Key.MOVE_TYPE;
import static org.czareg.game.MoveType.CASTLING;

public class KingMoveValidator implements MoveLegalityValidator {

    public void validate(Context context, Move move) {
        if (move.getPiece().getClass() != King.class) {
            return;
        }
        Position kingEndPosition = move.getEnd();
        simulateTheMoveAndCheckIfUnderAttack(context, move, kingEndPosition, "King would be in check after moving to %s that is under attack by %s.");
        boolean isCastleMove = move.getMetadata().isExactly(MOVE_TYPE, CASTLING);
        if (isCastleMove) {
            Position rookEndPosition = move.getMetadata().get(CASTLING_ROOK_END_POSITION, Position.class).orElseThrow();
            simulateTheMoveAndCheckIfUnderAttack(context, move, rookEndPosition, "King would pass through %s that is under attack by %s.");
        }
    }

    private void simulateTheMoveAndCheckIfUnderAttack(Context context, Move move, Position position, String message) {
        Player player = move.getPiece().getPlayer();
        Context duplicatedContext = context.duplicate();
        MoveExecutor moveExecutor = duplicatedContext.getMoveExecutor();
        moveExecutor.execute(duplicatedContext, move);
        ThreatAnalyzer threatAnalyzer = duplicatedContext.getThreatAnalyzer();
        if (threatAnalyzer.isUnderAttack(duplicatedContext, position, player)) {
            Player opponent = player.getOpponent();
            throw new IllegalArgumentException(message.formatted(position, opponent));
        }
    }
}
