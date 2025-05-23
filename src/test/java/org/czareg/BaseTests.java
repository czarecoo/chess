package org.czareg;

import org.czareg.board.Board;
import org.czareg.game.ClassicContext;
import org.czareg.game.Context;
import org.czareg.game.History;
import org.czareg.game.MoveMaker;
import org.czareg.move.MoveGenerator;
import org.czareg.move.piece.PieceMoveGeneratorFactory;
import org.czareg.position.PositionFactory;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTests {

    protected Context context;
    protected Board board;
    protected PositionFactory pf;
    protected MoveGenerator moveGenerator;
    protected MoveMaker moveMaker;
    protected History history;
    protected PieceMoveGeneratorFactory pieceMoveGeneratorFactory;

    @BeforeEach
    protected void init() {
        context = new ClassicContext();
        board = context.getBoard();
        pf = board.getPositionFactory();
        moveGenerator = context.getMoveGenerator();
        moveMaker = context.getMoveMaker();
        history = context.getHistory();
        pieceMoveGeneratorFactory = context.getPieceMoveGeneratorFactory();
    }
}
