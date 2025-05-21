package org.czareg.game;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.czareg.move.MoveExecutor;
import org.czareg.move.MoveGenerator;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.position.Position;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Getter
public class ClassicGame implements Game {

    @Override
    public void makeMove(Context context, Move move) {
        checkIfPlayersTurn(context, move);
        checkIfLegal(context, move);
        MoveExecutor moveExecutor = context.getMoveExecutor();
        moveExecutor.execute(context, move);
        History history = context.getHistory();
        history.save(move);
    }

    @Override
    public boolean isUnderAttack(Context context, Position position, Player defender, Player attacker) {
        MoveGenerator moveGenerator = context.getMoveGenerator();
        List<Move> attackMoves = moveGenerator.generate(context, attacker)
                .filter(move -> move.getEnd().equals(position))
                .filter(move -> move.getMetadata().isExactly(Metadata.Key.MOVE_TYPE, MoveType.CAPTURE))
                .filter(move -> move.getMetadata().get(Metadata.Key.CAPTURE_PIECE, Piece.class)
                        .filter(piece -> piece.getPlayer() == defender)
                        .isPresent()
                ).toList();
        log.debug("Generated {} moves that attack {} where defender {} and attacker {}", attackMoves.size(), position, defender, attacker);
        return !attackMoves.isEmpty();
    }

    private void checkIfPlayersTurn(Context context, Move move) {
        History history = context.getHistory();
        Order order = context.getOrder();
        Player nowMovingPlayer = history.getLastMovingPlayer()
                .map(order::getNowMovingPlayer)
                .orElse(order.startingPlayer());
        Player requestingPlayer = move.getPiece().getPlayer();
        if (requestingPlayer != nowMovingPlayer) {
            throw new IllegalArgumentException("Now moving player %s not player %s".formatted(nowMovingPlayer, requestingPlayer));
        }
    }

    private void checkIfLegal(Context context, Move move) {
        MoveGenerator moveGenerator = context.getMoveGenerator();
        Stream<Move> moveStream = moveGenerator.generate(context, move.getStart());
        Set<Move> moves = moveStream.collect(Collectors.toSet());
        log.debug("Generated {} moves.", moves.size());
        boolean noMatchingGeneratedMoveFound = moves.stream()
                .filter(generatedMove -> Objects.equals(move.getStart(), generatedMove.getStart()))
                .filter(generatedMove -> Objects.equals(move.getEnd(), generatedMove.getEnd()))
                .filter(generatedMove -> move.getPiece() == generatedMove.getPiece())
                .filter(generatedMove -> move.getMetadata().containsAll(generatedMove.getMetadata())) // allow extra metadata e.g. user chosen PROMOTION_PIECE
                .findAny()
                .isEmpty();
        if (noMatchingGeneratedMoveFound) {
            throw new IllegalArgumentException("%s is not one of the generated moves %s".formatted(move, moves));
        }
    }
}
