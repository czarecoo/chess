package org.czareg.game;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.czareg.board.Board;
import org.czareg.board.ClassicBoard;
import org.czareg.move.*;
import org.czareg.move.piece.ClassicPieceMoveGeneratorFactory;
import org.czareg.move.piece.PieceMoveGeneratorFactory;

@Value
@RequiredArgsConstructor
public class ClassicContext implements Context {

    Board board;
    History history;
    MoveMaker moveMaker;
    MoveGenerators moveGenerators;
    PlayerTurnValidator playerTurnValidator;
    MoveExecutor moveExecutor;
    PieceMoveGeneratorFactory pieceMoveGeneratorFactory;
    MoveLegalityValidator moveLegalityValidator;
    ThreatAnalyzer threatAnalyzer;
    StateValidator stateValidator;
    BoardValidator boardValidator;

    @Override
    public ClassicContext duplicate() {
        return new ClassicContext(
                board.duplicate(),
                history.duplicate(),
                moveMaker,
                moveGenerators,
                playerTurnValidator,
                moveExecutor,
                pieceMoveGeneratorFactory,
                moveLegalityValidator,
                threatAnalyzer,
                stateValidator,
                boardValidator
        );
    }

    public static ClassicContext create() {
        MoveGenerator pseudoLegalMoveGenerator = new PseudoLegalMoveGenerator();
        return new ClassicContext(new ClassicBoard(8, 8),
                new ClassicHistory(),
                new ClassicMoveMaker(),
                new ClassicMoveGenerators(new LegalMoveGenerator(pseudoLegalMoveGenerator, new ClassicOnlyValidKingMoveFilter()), pseudoLegalMoveGenerator),
                new ClassicPlayerTurnValidator(),
                new ClassicMoveExecutor(),
                new ClassicPieceMoveGeneratorFactory(),
                new ClassicMoveLegalityValidator(),
                new ClassicThreatAnalyzer(),
                new ClassicStateValidator(),
                new ClassicBoardValidator()
        );
    }
}
