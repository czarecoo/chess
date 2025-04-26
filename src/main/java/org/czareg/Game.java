package org.czareg;

import java.util.List;
import java.util.Map;

interface Game {

    Map<Player,List<LegalMove>> getPlayerMoves();
    List<Player> getPlayerOrder();
    Board getBoard();
}
