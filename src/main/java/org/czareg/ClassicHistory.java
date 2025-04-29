package org.czareg;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.czareg.Player.values;

class ClassicHistory implements History {

    private final Map<Player, List<LegalMove>> history;

    ClassicHistory() {
        history = new EnumMap<>(Player.class);
        for (Player player : values()) {
            history.put(player, new ArrayList<>());
        }
    }

    @Override
    public boolean hasPieceMovedBefore(Piece piece) {
        return history
                .get(piece.getPlayer())
                .stream()
                .anyMatch(legalMove -> legalMove.piece() == piece);
    }

    @Override
    public void save(LegalMove legalMove) {
        Player player = legalMove.piece().getPlayer();
        history.get(player).add(legalMove);
    }
}
