package org.czareg;

import java.util.List;
import java.util.Map;

interface Game {

    boolean hasPieceMovedBefore(Piece piece);
    Map<Player,List<LegalMove>> getPlayerMoves();
    void makeMove(LegalMove legalMove);
}
