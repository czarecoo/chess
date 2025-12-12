package org.czareg.game.state;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.*;
import org.czareg.game.validator.InsufficientMaterialChecker;
import org.czareg.move.MoveGenerators;
import org.czareg.piece.King;
import org.czareg.piece.Pawn;
import org.czareg.piece.Player;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.czareg.game.Metadata.Key.MOVE_TYPE;

@Slf4j
public class ClassicState implements StateValidator, StateChecker {

    @Override
    public State check(Context context) {
        if (isInsufficientMaterial(context)) {
            return new State.Draw();
        }
        History history = context.getHistory();
        if (isDrawnBy50MoveRule(history)) {
            return new State.Draw();
        }
        ThreatAnalyzer threatAnalyzer = context.getThreatAnalyzer();
        Player currentPlayer = history.getCurrentPlayer();
        if (hasNoLegalMoves(context)) {
            if (threatAnalyzer.isKingUnderAttack(context, currentPlayer)) {
                return new State.Win(currentPlayer.getOpponent());
            }
        }
        return new State.InProgress(currentPlayer);
    }

    @Override
    public void validate(Context context) {
        Board board = context.getBoard();
        if (isEmptyBoard(board)) {
            throw new IllegalStateException("Board is empty.");
        } else if (hasIncorrectNumberOfKingsPerPlayer(board)) {
            throw new IllegalStateException("Invalid number of kings on the board");
        }
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

    private boolean isEmptyBoard(Board board) {
        return board.getAllPiecePositions().isEmpty();
    }

    private boolean hasIncorrectNumberOfKingsPerPlayer(Board board) {
        Map<Player, Long> kingCounts = new EnumMap<>(Player.class);
        for (Player player : Player.values()) {
            long count = board.getAllPiecePositions(player, King.class).size();
            kingCounts.put(player, count);
        }
        log.debug("King count per player: {}", kingCounts);
        return kingCounts.values()
                .stream()
                .anyMatch(count -> count != 1);
    }

    private boolean hasNoLegalMoves(Context context) {
        MoveGenerators moveGenerators = context.getMoveGenerators();
        return moveGenerators.getOrGenerateLegal(context).isEmpty();
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
