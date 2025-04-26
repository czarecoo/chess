package org.czareg;

import java.util.Optional;

interface Board {

    boolean isValid(IndexPosition indexPosition);

    Position getPositionOrThrow(IndexPosition indexPosition);

    default Optional<Position> getPosition(IndexPosition indexPosition) {
        return isValid(indexPosition) ? Optional.of(getPositionOrThrow(indexPosition)) : Optional.empty();
    }

    boolean hasPiece(Position position);

    Piece getPieceOrThrow(Position position);

    default Optional<Piece> getPiece(Position position) {
        return hasPiece(position) ? Optional.of(getPieceOrThrow(position)) : Optional.empty();
    }

    void placePiece(Piece piece, Position position);

    void removeAllPieces();
}
