package org.czareg.game;

import lombok.Getter;
import org.czareg.board.Board;
import org.czareg.board.BoardSize;
import org.czareg.board.ClassicBoard;
import org.czareg.piece.Player;
import org.czareg.piece.move.ClassicMoveExecutor;
import org.czareg.piece.move.ClassicMoveGenerator;
import org.czareg.piece.move.MoveExecutor;
import org.czareg.piece.move.MoveGenerator;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class ClassicGame implements Game {

    private final Board board;
    private final History history;
    private final MoveGenerator moveGenerator;
    private final Order order;
    private final MoveExecutor moveExecutor;

    public ClassicGame() {
        this(
                new ClassicBoard(new BoardSize(8, 8)),
                new ClassicHistory(),
                new ClassicMoveGenerator(),
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
        if (!moves.contains(move)) {
            throw new IllegalArgumentException("%s is not one of the legal moves %s".formatted(move, moves));
        }
    }
}
