package org.czareg;

interface Board {

    boolean hasPiece(ClassicPosition classicPosition);

    Piece getPiece(ClassicPosition classicPosition);

    void placePiece(ClassicPosition startPosition, Piece piece);

    void movePiece(ClassicPosition startClassicPosition, ClassicPosition endClassicPosition);

    Piece removePiece(ClassicPosition classicPosition);
}
