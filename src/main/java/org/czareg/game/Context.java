package org.czareg.game;

import org.czareg.board.Board;
import org.czareg.game.state.StateChecker;
import org.czareg.move.MoveExecutor;
import org.czareg.move.MoveGenerators;
import org.czareg.move.piece.PieceMoveGeneratorFactory;

public interface Context extends Duplicatable<Context> {

    Board getBoard();

    History getHistory();

    MoveMaker getMoveMaker();

    MoveGenerators getMoveGenerators();

    MoveExecutor getMoveExecutor();

    PieceMoveGeneratorFactory getPieceMoveGeneratorFactory();

    MoveLegalityValidator getMoveLegalityValidator();

    ThreatAnalyzer getThreatAnalyzer();

    StateValidator getStateValidator();

    StateChecker getStateChecker();
}
