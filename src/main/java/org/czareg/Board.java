package org.czareg;

import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

interface Board {

    PositionFactory getPositionFactory();

    boolean hasPiece(Position position);

    Piece getPiece(Position position);

    void placePiece(Position startPosition, Piece piece);

    void movePiece(Position startPosition, Position endPosition);

    Piece removePiece(Position position);
}
