package org.czareg.game;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;
import org.czareg.board.Board;
import org.czareg.board.ClassicBoard;
import org.czareg.move.ClassicMoveExecutor;
import org.czareg.move.ClassicMoveGenerator;
import org.czareg.move.MoveExecutor;
import org.czareg.move.MoveGenerator;
import org.czareg.move.piece.ClassicPieceMoveGeneratorFactory;
import org.czareg.move.piece.PieceMoveGeneratorFactory;

@With
@Value
@RequiredArgsConstructor
public class ClassicContext implements Context {

    Board board;
    History history;
    Game game;
    MoveGenerator moveGenerator;
    Order order;
    MoveExecutor moveExecutor;
    PieceMoveGeneratorFactory pieceMoveGeneratorFactory;

    public ClassicContext() {
        this(
                new ClassicBoard(8, 8),
                new ClassicHistory(),
                new ClassicGame(),
                new ClassicMoveGenerator(),
                new ClassicOrder(),
                new ClassicMoveExecutor(),
                new ClassicPieceMoveGeneratorFactory()
        );
    }
}
