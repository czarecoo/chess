package org.czareg.move.piece.king;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.game.Game;
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

import static org.czareg.game.Metadata.Key.CASTLING_ROOK_END_POSITION;
import static org.czareg.game.Metadata.Key.CASTLING_ROOK_START_POSITION;

@Slf4j
public class KingCastlingMoveGenerator implements PieceMoveGenerator, PromotionRankChecker {

    @Override
    public Stream<Move> generate(Game game, Piece piece, Position currentPosition) {
        List<Move> moves = new ArrayList<>();
        for (IndexChange captureTargetIndexChange : getEndPositionIndexChanges()) {
            generate(game, piece, currentPosition, captureTargetIndexChange).ifPresent(moves::add);
        }
        return moves.stream();
    }

    @Override
    public Optional<Move> generate(Game game, Piece piece, Position kingCurrentPosition, IndexChange kingEndPositionIndexChange) {
        log.debug("Generating move for {} at {} and {}.", piece, kingCurrentPosition, kingEndPositionIndexChange);
        Board board = game.getBoard();
        PositionFactory positionFactory = board.getPositionFactory();
        Player player = piece.getPlayer();
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
        if (!isOnPromotionRank(kingEndPosition, positionFactory, player)) {
            log.debug("Rejecting move because end {} is not on promotion rank.", kingEndPosition);
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
            IndexChange indexChange = new IndexChange(0, 1);
            Optional<Position> optionalRookEndPosition = positionFactory.create(kingCurrentPositionIndex, indexChange);
            if (optionalRookEndPosition.isEmpty()) {
                log.debug("Rejecting move because rook end position is not valid on the board ({}, {}).", kingCurrentPositionIndex, indexChange);
                return Optional.empty();
            }
            rookEndPosition = optionalRookEndPosition.get();
        } else {
            // 0-0-0
            String firstFile = positionFactory.getAllowedFileValues().getFirst();
            rookStartPosition = positionFactory.create(currentRank, firstFile);
            IndexChange indexChange = new IndexChange(0, -1);
            Optional<Position> optionalRookEndPosition = positionFactory.create(kingCurrentPositionIndex, indexChange);
            if (optionalRookEndPosition.isEmpty()) {
                log.debug("Rejecting move because rook end position is not valid on the board ({}, {}).", kingCurrentPositionIndex, indexChange);
                return Optional.empty();
            }
            rookEndPosition = optionalRookEndPosition.get();
        }

        // TODO

        Metadata metadata = new Metadata(getMoveType())
                .put(CASTLING_ROOK_START_POSITION, rookStartPosition)
                .put(CASTLING_ROOK_END_POSITION, rookEndPosition);
        Move move = new Move(piece, kingCurrentPosition, kingEndPosition, metadata);
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

//    private void tryCastling(Game game,
//                             Piece king,
//                             Position kingStart,
//                             Position kingEnd,
//                             Position rookStart,
//                             Position kingPassThrough,
//                             Position kingDestination,
//                             Position rookEnd,
//                             List<Move> moves) {
//        Board board = game.getBoard();
//        History history = game.getHistory();
//
//        if (!board.hasPiece(rookStart)) return;
//        Piece rook = board.getPiece(rookStart);
//        if (!rook.getPlayer().equals(king.getPlayer())) return;
//
//        // Check neither piece has moved
//        if (history.hasMoved(king) || history.hasMoved(rook)) return;
//
//        // Ensure path between king and rook is empty
//        List<Position> between = getPositionsBetween(kingStart, rookStart, board.getPositionFactory());
//        if (between.stream().anyMatch(board::hasPiece)) return;
//
//        // Ensure no square is under attack
//        if (game.isInCheck(king.getPlayer())) return;
//        if (game.isUnderAttack(kingPassThrough, king.getPlayer().opponent())) return;
//        if (game.isUnderAttack(kingDestination, king.getPlayer().opponent())) return;
//
//        // Create move with metadata
//        Metadata metadata = new MetadataBuilder(MoveType.CASTLING)
//                .put(CASTLING_ROOK_START_POSITION, rookStart)
//                .put(CASTLING_ROOK_END_POSITION, rookEnd)
//                .build();
//
//        moves.add(new Move(king, kingStart, kingEnd, metadata));
//    }
//
//    private List<Position> getPositionsBetween(Position start, Position end, PositionFactory factory) {
//        List<Position> between = new ArrayList<>();
//        int startFile = start.getFile();
//        int endFile = end.getFile();
//        int rank = start.getRank();
//        int min = Math.min(startFile, endFile);
//        int max = Math.max(startFile, endFile);
//        for (int file = min + 1; file < max; file++) {
//            between.add(factory.create(rank, file));
//        }
//        return between;
//    }
}
