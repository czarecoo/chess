package org.czareg.piece.move.pawn;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.Game;
import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.piece.Pawn;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.position.Index;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class PawnCaptureMoveGenerator implements PawnMoveGenerator {

    @Override
    public Stream<Move> generate(Game game, Pawn pawn, Position currentPosition) {
        log.debug("Generating moves for {} at {}.", pawn, currentPosition);
        List<Move> moves = new ArrayList<>();
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Player player = pawn.getPlayer();
        Index currentPositionIndex = positionFactory.create(currentPosition);

        for (IndexChange captureTargetIndexChange : getCaptureTargetIndexChanges(player)) {
            log.debug("Generating move for {}", captureTargetIndexChange);
            Optional<Position> optionalTargetPosition = positionFactory.create(currentPositionIndex, captureTargetIndexChange);
            if (optionalTargetPosition.isEmpty()) {
                log.debug("Rejecting move because target position is not valid on the board ({}, {}).", currentPositionIndex, captureTargetIndexChange);
                continue;
            }
            Position targetPosition = optionalTargetPosition.get();
            if (!board.hasPiece(targetPosition)) {
                log.debug("Rejecting move because target {} is empty.", targetPosition);
                continue;
            }
            Piece targetPiece = board.getPiece(targetPosition);
            if (targetPiece.getPlayer() == player) {
                log.debug("Rejecting move because target {} is occupied by friendly {}.", targetPosition, targetPiece);
                continue;
            }
            Metadata metadata = new Metadata();
            metadata.put(Metadata.Key.CAPTURED_PIECE, targetPiece);
            metadata.put(Metadata.Key.CAPTURED_PIECE_POSITION, targetPosition);
            metadata.put(Metadata.Key.MOVE_TYPE, MoveType.PAWN_CAPTURE);
            Move move = new Move(pawn, currentPosition, targetPosition, metadata);
            moves.add(move);
            log.debug("Accepted move: {}", move);
        }
        return moves.stream();
    }

    private List<IndexChange> getCaptureTargetIndexChanges(Player player) {
        return switch (player) {
            case WHITE -> List.of(
                    new IndexChange(1, -1),
                    new IndexChange(1, 1)
            );
            case BLACK -> List.of(
                    new IndexChange(-1, -1),
                    new IndexChange(-1, 1)
            );
        };
    }
}
