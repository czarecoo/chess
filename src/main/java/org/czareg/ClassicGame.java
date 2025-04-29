package org.czareg;

import lombok.Getter;

class ClassicGame implements Game {

    @Getter
    private final Board board;
    @Getter
    private final History history;

    ClassicGame() {
        this(new ClassicBoard(8, 8), new ClassicHistory());
    }

    ClassicGame(Board board, History history) {
        this.board = board;
        this.history = history;
    }

    @Override
    public void makeMove(LegalMove legalMove) {
        // TODO check who's turn it is
        board.movePiece(legalMove.start(), legalMove.end());
        history.save(legalMove);
    }
}
