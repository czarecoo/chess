package org.czareg.board;

import org.czareg.game.Duplicatable;
import org.czareg.game.hasher.ZobristHasher;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

public interface Board extends Duplicatable<Board> {

    PositionFactory getPositionFactory();

    ZobristHasher getZobristHasher();

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

    default List<PiecePosition> getAllPiecePositions(Player player, Class<? extends Piece> pieceType) {
        return getAllPiecePositions(player).stream()
                .filter(piecePosition -> piecePosition.piece().getClass() == pieceType)
                .toList();
    }

    default Map<Player, Set<Piece>> getPiecesForPlayers() {
        return getAllPiecePositions().stream()
                .map(PiecePosition::piece)
                .collect(Collectors.groupingBy(Piece::getPlayer, mapping(Function.identity(), toSet())));
    }
}
