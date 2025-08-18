package org.czareg.board;

import org.czareg.game.Duplicatable;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.List;

public interface Board extends Duplicatable<Board> {

    PositionFactory getPositionFactory();

    boolean hasPiece(Position position);

    Piece getPiece(Position position);

    void placePiece(Position startPosition, Piece piece);

    void movePiece(Position startPosition, Position endPosition);

    Piece removePiece(Position position);

    List<PiecePosition> getAllPiecePositions();

    default List<PiecePosition> getAllPiecePositions(Player player) {
        return getAllPiecePositions().stream()
                .filter(piecePosition -> piecePosition.piece().getPlayer() == player)
                .toList();
    }

    default List<Piece> getAllPieces(Player player, Class<? extends Piece> pieceType) {
        return getAllPiecePositions(player).stream()
                .map(PiecePosition::piece)
                .filter(pieceType::isInstance)
                .toList();
    }
}
