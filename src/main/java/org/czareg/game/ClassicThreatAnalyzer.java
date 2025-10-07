package org.czareg.game;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.board.PiecePosition;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.PieceMoveGeneratorFactory;
import org.czareg.piece.King;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.position.Position;

import java.util.List;

@Slf4j
public class ClassicThreatAnalyzer implements ThreatAnalyzer {

    @Override
    public boolean isKingUnderAttack(Context context, Player player) {
        Board board = context.getBoard();

        List<PiecePosition> playerKingPiecePositions = board.getAllPiecePositions(player, King.class);

        if (playerKingPiecePositions.isEmpty()) {
            log.debug("King not found for player {}, skipping isInCheck calculations", player);
            return false;
        }
        if (playerKingPiecePositions.size() > 1) {
            throw new IllegalStateException("More than one king found for player " + player);
        }

        Position kingPosition = playerKingPiecePositions.getFirst().position();
        return isPositionUnderAttack(context, kingPosition, player);
    }

    @Override
    public boolean isPositionUnderAttack(Context context, Position position, Player player) {
        Player opponent = player.getOpponent();
        Board board = context.getBoard();

        PieceMoveGeneratorFactory pieceMoveGeneratorFactory = context.getPieceMoveGeneratorFactory();
        for (PiecePosition piecePosition : board.getAllPiecePositions(opponent)) {
            Piece opponentPiece = piecePosition.piece();
            for (PieceMoveGenerator pieceMoveGenerator : pieceMoveGeneratorFactory.getPieceMoveGenerators(opponentPiece)) {
                Position opponentPosition = piecePosition.position();
                if (pieceMoveGenerator.generate(context, opponentPiece, opponentPosition)
                        .anyMatch(move -> move.getEnd().equals(position))) {
                    return true;
                }
            }
        }
        return false;
    }
}