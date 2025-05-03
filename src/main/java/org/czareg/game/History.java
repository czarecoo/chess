package org.czareg.game;

import org.czareg.piece.Piece;
import org.czareg.piece.Player;

import java.util.Optional;

public interface History {

    boolean hasPieceMovedBefore(Piece piece);

    void save(Move move);

    Optional<Player> getLastMovingPlayer();

    Optional<Move> getLastPlayedMove();
}
