package org.czareg.game;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.piece.King;
import org.czareg.piece.Player;

import java.util.EnumMap;
import java.util.Map;

@Slf4j
public class ClassicBoardValidator implements BoardValidator {

    @Override
    public void validate(Board board) {
        if (isEmptyBoard(board)) {
            throw new IllegalStateException("Board is empty.");
        } else if (hasIncorrectNumberOfKingsPerPlayer(board)) {
            throw new IllegalStateException("Invalid number of kings on the board");
        }
    }

    private boolean isEmptyBoard(Board board) {
        return board.getAllPiecePositions().isEmpty();
    }

    private boolean hasIncorrectNumberOfKingsPerPlayer(Board board) {
        Map<Player, Long> kingCounts = new EnumMap<>(Player.class);
        for (Player player : Player.values()) {
            long count = board.getAllPieces(player, King.class).size();
            kingCounts.put(player, count);
        }
        log.debug("King count per player: {}", kingCounts);
        return kingCounts.values()
                .stream()
                .anyMatch(count -> count != 1);
    }
}
