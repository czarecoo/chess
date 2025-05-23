package org.czareg.move.piece.pawn;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.Context;
import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.shared.PromotionRankChecker;
import org.czareg.piece.*;
import org.czareg.position.Index;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.czareg.game.Metadata.Key.PROMOTION_PIECE;

@Slf4j
public class PawnPromotionMoveGenerator implements PieceMoveGenerator, PromotionRankChecker {

    @Override
    public Stream<Move> generate(Context context, Piece piece, Position currentPosition) {
        List<Move> moves = new ArrayList<>();
        Player player = piece.getPlayer();
        IndexChange endPositionIndexChange = getEndPositionIndexChange(player);
        for (Piece promotionPiece : getPossiblePromotionPieces(player)) {
            generate(context, piece, currentPosition, endPositionIndexChange)
                    .ifPresent(move -> {
                        move.getMetadata().put(PROMOTION_PIECE, promotionPiece);
                        moves.add(move);
                    });
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
        if (board.hasPiece(endPosition)) {
            Piece endPositionOccupyingPiece = board.getPiece(endPosition);
            log.debug("Rejecting move because end {} is occupied by {}.", endPosition, endPositionOccupyingPiece);
            return Optional.empty();
        }
        Player player = piece.getPlayer();
        if (!isOnPromotionRank(endPosition, positionFactory, player)) {
            log.debug("Rejecting move because end {} is not on promotion rank.", endPosition);
            return Optional.empty();
        }
        Metadata metadata = new Metadata(getMoveType());
        Move move = new Move(piece, currentPosition, endPosition, metadata);
        log.debug("Accepted move {}.", move);
        return Optional.of(move);
    }

    private IndexChange getEndPositionIndexChange(Player player) {
        return switch (player) {
            case WHITE -> new IndexChange(1, 0);
            case BLACK -> new IndexChange(-1, 0);
        };
    }

    @Override
    public MoveType getMoveType() {
        return MoveType.PROMOTION;
    }

    private List<Piece> getPossiblePromotionPieces(Player player) {
        return List.of(new Knight(player), new Bishop(player), new Rook(player), new Queen(player));
    }
}
