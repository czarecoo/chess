package org.czareg.move;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czareg.game.Context;
import org.czareg.game.GeneratedMoves;

@Slf4j
@RequiredArgsConstructor
public class ClassicMoveGenerators implements MoveGenerators {

    private final MoveGenerator legalMoveGenerator;
    private final MoveGenerator pseudoLegalMoveGenerator;

    @Override
    public GeneratedMoves generateLegal(Context context) {
        log.debug("Start generate legal moves");
        GeneratedMoves generatedMoves = legalMoveGenerator.generate(context);
        log.debug("End generate legal moves");
        return generatedMoves;
    }

    @Override
    public GeneratedMoves generatePseudoLegal(Context context) {
        log.debug("Start generate pseudo legal moves");
        GeneratedMoves generatedMoves = pseudoLegalMoveGenerator.generate(context);
        log.debug("End generate pseudo legal moves");
        return generatedMoves;
    }
}
