package org.czareg;

import java.util.List;
import java.util.Map;

interface Game {

    Board getBoard();
    boolean hasPieceMovedBefore(Piece piece);
    Map<Player,List<LegalMove>> getPlayerMoves();
    void makeMove(LegalMove legalMove);
}
