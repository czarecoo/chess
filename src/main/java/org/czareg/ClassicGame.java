package org.czareg;

import lombok.Getter;

import java.util.Set;

@Getter
class ClassicGame implements Game {

    private final Board board;
    private final History history;
    private final MoveGenerator moveGenerator;
    private final Order order;

    ClassicGame() {
        this(
                new ClassicBoard(new BoardSize(8, 8)),
                new ClassicHistory(),
                new ClassicMoveGenerator(),
                new ClassicOrder()
        );
    }

    ClassicGame(Board board, History history, MoveGenerator moveGenerator, Order order) {
        this.board = board;
        this.history = history;
        this.moveGenerator = moveGenerator;
        this.order = order;
    }

    @Override
    public void makeMove(LegalMove legalMove) {
        checkIfPlayersTurn(legalMove);
        checkIfLegal(legalMove);
        board.movePiece(legalMove.getStart(), legalMove.getEnd());
        history.save(legalMove);
    }

    private void checkIfPlayersTurn(LegalMove legalMove) {
        Player nowMovingPlayer = history.getLastMovingPlayer()
                .map(order::getNowMovingPlayer)
                .orElse(order.startingPlayer());
        Player requestingPlayer = legalMove.getPiece().getPlayer();
        if (requestingPlayer != nowMovingPlayer) {
            throw new IllegalArgumentException("Now moving player: %s not player: %s".formatted(nowMovingPlayer, requestingPlayer));
        }
    }

    private void checkIfLegal(LegalMove legalMove) {
        Set<LegalMove> legalMoves = moveGenerator.generate(this, legalMove.getStart());
        if (!legalMoves.contains(legalMove)) {
            throw new IllegalArgumentException("%s is not one of the legal moves: %s".formatted(legalMove, legalMoves));
        }
    }
}
