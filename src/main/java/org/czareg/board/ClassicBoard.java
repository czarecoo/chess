package org.czareg.board;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.czareg.piece.Piece;
import org.czareg.position.Index;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.Optional;

@Slf4j
public class ClassicBoard implements Board {

    @Getter
    private final BoardSize boardSize;
    private final Piece[][] board;
    @Getter
    private final PositionFactory positionFactory;

    public ClassicBoard(BoardSize boardSize) {
        this.boardSize = boardSize;
        this.positionFactory = new PositionFactory(boardSize);
        this.board = new Piece[boardSize.getRanks()][boardSize.getFiles()];
    }

    @Override
    public boolean hasPiece(Position position) {
        return get(position) != null;
    }

    @Override
    public Piece getPiece(Position position) {
        if (!hasPiece(position)) {
            throw new IllegalArgumentException("No piece at position: " + position);
        }
        return get(position);
    }

    @Override
    public void placePiece(Position startPosition, Piece piece) {
        if (hasPiece(startPosition)) {
            String message = "Position: %s already occupied by different piece".formatted(startPosition);
            throw new IllegalArgumentException(message);
        }
        find(piece).ifPresent(foundPiecePosition -> {
            String message = "Piece is already on board at position: %s".formatted(foundPiecePosition);
            throw new IllegalStateException(message);
        });
        set(piece, startPosition);
    }

    @Override
    public Piece removePiece(Position position) {
        Piece piece = getPiece(position);
        set(null, position);
        return piece;
    }

    @Override
    public void movePiece(Position startPosition, Position endPosition) {
        Piece piece = removePiece(startPosition);
        placePiece(endPosition, piece);
    }

    private Piece get(Position position) {
        Index index = positionFactory.create(position);
        return board[index.getRank()][index.getFile()];
    }

    private void set(Piece piece, Position position) {
        Index index = positionFactory.create(position);
        board[index.getRank()][index.getFile()] = piece;
    }

    private Optional<Position> find(Piece piece) {
        for (int rankIndex = 0; rankIndex < board.length; rankIndex++) {
            for (int fileIndex = 0; fileIndex < board[rankIndex].length; fileIndex++) {
                Piece foundPiece = board[rankIndex][fileIndex];
                if (piece == foundPiece) {
                    Position position = positionFactory.create(rankIndex, fileIndex);
                    return Optional.of(position);
                }
            }
        }
        return Optional.empty();
    }
}
