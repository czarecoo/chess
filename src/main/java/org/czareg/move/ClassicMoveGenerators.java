package org.czareg.move;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.Context;
import org.czareg.game.GeneratedMoves;
import org.czareg.game.Move;
import org.czareg.game.hasher.ZobristHasher;
import org.czareg.piece.Player;

import java.util.EnumMap;
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
            Map<Player, Set<Move>> playerMoves = generatedMoves.getPlayerMoves();
            Map<Player, Set<Move>> result = new EnumMap<>(Player.class);
            for (Map.Entry<Player, Set<Move>> playerSetEntry : playerMoves.entrySet()) {
                Player player = playerSetEntry.getKey();
                Set<Move> moves = filterMoves(context, playerSetEntry);
                result.put(player, moves);
            }
            log.info("End generate legal moves, current hash={}", currentHash);
            return new GeneratedMoves(result);
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

    private Set<Move> filterMoves(Context context, Map.Entry<Player, Set<Move>> playerSetEntry) {
        return playerSetEntry.getValue()
                .stream()
                .filter(move -> onlyValidKingMoveFilter.filter(context, move))
                .collect(Collectors.toSet());
    }
}
