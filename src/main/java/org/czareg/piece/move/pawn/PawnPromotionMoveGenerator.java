package org.czareg.piece.move.pawn;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.board.BoardSize;
import org.czareg.game.Game;
import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.piece.move.PieceMoveGenerator;
import org.czareg.position.Index;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.Optional;
import java.util.stream.Stream;

import static org.czareg.game.Metadata.Key.MOVE_TYPE;

@Slf4j
public class PawnPromotionMoveGenerator implements PieceMoveGenerator {

    @Override
    public Stream<Move> generate(Game game, Piece piece, Position currentPosition) {
        Player player = piece.getPlayer();
        IndexChange endPositionIndexChange = getEndPositionIndexChange(player);
        return generate(game, piece, currentPosition, endPositionIndexChange).stream();
    }

    @Override
    public Optional<Move> generate(Game game, Piece piece, Position currentPosition, IndexChange endPositionIndexChange) {
        log.debug("Generating move for {} at {} and {}.", piece, currentPosition, endPositionIndexChange);
        Board board = game.getBoard();
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
        BoardSize boardSize = board.getBoardSize();
        Player player = piece.getPlayer();
        if (!isOnPromotionRank(endPosition, boardSize, player)) {
            log.debug("Rejecting move because end {} is not on promotion rank {}.", endPosition, boardSize);
            return Optional.empty();
        }
        Metadata metadata = new Metadata(MOVE_TYPE, getMoveType());
        Move move = new Move(piece, currentPosition, endPosition, metadata);
        log.debug("Accepted move {}.", move);
        return Optional.of(move);
    }

    private boolean isOnPromotionRank(Position position, BoardSize boardSize, Player player) {
        int rank = position.getRank();
        return switch (player) {
            case WHITE -> rank == boardSize.getRanks();
            case BLACK -> rank == 1;
        };
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
}
