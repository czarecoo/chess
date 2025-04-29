package org.czareg;

import java.util.Optional;

interface History {

    boolean hasPieceMovedBefore(Piece piece);

    void save(LegalMove legalMove);

    Optional<Player> getLastMovingPlayer();
}
