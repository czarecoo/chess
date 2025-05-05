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
        List<Move> moves = new ArrayList<>();
        Player player = pawn.getPlayer();
        for (IndexChange endPositionIndexChange : getEndPositionIndexChanges(player)) {
            generate(game, pawn, currentPosition, endPositionIndexChange).ifPresent(moves::add);
        }
        return moves.stream();
    }

    @Override
    public Optional<Move> generate(Game game, Pawn pawn, Position currentPosition, IndexChange endPositionIndexChange) {
        log.debug("Generating move for {} at {} and {}.", pawn, currentPosition, endPositionIndexChange);
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Player player = pawn.getPlayer();
        Index currentPositionIndex = positionFactory.create(currentPosition);
        Optional<Position> optionalEndPosition = positionFactory.create(currentPositionIndex, endPositionIndexChange);
        if (optionalEndPosition.isEmpty()) {
            log.debug("Rejecting move because end position is not valid on the board ({}, {}).", currentPositionIndex, endPositionIndexChange);
            return Optional.empty();
        }
        Position endPosition = optionalEndPosition.get();
        if (!board.hasPiece(endPosition)) {
            log.debug("Rejecting move because target {} is empty.", endPosition);
            return Optional.empty();
        }
        Piece targetPiece = board.getPiece(endPosition);
        if (targetPiece.getPlayer() == player) {
            log.debug("Rejecting move because target {} is occupied by friendly {}.", endPosition, targetPiece);
            return Optional.empty();
        }
        Metadata metadata = new Metadata()
                .put(Metadata.Key.CAPTURE_PIECE, targetPiece)
                .put(Metadata.Key.CAPTURE_PIECE_POSITION, endPosition)
                .put(Metadata.Key.MOVE_TYPE, MoveType.PAWN_CAPTURE);
        Move move = new Move(pawn, currentPosition, endPosition, metadata);
        log.debug("Accepted move {}", move);
        return Optional.of(move);
    }

    private List<IndexChange> getEndPositionIndexChanges(Player player) {
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

    @Override
    public MoveType getMoveType() {
        return MoveType.PAWN_CAPTURE;
    }
}
