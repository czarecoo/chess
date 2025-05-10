package org.czareg.game;

import org.czareg.board.Board;
import org.czareg.move.MoveExecutor;
import org.czareg.move.MoveGenerator;

public interface Game {

    Board getBoard();

    History getHistory();

    void makeMove(Move move);

    MoveGenerator getMoveGenerator();

    Order getOrder();

    MoveExecutor getMoveExecutor();
}
