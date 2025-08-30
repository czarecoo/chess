package org.czareg.game;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.board.PiecePosition;
import org.czareg.move.MoveGenerators;
import org.czareg.piece.King;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.position.Position;

import java.util.List;

import static org.czareg.game.Metadata.Key.CAPTURE_PIECE;
import static org.czareg.game.Metadata.Key.MOVE_TYPE;

@Slf4j
public class ClassicThreatAnalyzer implements ThreatAnalyzer {

    @Override
    public boolean isInCheck(Context context, Player player) {
        Board board = context.getBoard();
        List<Position> kingPositions = board.getAllPiecePositions()
                .stream()
                .filter(piecePosition -> piecePosition.piece() instanceof King)
                .filter(piecePosition -> piecePosition.piece().getPlayer() == player)
                .map(PiecePosition::position)
                .toList();
        if (kingPositions.isEmpty()) {
            log.debug("King not found for player {}, skipping isInCheck calculations", player);
            return false;
        }
        if (kingPositions.size() > 1) {
            throw new IllegalStateException("More than one king found for player " + player);
        }
        Position kingPosition = kingPositions.getFirst();
        return isUnderAttack(context, kingPosition, player);
    }

    @Override
    public boolean isUnderAttack(Context context, Position position, Player player) {
        MoveGenerators moveGenerators = context.getMoveGenerators();
        GeneratedMoves generatedMoves = moveGenerators.generateLegal(context);
        return isUnderAttack(generatedMoves, position, player);
    }

    @Override
    public boolean isUnderAttack(GeneratedMoves generatedMoves, Position position, Player player) {
        Player attacker = player.getOpponent();
        return generatedMoves
                .getMoves(attacker)
                .stream()
                .anyMatch(move -> isCapture(position, player, move));
    }

    private boolean isCapture(Position position, Player player, Move move) {
        return move.getEnd().equals(position) &&
                move.getMetadata().isExactly(MOVE_TYPE, MoveType.CAPTURE) &&
                move.getMetadata().get(CAPTURE_PIECE, Piece.class)
                        .map(piece -> piece.getPlayer() == player)
                        .orElse(false);
    }
}
