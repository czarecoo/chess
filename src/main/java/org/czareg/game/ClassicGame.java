package org.czareg.game;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.board.ClassicBoard;
import org.czareg.move.ClassicMoveExecutor;
import org.czareg.move.ClassicMoveGenerator;
import org.czareg.move.MoveExecutor;
import org.czareg.move.MoveGenerator;
import org.czareg.move.piece.ClassicPieceMoveGeneratorFactory;
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

    private final Board board;
    private final History history;
    private final MoveGenerator moveGenerator;
    private final Order order;
    private final MoveExecutor moveExecutor;

    public ClassicGame() {
        this(
                new ClassicBoard(8, 8),
                new ClassicHistory(),
                new ClassicMoveGenerator(new ClassicPieceMoveGeneratorFactory()),
                new ClassicOrder(),
                new ClassicMoveExecutor()
        );
    }

    public ClassicGame(Board board, History history, MoveGenerator moveGenerator, Order order, MoveExecutor moveExecutor) {
        this.board = board;
        this.history = history;
        this.moveGenerator = moveGenerator;
        this.order = order;
        this.moveExecutor = moveExecutor;
    }

    @Override
    public void makeMove(Move move) {
        checkIfPlayersTurn(move);
        checkIfLegal(move);
        moveExecutor.execute(move, this);
        history.save(move);
    }

    @Override
    public boolean isUnderAttack(Position position, Player defender, Player attacker) {
        List<Move> attackMoves = moveGenerator.generate(this, attacker)
                .filter(move -> move.getEnd().equals(position))
                .filter(move -> move.getMetadata().isExactly(Metadata.Key.MOVE_TYPE, MoveType.CAPTURE))
                .filter(move -> move.getMetadata().get(Metadata.Key.CAPTURE_PIECE, Piece.class)
                        .filter(piece -> piece.getPlayer() == defender)
                        .isPresent()
                ).toList();
        log.debug("Generated {} moves that attack {} where defender {} and attacker {}", attackMoves.size(), position, defender, attacker);
        return !attackMoves.isEmpty();
    }

    private void checkIfPlayersTurn(Move move) {
        Player nowMovingPlayer = history.getLastMovingPlayer()
                .map(order::getNowMovingPlayer)
                .orElse(order.startingPlayer());
        Player requestingPlayer = move.getPiece().getPlayer();
        if (requestingPlayer != nowMovingPlayer) {
            throw new IllegalArgumentException("Now moving player %s not player %s".formatted(nowMovingPlayer, requestingPlayer));
        }
    }

    private void checkIfLegal(Move move) {
        Stream<Move> moveStream = moveGenerator.generate(this, move.getStart());
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
