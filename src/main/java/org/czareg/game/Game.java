package org.czareg.game;

import org.czareg.board.Board;
import org.czareg.piece.move.MoveGenerator;
import org.czareg.piece.move.SpecialMoveExecutor;

public interface Game {

    Board getBoard();

    History getHistory();

    void makeMove(Move move);

    MoveGenerator getMoveGenerator();

    Order getOrder();

    SpecialMoveExecutor getSpecialMoveExecutor();
}
