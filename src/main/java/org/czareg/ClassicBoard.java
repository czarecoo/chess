package org.czareg;

class ClassicBoard implements Board {

    private static final int RANKS = 8;
    private static final int FILES = 8;

    private Piece[][] board;

    ClassicBoard() {
        this.board = new Piece[RANKS][FILES];
    }

    @Override
    public boolean isValid(IndexPosition indexPosition) {
        int rankIndex = indexPosition.rankIndex();
        int fileIndex = indexPosition.fileIndex();
        return rankIndex >= 0 && fileIndex >= 0 && rankIndex < RANKS && fileIndex < FILES;
    }

    @Override
    public Position getPositionOrThrow(IndexPosition indexPosition) {
        int rankIndex = indexPosition.rankIndex();
        int fileIndex = indexPosition.fileIndex();
        return new Position(Rank.fromIndex(rankIndex), File.fromIndex(fileIndex));
    }

    @Override
    public boolean hasPiece(Position position) {
        int rankIndex = position.rank().getIndex();
        int fileIndex = position.file().getIndex();
        return board[rankIndex][fileIndex] != null;
    }

    @Override
    public Piece getPieceOrThrow(Position position) {
        if (!hasPiece(position)) {
            throw new IllegalArgumentException("No piece at position: " + position);
        }
        int rankIndex = position.rank().getIndex();
        int fileIndex = position.file().getIndex();
        return board[rankIndex][fileIndex];
    }

    @Override
    public void placePiece(Piece piece, Position position) {
        int rankIndex = position.rank().getIndex();
        int fileIndex = position.file().getIndex();
        board[rankIndex][fileIndex] = piece;
    }

    @Override
    public void removeAllPieces() {
        this.board = new Piece[RANKS][FILES];
    }
}
