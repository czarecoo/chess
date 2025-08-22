package org.czareg.game.validator;

import org.czareg.board.Board;
import org.czareg.board.PiecePosition;
import org.czareg.game.Context;
import org.czareg.piece.*;
import org.czareg.position.PositionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InsufficientMaterialChecker {

    private final Map<Player, Map<Class<? extends Piece>, Long>> playerPieceCounts;
    private final Map<Player, List<Piece>> playerPieces;
    private final List<PiecePosition> allPiecePositions;
    private final PositionFactory positionFactory;

    public InsufficientMaterialChecker(Context context) {
        Board board = context.getBoard();
        positionFactory = board.getPositionFactory();
        Map<Player, Set<Piece>> piecesForPlayers = board.getPiecesForPlayers();

        allPiecePositions = board.getAllPiecePositions();

        playerPieceCounts = piecesForPlayers.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream()
                        .collect(Collectors.groupingBy(Piece::getClass, Collectors.counting()))));

        playerPieces = piecesForPlayers.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new ArrayList<>(e.getValue())));
    }

    public boolean check() {
        return checkOnlyKings() || checkKingAndMinorVsKing() || checkBishopsSameColorDraw() || checkTwoKnightsVsKing() || checkTwoBishopsSameColorVsKing();
    }

    public boolean checkOnlyKings() {
        if (playerPieces.isEmpty()) {
            return false;
        }
        return playerPieces.values()
                .stream()
                .allMatch(
                        pieces -> pieces.size() == 1 && pieces.getFirst() instanceof King
                );
    }

    public boolean checkKingAndMinorVsKing() {
        for (Player player : playerPieces.keySet()) {
            List<Piece> pieces = playerPieces.get(player);
            if (pieces.size() == 2 && (
                    playerPieceCounts.get(player).getOrDefault(Bishop.class, 0L) == 1 ||
                            playerPieceCounts.get(player).getOrDefault(Knight.class, 0L) == 1)) {

                // check opponent has only king
                for (Player opponent : playerPieces.keySet()) {
                    if (!opponent.equals(player)) {
                        List<Piece> oppPieces = playerPieces.get(opponent);
                        if (oppPieces.size() == 1 && oppPieces.getFirst() instanceof King) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean checkBishopsSameColorDraw() {
        if (playerPieces.values().stream().allMatch(p -> p.size() == 2)) {
            List<PiecePosition> bishopPositions = allPiecePositions.stream()
                    .filter(piecePosition -> piecePosition.piece() instanceof Bishop)
                    .toList();

            if (bishopPositions.size() == 2) {
                return positionFactory.isLightSquare(bishopPositions.get(0).position())
                        == positionFactory.isLightSquare(bishopPositions.get(1).position());
            }
        }
        return false;
    }

    public boolean checkTwoKnightsVsKing() {
        for (Player player : playerPieceCounts.keySet()) {
            Map<Class<? extends Piece>, Long> counts = playerPieceCounts.get(player);
            if (counts.getOrDefault(Knight.class, 0L) == 2 &&
                    counts.getOrDefault(King.class, 0L) == 1 &&
                    counts.size() == 2) { // only king + knights
                // Opponent must have only king
                for (Player opponent : playerPieceCounts.keySet()) {
                    if (opponent != player) {
                        Map<Class<? extends Piece>, Long> oppCounts = playerPieceCounts.get(opponent);
                        if (oppCounts.size() == 1 && oppCounts.getOrDefault(King.class, 0L) == 1) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean checkTwoBishopsSameColorVsKing() {
        for (Player player : playerPieces.keySet()) {
            List<Piece> pieces = playerPieces.get(player);

            if (pieces.size() == 3 && playerPieceCounts.get(player).getOrDefault(Bishop.class, 0L) == 2) {
                // Opponent must have only king
                for (Player opponent : playerPieces.keySet()) {
                    if (!opponent.equals(player)) {
                        List<Piece> oppPieces = playerPieces.get(opponent);
                        if (oppPieces.size() == 1 && oppPieces.getFirst() instanceof King) {
                            // Check bishops color
                            List<PiecePosition> bishops = allPiecePositions.stream()
                                    .filter(pp -> pp.piece() instanceof Bishop && pp.piece().getPlayer().equals(player))
                                    .toList();
                            if (bishops.size() == 2) {
                                boolean sameColor = positionFactory.isLightSquare(bishops.get(0).position()) ==
                                        positionFactory.isLightSquare(bishops.get(1).position());
                                return sameColor;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}
