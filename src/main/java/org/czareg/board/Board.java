package org.czareg.board;

import org.czareg.piece.Piece;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

public interface Board {

    BoardSize getBoardSize();

    PositionFactory getPositionFactory();

    boolean hasPiece(Position position);

    Piece getPiece(Position position);

    void placePiece(Position startPosition, Piece piece);

    void movePiece(Position startPosition, Position endPosition);

    Piece removePiece(Position position);
}
