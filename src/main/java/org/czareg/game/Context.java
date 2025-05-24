package org.czareg.game;

import org.czareg.board.Board;
import org.czareg.move.MoveExecutor;
import org.czareg.move.MoveGenerator;
import org.czareg.move.piece.PieceMoveGeneratorFactory;

public interface Context extends Duplicatable<Context> {

    Board getBoard();

    History getHistory();

    Game getGame();

    MoveGenerator getMoveGenerator();

    Order getOrder();

    MoveExecutor getMoveExecutor();

    PieceMoveGeneratorFactory getPieceMoveGeneratorFactory();

    MoveLegalityChecker getMoveLegalityChecker();
}
