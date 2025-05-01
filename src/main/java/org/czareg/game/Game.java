package org.czareg.game;

import org.czareg.board.Board;
import org.czareg.piece.move.MoveGenerator;

public interface Game {

    Board getBoard();

    History getHistory();

    void makeMove(LegalMove legalMove);

    MoveGenerator getMoveGenerator();

    Order getOrder();
}
