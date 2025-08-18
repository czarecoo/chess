package org.czareg.game;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;
import org.czareg.board.Board;
import org.czareg.board.ClassicBoard;
import org.czareg.move.*;
import org.czareg.move.piece.ClassicPieceMoveGeneratorFactory;
import org.czareg.move.piece.PieceMoveGeneratorFactory;

@With
@Value
@RequiredArgsConstructor
public class ClassicContext implements Context {

    Board board;
    History history;
    MoveMaker moveMaker;
    MoveGenerator moveGenerator;
    PlayerTurnValidator playerTurnValidator;
    MoveExecutor moveExecutor;
    PieceMoveGeneratorFactory pieceMoveGeneratorFactory;
    MoveLegalityValidator moveLegalityValidator;
    ThreatAnalyzer threatAnalyzer;
    StateValidator stateValidator;
    BoardValidator boardValidator;

    public ClassicContext() {
        this(
                new ClassicBoard(8, 8),
                new ClassicHistory(),
                new ClassicMoveMaker(),
                new ClassicMoveGenerator(new CachingLegalMoveGenerator(new LegalMoveGenerator(new PseudoLegalMoveGenerator(), new ClassicOnlyValidKingMoveFilter())), new PseudoLegalMoveGenerator()),
                new ClassicPlayerTurnValidator(),
                new ClassicMoveExecutor(),
                new ClassicPieceMoveGeneratorFactory(),
                new ClassicMoveLegalityValidator(),
                new ClassicThreatAnalyzer(),
                new ClassicStateValidator(),
                new ClassicBoardValidator()
        );
    }

    @Override
    public ClassicContext duplicate() {
        return new ClassicContext(
                board.duplicate(),
                history.duplicate(),
                moveMaker,
                moveGenerator,
                playerTurnValidator,
                moveExecutor,
                pieceMoveGeneratorFactory,
                moveLegalityValidator,
                threatAnalyzer,
                stateValidator,
                boardValidator
        );
    }
}
