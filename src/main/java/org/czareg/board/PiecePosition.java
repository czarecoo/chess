package org.czareg.board;

import org.czareg.piece.Piece;
import org.czareg.position.Position;

public record PiecePosition(Piece piece, Position position) {
}
