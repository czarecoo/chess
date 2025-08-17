package org.czareg.move;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czareg.game.Context;
import org.czareg.game.GeneratedMoves;

@Slf4j
@RequiredArgsConstructor
public class ClassicMoveGenerator implements MoveGenerator {

    private final CachingLegalMoveGenerator cachingLegalMoveGenerator;
    private final PseudoLegalMoveGenerator pseudoLegalMoveGenerator;

    @Override
    public GeneratedMoves generateLegal(Context context) {
        log.info("Start generate legal moves");
        GeneratedMoves generatedMoves = cachingLegalMoveGenerator.generate(context);
        log.info("End generate legal moves");
        return generatedMoves;
    }

    @Override
    public GeneratedMoves generatePseudoLegal(Context context) {
        log.info("Start generate pseudo legal moves");
        GeneratedMoves generatedMoves = pseudoLegalMoveGenerator.generate(context);
        log.info("End generate psuedo legal moves");
        return generatedMoves;
    }
}
