package org.czareg;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
class ClassicBoard implements Board {

    private static final int RANKS = 8;
    private static final int FILES = 8;
    private final Piece[][] board;

    ClassicBoard() {
        this.board = new Piece[RANKS][FILES];
    }

    @Override
    public boolean hasPiece(ClassicPosition classicPosition) {
        return get(classicPosition) != null;
    }

    @Override
    public Piece getPiece(ClassicPosition classicPosition) {
        if (!hasPiece(classicPosition)) {
            throw new IllegalArgumentException("No piece at position: " + classicPosition);
        }
        return get(classicPosition);
    }

    @Override
    public void placePiece(ClassicPosition startPosition, Piece piece) {
        if (hasPiece(startPosition)) {
            String message = "Position: %s already occupied by different piece".formatted(startPosition);
            throw new IllegalArgumentException(message);
        }
        find(piece).ifPresent(classicPosition -> {
            String message = "Piece is already on board at position: %s".formatted(classicPosition);
            throw new IllegalStateException(message);
        });
        set(piece, startPosition);
    }

    @Override
    public Piece removePiece(ClassicPosition classicPosition) {
        Piece piece = getPiece(classicPosition);
        set(null, classicPosition);
        return piece;
    }

    @Override
    public void movePiece(ClassicPosition startClassicPosition, ClassicPosition endClassicPosition) {
        Piece piece = removePiece(startClassicPosition);
        placePiece(endClassicPosition, piece);
    }

    private Piece get(ClassicPosition classicPosition) {
        IndexPosition indexPosition = classicPosition.toIndexPosition();
        return board[indexPosition.rankIndex()][indexPosition.fileIndex()];
    }

    private void set(Piece piece, ClassicPosition classicPosition) {
        IndexPosition indexPosition = classicPosition.toIndexPosition();
        board[indexPosition.rankIndex()][indexPosition.fileIndex()] = piece;
    }

    private Optional<ClassicPosition> find(Piece piece) {
        for (int ranks = 0; ranks < RANKS; ranks++) {
            for (int files = 0; files < FILES; files++) {
                Piece foundPiece = board[ranks][files];
                if (piece == foundPiece) {
                    ClassicPosition position = new IndexPosition(ranks, files).toPosition();
                    return Optional.of(position);
                }
            }
        }
        return Optional.empty();
    }
}
