package org.czareg.move;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.Context;
import org.czareg.game.GeneratedMoves;

@Slf4j
@RequiredArgsConstructor
public class CachingLegalMoveGenerator {

    private final LegalMoveGenerator legalMoveGenerator;

    private CachedGeneratedMoves cachedGeneratedMoves;

    public GeneratedMoves generate(Context context) {
        Board board = context.getBoard();
        int boardHashCode = board.hashCode();
        if (isCachedAlready(boardHashCode)) {
            log.info("Using cached legal generated moves");
            return cachedGeneratedMoves.generatedMoves();
        }
        GeneratedMoves generatedMoves = legalMoveGenerator.generate(context);
        cachedGeneratedMoves = new CachedGeneratedMoves(boardHashCode, generatedMoves);
        return generatedMoves;
    }

    private boolean isCachedAlready(int boardHashCode) {
        return cachedGeneratedMoves != null && cachedGeneratedMoves.boardHashCode() == boardHashCode;
    }

    record CachedGeneratedMoves(int boardHashCode, GeneratedMoves generatedMoves) {
    }
}
