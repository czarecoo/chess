package org.czareg.move.piece.pawn;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.Context;
import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.shared.PromotionRankChecker;
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

import static org.czareg.game.Metadata.Key.CAPTURE_PIECE;
import static org.czareg.game.Metadata.Key.PROMOTION_PIECE_CLASS;

@Slf4j
public class PawnPromotionCaptureMoveGenerator implements PieceMoveGenerator, PromotionRankChecker {

    @Override
    public Stream<Move> generate(Context context, Piece piece, Position currentPosition) {
        List<Move> moves = new ArrayList<>();
        Player player = piece.getPlayer();
        for (IndexChange endPositionIndexChange : getEndPositionIndexChanges(player)) {
            for (Class<? extends Piece> promotionPieceClass : Piece.getPromotionPieceClasses()) {
                generate(context, piece, currentPosition, endPositionIndexChange)
                        .ifPresent(move -> {
                            move.getMetadata().put(PROMOTION_PIECE_CLASS, promotionPieceClass);
                            moves.add(move);
                        });
            }
        }
        return moves.stream();
    }

    @Override
    public Optional<Move> generate(Context context, Piece piece, Position currentPosition, IndexChange endPositionIndexChange) {
        log.debug("Generating move for {} at {} and {}.", piece, currentPosition, endPositionIndexChange);
        Board board = context.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
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
        Player player = piece.getPlayer();
        Piece targetPiece = board.getPiece(endPosition);
        if (targetPiece.getPlayer() == player) {
            log.debug("Rejecting move because target {} is occupied by friendly {}.", endPosition, targetPiece);
            return Optional.empty();
        }
        if (!isOnPromotionRank(endPosition, positionFactory, player)) {
            log.debug("Rejecting move because end {} is not on promotion rank.", endPosition);
            return Optional.empty();
        }
        Metadata metadata = new Metadata(getMoveType())
                .put(CAPTURE_PIECE, targetPiece);
        Move move = new Move(piece, currentPosition, endPosition, metadata);
        log.debug("Accepted move {}.", move);
        return Optional.of(move);
    }

    private List<IndexChange> getEndPositionIndexChanges(Player player) {
        return switch (player) {
            case WHITE -> List.of(
                    new IndexChange(-1, 1),
                    new IndexChange(1, 1)
            );
            case BLACK -> List.of(
                    new IndexChange(-1, -1),
                    new IndexChange(1, -1)
            );
        };
    }

    @Override
    public MoveType getMoveType() {
        return MoveType.PROMOTION_CAPTURE;
    }
}
