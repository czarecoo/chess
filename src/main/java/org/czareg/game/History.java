package org.czareg.game;

import org.czareg.piece.Piece;
import org.czareg.piece.Player;

import java.util.Optional;

public interface History {

    boolean hasPieceMovedBefore(Piece piece);

    void save(LegalMove legalMove);

    Optional<Player> getLastMovingPlayer();
}
