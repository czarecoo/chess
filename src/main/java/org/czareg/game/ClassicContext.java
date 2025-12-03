package org.czareg.game;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;
import org.czareg.board.Board;
import org.czareg.board.ClassicBoard;
import org.czareg.move.*;
import org.czareg.move.piece.ClassicPieceMoveGeneratorFactory;
import org.czareg.move.piece.PieceMoveGeneratorFactory;

@Value
@RequiredArgsConstructor
@With
public class ClassicContext implements Context {

    Board board;
    History history;
    MoveMaker moveMaker;
    MoveGenerators moveGenerators;
    MoveExecutor moveExecutor;
    PieceMoveGeneratorFactory pieceMoveGeneratorFactory;
    MoveLegalityValidator moveLegalityValidator;
    ThreatAnalyzer threatAnalyzer;
    StateValidator stateValidator;

    @Override
    public ClassicContext duplicate() {
        return new ClassicContext(
                board.duplicate(),
                history.duplicate(),
                moveMaker,
                moveGenerators,
                moveExecutor,
                pieceMoveGeneratorFactory,
                moveLegalityValidator,
                threatAnalyzer,
                stateValidator
        );
    }

    public static ClassicContext create() {
        return new ClassicContext(new ClassicBoard(8, 8),
                new ClassicHistory(),
                new ClassicMoveMaker(),
                new ClassicMoveGenerators(new PseudoLegalMoveGenerator(), new ClassicLegalMoveFilter()),
                new ClassicMoveExecutor(),
                new ClassicPieceMoveGeneratorFactory(),
                new ClassicMoveLegalityValidator(),
                new ClassicThreatAnalyzer(),
                new ClassicStateValidator()
        );
    }
}
