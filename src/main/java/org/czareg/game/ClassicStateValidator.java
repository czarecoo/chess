package org.czareg.game;

import lombok.extern.slf4j.Slf4j;
import org.czareg.move.MoveExecutor;
import org.czareg.move.MoveGenerator;
import org.czareg.piece.Pawn;
import org.czareg.piece.Player;

import java.util.List;

import static org.czareg.game.Metadata.Key.MOVE_TYPE;

@Slf4j
public class ClassicStateValidator implements StateValidator {

    @Override
    public void validate(Context context) {
        if (isInsufficientMaterial(context)) {
            throw new IllegalStateException("Insufficient material.");
        } else if (isDrawnBy50MoveRule(context.getHistory())) {
            throw new IllegalStateException("Drawn by 50 move rule.");
        }
        ThreatAnalyzer threatAnalyzer = context.getThreatAnalyzer();
        Player currentPlayer = context.getHistory().getLastMovingPlayer().map(Player::getOpponent).orElse(Player.WHITE);
        boolean hasNoLegalMoves = !hasLegalMove(context, currentPlayer);
        if (hasNoLegalMoves) {
            boolean inInCheck = threatAnalyzer.isInCheck(context, currentPlayer);
            if (inInCheck) {
                throw new IllegalStateException("Checkmate");
            } else {
                throw new IllegalStateException("Stalemate");
            }
        }
    }

    private boolean hasLegalMove(Context context, Player currentPlayer) {
        ThreatAnalyzer threatAnalyzer = context.getThreatAnalyzer();
        MoveGenerator moveGenerator = context.getMoveGenerator();
        return moveGenerator.generateLegal(context)
                .getMoves(currentPlayer)
                .stream()
                .anyMatch(move -> {
                    Context duplicatedContext = context.duplicate();
                    MoveExecutor moveExecutor = duplicatedContext.getMoveExecutor();
                    moveExecutor.execute(duplicatedContext, move);
                    return !threatAnalyzer.isInCheck(duplicatedContext, currentPlayer);
                });
    }

    private boolean isDrawnBy50MoveRule(History history) {
        if (history.movesCount() < 100) {
            return false;
        }

        List<Move> last100Moves = history.getLastXMoves(100);

        return last100Moves.stream()
                .noneMatch(move -> {
                    MoveType moveType = move.getMetadata().get(MOVE_TYPE, MoveType.class).orElseThrow();
                    return move.getPiece() instanceof Pawn
                            || moveType == MoveType.CAPTURE
                            || moveType == MoveType.INITIAL_DOUBLE_FORWARD
                            || moveType == MoveType.PROMOTION
                            || moveType == MoveType.PROMOTION_CAPTURE
                            || moveType == MoveType.EN_PASSANT;
                });
    }

    /*
        The game is declared a draw whenever both sides do not have the "sufficient material" to force a checkmate.
        Insufficient material (no checkmates are possible or no checkmates can be forced):
        - King vs king
        - King + bishop or knight vs king
        - King + Bishop vs. King + Bishop (both bishops on the same color)
        - King + 2 knights vs King

        In the specific case of two knights versus a lone king
        USCF (United States Chess Federation): The rule specifies a game is drawn if there is no forced mate.
        FIDE (International Chess Federation): The rule states a game is only drawn when a checkmate is absolutely impossible,
        meaning that two knights versus a lone king may not be an automatic draw since a checkmate could theoretically occur
        if the lone king 'helps' you by making specific moves to allow the checkmate.

        Chess.com follows the USCF rule in this case and calls two knights insufficient mating material because the checkmate can not be forced.

        I am going with USCF rule also.
     */
    private boolean isInsufficientMaterial(Context context) {
        return false;
    }
}
