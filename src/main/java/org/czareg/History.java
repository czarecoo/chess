package org.czareg;

interface History {

    boolean hasPieceMovedBefore(Piece piece);

    void save(LegalMove legalMove);
}
