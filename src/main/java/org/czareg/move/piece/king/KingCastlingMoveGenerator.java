package org.czareg.move.piece.king;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.*;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.shared.StartingRankChecker;
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

import static org.czareg.game.Metadata.Key.CASTLING_ROOK_END_POSITION;
import static org.czareg.game.Metadata.Key.CASTLING_ROOK_START_POSITION;

@Slf4j
public class KingCastlingMoveGenerator implements PieceMoveGenerator, StartingRankChecker {

    @Override
    public Stream<Move> generate(Context context, Piece piece, Position currentPosition) {
        List<Move> moves = new ArrayList<>();
        for (IndexChange captureTargetIndexChange : getEndPositionIndexChanges()) {
            generate(context, piece, currentPosition, captureTargetIndexChange).ifPresent(moves::add);
        }
        return moves.stream();
    }

    @Override
    public Optional<Move> generate(Context context, Piece king, Position kingCurrentPosition, IndexChange kingEndPositionIndexChange) {
        log.debug("Generating move for {} at {} and {}.", king, kingCurrentPosition, kingEndPositionIndexChange);
        Board board = context.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Player player = king.getPlayer();
        Index kingCurrentPositionIndex = positionFactory.create(kingCurrentPosition);
        Optional<Position> optionalKingEndPosition = positionFactory.create(kingCurrentPositionIndex, kingEndPositionIndexChange);
        if (optionalKingEndPosition.isEmpty()) {
            log.debug("Rejecting move because end position is not valid on the board ({}, {}).", kingCurrentPositionIndex, kingEndPositionIndexChange);
            return Optional.empty();
        }
        Position kingEndPosition = optionalKingEndPosition.get();
        if (board.hasPiece(kingEndPosition)) {
            Piece endPositionOccupyingPiece = board.getPiece(kingEndPosition);
            log.debug("Rejecting move because end {} is occupied by {}.", kingEndPosition, endPositionOccupyingPiece);
            return Optional.empty();
        }
        int currentRank = kingCurrentPosition.getRank();
        if (currentRank != kingEndPosition.getRank()) {
            log.debug("Rejecting move because end {} is on different rank than current {}.", kingEndPosition, kingCurrentPosition);
            return Optional.empty();
        }
        if (!isOnStartingRank(kingEndPosition, positionFactory, player)) {
            log.debug("Rejecting move because end {} is not on starting rank.", kingEndPosition);
            return Optional.empty();
        }
        if (kingEndPositionIndexChange.getRankChange() != 0) {
            log.debug("Rejecting move because {} rank is not 0.", kingEndPositionIndexChange);
            return Optional.empty();
        }
        if (Math.abs(kingEndPositionIndexChange.getFileChange()) != 2) {
            log.debug("Rejecting move because end {} is not exactly 2 files away from current {} ({}).", kingEndPosition, kingCurrentPosition, kingEndPositionIndexChange);
            return Optional.empty();
        }
        Position rookStartPosition;
        Position rookEndPosition;
        if (kingEndPositionIndexChange.getFileChange() > 0) {
            // O-O
            String lastFile = positionFactory.getAllowedFileValues().getLast();
            rookStartPosition = positionFactory.create(currentRank, lastFile);
            IndexChange rookEndPositionIndexChange = new IndexChange(0, 1);
            Optional<Position> optionalRookEndPosition = positionFactory.create(kingCurrentPositionIndex, rookEndPositionIndexChange);
            if (optionalRookEndPosition.isEmpty()) {
                log.debug("Rejecting move because rook end position is not valid on the board ({}, {}).", kingCurrentPositionIndex, rookEndPositionIndexChange);
                return Optional.empty();
            }
            rookEndPosition = optionalRookEndPosition.get();
        } else {
            // 0-0-0
            String firstFile = positionFactory.getAllowedFileValues().getFirst();
            rookStartPosition = positionFactory.create(currentRank, firstFile);
            IndexChange rookEndPositionIndexChange = new IndexChange(0, -1);
            Optional<Position> optionalRookEndPosition = positionFactory.create(kingCurrentPositionIndex, rookEndPositionIndexChange);
            if (optionalRookEndPosition.isEmpty()) {
                log.debug("Rejecting move because rook end position is not valid on the board ({}, {}).", kingCurrentPositionIndex, rookEndPositionIndexChange);
                return Optional.empty();
            }
            rookEndPosition = optionalRookEndPosition.get();
        }
        if (!board.hasPiece(rookStartPosition)) {
            log.debug("Rejecting move because there is no rook at start {}.", rookStartPosition);
            return Optional.empty();
        }
        Piece rook = board.getPiece(rookStartPosition);
        History history = context.getHistory();
        if (history.hasPieceMovedBefore(king) || history.hasPieceMovedBefore(rook)) {
            log.debug("Rejecting move because king or rook has moved before.");
            return Optional.empty();
        }
        Index rookStartPositionIndex = positionFactory.create(rookStartPosition);
        List<Position> positionsBetween = positionFactory.between(kingCurrentPositionIndex, rookStartPositionIndex);
        List<Position> positionsWithPiecesBetween = positionsBetween.stream().filter(board::hasPiece).toList();
        if (!positionsWithPiecesBetween.isEmpty()) {
            log.debug("Rejecting move because there are pieces between king and rook at {}.", positionsWithPiecesBetween);
            return Optional.empty();
        }
        Metadata metadata = new Metadata(getMoveType())
                .put(CASTLING_ROOK_START_POSITION, rookStartPosition)
                .put(CASTLING_ROOK_END_POSITION, rookEndPosition);
        Move move = new Move(king, kingCurrentPosition, kingEndPosition, metadata);
        // no validation for rookEndPosition (aka kingPassThroughPosition) and kingEndPosition, this is handled by KingMoveValidator
        // the reason is during move generation we cannot check for isUnderAttack because that requires move generation
        log.debug("Accepted move {}", move);
        return Optional.of(move);
    }

    private List<IndexChange> getEndPositionIndexChanges() {
        return List.of(
                new IndexChange(0, -2),
                new IndexChange(0, 2)
            );
    }

    @Override
    public MoveType getMoveType() {
        return MoveType.CASTLING;
    }
}
