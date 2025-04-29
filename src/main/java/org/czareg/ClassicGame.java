package org.czareg;

import lombok.Getter;

class ClassicGame implements Game {

    @Getter
    private final Board board;
    @Getter
    private final History history;
    @Getter
    private final MoveGenerator moveGenerator;

    ClassicGame() {
        this(new ClassicBoard(8, 8), new ClassicHistory(), new ClassicMoveGenerator());
    }

    ClassicGame(Board board, History history, MoveGenerator moveGenerator) {
        this.board = board;
        this.history = history;
        this.moveGenerator = moveGenerator;
    }

    @Override
    public void makeMove(LegalMove legalMove) {
        // TODO check who's turn it is
        board.movePiece(legalMove.start(), legalMove.end());
        history.save(legalMove);
    }
}
