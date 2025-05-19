package org.czareg.game;

import org.czareg.board.Board;
import org.czareg.move.MoveExecutor;
import org.czareg.move.MoveGenerator;
import org.czareg.piece.Player;
import org.czareg.position.Position;

public interface Game {

    Board getBoard();

    History getHistory();

    void makeMove(Move move);

    MoveGenerator getMoveGenerator();

    Order getOrder();

    MoveExecutor getMoveExecutor();

    boolean isUnderAttack(Position position, Player byPlayer);
}
