package org.czareg.game;

import lombok.RequiredArgsConstructor;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ClassicHistory implements History {

    private final List<Move> history;

    public ClassicHistory() {
        history = new ArrayList<>();
    }

    @Override
    public boolean hasPieceMovedBefore(Piece piece) {
        return history
                .stream()
                .anyMatch(move -> move.getPiece() == piece);
    }

    @Override
    public void save(Move move) {
        history.add(move);
    }

    @Override
    public Optional<Player> getLastMovingPlayer() {
        return getLastPlayedMove()
                .map(Move::getPiece)
                .map(Piece::getPlayer);
    }

    @Override
    public Optional<Move> getLastPlayedMove() {
        if (history.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(history.getLast());
    }

    @Override
    public ClassicHistory duplicate() {
        return new ClassicHistory(new ArrayList<>(history));
    }

    @Override
    public int movesCount() {
        return history.size();
    }

    @Override
    public List<Move> getLastXMoves(int count) {
        int start = Math.max(0, history.size() - count);
        List<Move> subList = history.subList(start, history.size());
        return List.copyOf(subList);
    }
}
