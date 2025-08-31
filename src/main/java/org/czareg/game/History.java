package org.czareg.game;

import org.czareg.piece.Piece;
import org.czareg.piece.Player;

import java.util.List;
import java.util.Optional;

public interface History extends Duplicatable<History> {

    boolean hasPieceMovedBefore(Piece piece);

    void save(Move move);

    Optional<Player> getLastMovingPlayer();

    Player getCurrentPlayer();

    Optional<Move> getLastPlayedMove();

    int movesCount();

    List<Move> getLastXMoves(int count);
}
