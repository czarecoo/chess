package org.czareg.game;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.board.PiecePosition;
import org.czareg.move.MoveExecutor;
import org.czareg.move.MoveGenerator;
import org.czareg.piece.King;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.position.Position;

import java.util.List;

@Slf4j
@Getter
public class ClassicGame implements Game {

    @Override
    public void makeMove(Context context, Move move) {
        PlayerTurnValidator playerTurnValidator = context.getPlayerTurnValidator();
        Player player = move.getPiece().getPlayer();
        playerTurnValidator.validate(context, player);

        MoveLegalityValidator moveLegalityValidator = context.getMoveLegalityValidator();
        moveLegalityValidator.validate(context, move);

        MoveExecutor moveExecutor = context.getMoveExecutor();
        moveExecutor.execute(context, move);

        History history = context.getHistory();
        history.save(move);
    }

    @Override
    public boolean isUnderAttack(Context context, Position position, Player defender, Player attacker) {
        MoveGenerator moveGenerator = context.getMoveGenerator();
        List<Move> attackMoves = moveGenerator.generate(context, attacker)
                .filter(move -> move.getEnd().equals(position))
                .filter(move -> move.getMetadata().isExactly(Metadata.Key.MOVE_TYPE, MoveType.CAPTURE))
                .filter(move -> move.getMetadata().get(Metadata.Key.CAPTURE_PIECE, Piece.class)
                        .filter(piece -> piece.getPlayer() == defender)
                        .isPresent()
                ).toList();
        log.debug("Generated {} moves that attack {} where defender {} and attacker {}", attackMoves.size(), position, defender, attacker);
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
        Player opponent = player.getOpponent();
        return isUnderAttack(context, kingPosition, player, opponent);
    }
}
