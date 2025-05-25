package org.czareg.board;

import org.czareg.piece.*;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.Map;

public class SystemOutBoardStringifier implements BoardStringifier {

    private final Map<Player, Map<Class<? extends Piece>, String>> pieceSymbols = Map.of(
            Player.WHITE, Map.of(
                    Pawn.class, "♙",
                    Knight.class, "♘",
                    Bishop.class, "♗",
                    Rook.class, "♖",
                    Queen.class, "♕",
                    King.class, "♔"
            ),
            Player.BLACK, Map.of(
                    Pawn.class, "♟",
                    Knight.class, "♞",
                    Bishop.class, "♝",
                    Rook.class, "♜",
                    Queen.class, "♛",
                    King.class, "♚"
            )
    );

    @Override
    public String stringify(Board board) {
        PositionFactory positionFactory = board.getPositionFactory();
        int maxRank = positionFactory.getMaxRank();
        int maxFile = positionFactory.getMaxFile();
        StringBuilder boardStringBuilder = new StringBuilder();
        for (int rankIndex = maxRank - 1; rankIndex >= 0; rankIndex--) {
            for (int fileIndex = 0; fileIndex < maxFile; fileIndex++) {
                Position position = positionFactory.create(rankIndex, fileIndex);
                if (board.hasPiece(position)) {
                    Piece piece = board.getPiece(position);
                    Player player = piece.getPlayer();
                    Class<? extends Piece> pieceClass = piece.getClass();
                    String symbol = pieceSymbols
                            .getOrDefault(player, Map.of())
                            .getOrDefault(pieceClass, "?");
                    boardStringBuilder.append(symbol);
                } else {
                    boardStringBuilder.append("　"); // U+3000 ideographic space to match unicode pieces
                }
                boardStringBuilder.append(" ");
            }
            boardStringBuilder.append("\n");
        }
        return boardStringBuilder.toString();
    }
}
