package org.czareg.move;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.Context;
import org.czareg.game.GeneratedMoves;
import org.czareg.game.Move;
import org.czareg.game.hasher.ZobristHasher;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ClassicMoveGenerators implements MoveGenerators {

    private final MoveGenerator pseudoLegalMoveGenerator;
    private final OnlyValidKingMoveFilter onlyValidKingMoveFilter;
    private final Map<Long, GeneratedMoves> legalMovesCache;
    private final Map<Long, GeneratedMoves> pseudoLegalMovesCache;

    public ClassicMoveGenerators(MoveGenerator pseudoLegalMoveGenerator, OnlyValidKingMoveFilter onlyValidKingMoveFilter) {
        this.pseudoLegalMoveGenerator = pseudoLegalMoveGenerator;
        this.onlyValidKingMoveFilter = onlyValidKingMoveFilter;
        legalMovesCache = new HashMap<>();
        pseudoLegalMovesCache = new HashMap<>();
    }

    @Override
    public GeneratedMoves generateLegal(Context context) {
        Board board = context.getBoard();
        ZobristHasher zobristHasher = board.getZobristHasher();
        long currentHash = zobristHasher.computeHash(context);
        return legalMovesCache.computeIfAbsent(currentHash, hash -> {
            log.info("Start generate legal moves, current hash={}", currentHash);
            GeneratedMoves generatedMoves = generatePseudoLegal(context);
            Set<Move> legalMoves = generatedMoves.getMoves()
                    .stream()
                    .filter(move -> onlyValidKingMoveFilter.filter(context, move))
                    .collect(Collectors.toSet());
            log.info("End generate legal moves, current hash={}", currentHash);
            return new GeneratedMoves(legalMoves);
        });
    }

    @Override
    public GeneratedMoves generatePseudoLegal(Context context) {
        Board board = context.getBoard();
        ZobristHasher zobristHasher = board.getZobristHasher();
        long currentHash = zobristHasher.computeHash(context);
        return pseudoLegalMovesCache.computeIfAbsent(currentHash, hash -> {
            log.info("Start generate pseudo legal moves, current hash={}", currentHash);
            GeneratedMoves generatedMoves = pseudoLegalMoveGenerator.generate(context);
            log.info("End generate pseudo legal moves, current hash={}", currentHash);
            return generatedMoves;
        });
    }
}
