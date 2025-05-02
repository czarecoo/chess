package org.czareg.game;

import lombok.Getter;
import org.czareg.board.Board;
import org.czareg.board.BoardSize;
import org.czareg.board.ClassicBoard;
import org.czareg.piece.Player;
import org.czareg.piece.move.ClassicMoveGenerator;
import org.czareg.piece.move.ClassicSpecialMoveExecutor;
import org.czareg.piece.move.MoveGenerator;
import org.czareg.piece.move.SpecialMoveExecutor;

import java.util.Set;

@Getter
public class ClassicGame implements Game {

    private final Board board;
    private final History history;
    private final MoveGenerator moveGenerator;
    private final Order order;
    private final SpecialMoveExecutor specialMoveExecutor;

    public ClassicGame() {
        this(
                new ClassicBoard(new BoardSize(8, 8)),
                new ClassicHistory(),
                new ClassicMoveGenerator(),
                new ClassicOrder(),
                new ClassicSpecialMoveExecutor()
        );
    }

    public ClassicGame(Board board, History history, MoveGenerator moveGenerator, Order order, SpecialMoveExecutor specialMoveExecutor) {
        this.board = board;
        this.history = history;
        this.moveGenerator = moveGenerator;
        this.order = order;
        this.specialMoveExecutor = specialMoveExecutor;
    }

    @Override
    public void makeMove(Move move) {
        checkIfPlayersTurn(move);
        checkIfLegal(move);
        make(move);
        history.save(move);
    }

    private void make(Move move) {
        if (specialMoveExecutor.isSpecialMove(move)) {
            specialMoveExecutor.execute(move, this);
        } else {
            board.movePiece(move.getStart(), move.getEnd());
        }
    }

    private void checkIfPlayersTurn(Move move) {
        Player nowMovingPlayer = history.getLastMovingPlayer()
                .map(order::getNowMovingPlayer)
                .orElse(order.startingPlayer());
        Player requestingPlayer = move.getPiece().getPlayer();
        if (requestingPlayer != nowMovingPlayer) {
            throw new IllegalArgumentException("Now moving player: %s not player: %s".formatted(nowMovingPlayer, requestingPlayer));
        }
    }

    private void checkIfLegal(Move move) {
        Set<Move> moves = moveGenerator.generate(this, move.getStart());
        if (!moves.contains(move)) {
            throw new IllegalArgumentException("%s is not one of the legal moves: %s".formatted(move, moves));
        }
    }
}
