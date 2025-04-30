package org.czareg;

interface Game {

    Board getBoard();

    History getHistory();

    void makeMove(LegalMove legalMove);

    MoveGenerator getMoveGenerator();

    Order getOrder();
}
