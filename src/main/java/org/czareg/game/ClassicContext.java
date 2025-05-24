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
    PlayerTurnValidator playerTurnValidator;
    MoveExecutor moveExecutor;
    PieceMoveGeneratorFactory pieceMoveGeneratorFactory;
    MoveLegalityValidator moveLegalityValidator;

    public ClassicContext() {
        this(
                new ClassicBoard(8, 8),
                new ClassicHistory(),
                new ClassicGame(),
                new ClassicMoveGenerator(),
                new ClassicPlayerTurnValidator(),
                new ClassicMoveExecutor(),
                new ClassicPieceMoveGeneratorFactory(),
                new ClassicMoveLegalityValidator()
        );
    }

    @Override
    public ClassicContext duplicate() {
        return new ClassicContext(
                board.duplicate(),
                history.duplicate(),
                game,
                moveGenerator,
                playerTurnValidator,
                moveExecutor,
                pieceMoveGeneratorFactory,
                moveLegalityValidator
        );
    }
}
