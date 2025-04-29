package org.czareg;

import lombok.Getter;

import java.util.List;
import java.util.Set;

import static org.czareg.Player.BLACK;
import static org.czareg.Player.WHITE;

class ClassicGame implements Game {

    @Getter
    private final Board board;
    @Getter
    private final History history;
    @Getter
    private final MoveGenerator moveGenerator;
    private final List<Player> movingOrder;

    ClassicGame() {
        this(
                new ClassicBoard(8, 8),
                new ClassicHistory(),
                new ClassicMoveGenerator(),
                List.of(WHITE, BLACK)
        );
    }

    ClassicGame(Board board, History history, MoveGenerator moveGenerator, List<Player> movingOrder) {
        this.board = board;
        this.history = history;
        this.moveGenerator = moveGenerator;
        this.movingOrder = movingOrder;
    }

    @Override
    public void makeMove(LegalMove legalMove) {
        checkIfPlayersTurn(legalMove);
        checkIfLegal(legalMove);
        board.movePiece(legalMove.start(), legalMove.end());
        history.save(legalMove);
    }

    private void checkIfPlayersTurn(LegalMove legalMove) {
        Player nowMoving = history.getLastMovingPlayer()
                .map(movingOrder::lastIndexOf)
                .map(i -> i + 1)
                .map(i -> i >= movingOrder.size() ? 0 : i)
                .map(movingOrder::get)
                .orElse(WHITE);
        Player requestingPlayer = legalMove.piece().getPlayer();
        if (requestingPlayer != nowMoving) {
            throw new IllegalArgumentException("Now moving: %s not %s".formatted(nowMoving, requestingPlayer));
        }
    }

    private void checkIfLegal(LegalMove legalMove) {
        Set<LegalMove> legalMoves = moveGenerator.generate(this, legalMove.start());
        if (!legalMoves.contains(legalMove)) {
            throw new IllegalArgumentException("%s is not on the one of the legal moves: %s".formatted(legalMove, legalMoves));
        }
    }
}
