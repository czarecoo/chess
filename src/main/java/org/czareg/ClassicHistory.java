package org.czareg;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ClassicHistory implements History {

    private final List<LegalMove> history;

    ClassicHistory() {
        history = new ArrayList<>();
    }

    @Override
    public boolean hasPieceMovedBefore(Piece piece) {
        return history
                .stream()
                .anyMatch(legalMove -> legalMove.getPiece() == piece);
    }

    @Override
    public void save(LegalMove legalMove) {
        history.add(legalMove);
    }

    @Override
    public Optional<Player> getLastMovingPlayer() {
        if (history.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(history.getLast())
                .map(LegalMove::getPiece)
                .map(Piece::getPlayer);
    }
}
