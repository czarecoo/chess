package org.czareg.game;

import org.czareg.game.validator.InsufficientMaterialChecker;
import org.czareg.move.MoveGenerators;
import org.czareg.piece.Pawn;

import java.util.List;

import static org.czareg.game.Metadata.Key.MOVE_TYPE;

public class ClassicStateValidator implements StateValidator {

    @Override
    public void validate(Context context) {
        History history = context.getHistory();
        if (isInsufficientMaterial(context)) {
            throw new IllegalStateException("Insufficient material.");
        } else if (isDrawnBy50MoveRule(history)) {
            throw new IllegalStateException("Drawn by 50 move rule.");
        }
        if (hasNoLegalMoves(context)) {
            throw new IllegalStateException("There are no legal moves available.");
        }
    }

    private boolean hasNoLegalMoves(Context context) {
        MoveGenerators moveGenerators = context.getMoveGenerators();
        return moveGenerators.generateLegal(context)
                .getMoves().isEmpty();
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

    private boolean isInsufficientMaterial(Context context) {
        InsufficientMaterialChecker insufficientMaterialChecker = new InsufficientMaterialChecker(context);
        return insufficientMaterialChecker.check();
    }
}
