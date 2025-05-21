package org.czareg.board;

import org.czareg.game.Duplicatable;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.stream.Stream;

public interface Board extends Duplicatable<Board> {

    PositionFactory getPositionFactory();

    boolean hasPiece(Position position);

    Piece getPiece(Position position);

    void placePiece(Position startPosition, Piece piece);

    void movePiece(Position startPosition, Position endPosition);

    Piece removePiece(Position position);

    Stream<PiecePosition> getAllPiecePositions(Player player);
}
