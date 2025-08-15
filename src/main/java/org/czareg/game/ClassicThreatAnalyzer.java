package org.czareg.game;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.board.PiecePosition;
import org.czareg.move.MoveGenerator;
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
    public boolean isUnderAttack(Context context, Position position, Player player) {
        MoveGenerator moveGenerator = context.getMoveGenerator();
        Player attacker = player.getOpponent();
        List<Move> attackMoves = moveGenerator.generate(context, attacker)
                .filter(move -> move.getEnd().equals(position))
                .filter(move -> move.getMetadata().isExactly(MOVE_TYPE, MoveType.CAPTURE))
                .filter(move -> move.getMetadata().get(CAPTURE_PIECE, Piece.class)
                        .filter(piece -> piece.getPlayer() == player)
                        .isPresent()
                ).toList();
        log.debug("Generated {} moves that attack {} where defender {} and attacker {}", attackMoves.size(), position, player, attacker);
        return !attackMoves.isEmpty();
    }

    @Override
    public boolean isInCheck(Context context, Player player) {
        Board board = context.getBoard();
        List<Position> kingPositions = board.getAllPiecePositions(player)
                .filter(piecePosition -> piecePosition.piece() instanceof King)
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
}
