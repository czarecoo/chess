package org.czareg.piece.move;

import org.czareg.board.Board;
import org.czareg.game.Game;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.piece.Pawn;
import org.czareg.piece.Piece;
import org.czareg.piece.Queen;
import org.czareg.piece.move.pawn.*;
import org.czareg.piece.move.queen.QueenCaptureMoveGenerator;
import org.czareg.piece.move.queen.QueenMoveMoveGenerator;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class ClassicMoveGenerator implements MoveGenerator {

    private final Set<PawnMoveGenerator> pawnMoveGenerators = Set.of(
            new PawnForwardMoveGenerator(),
            new PawnDoubleForwardMoveGenerator(),
            new PawnCaptureMoveGenerator(),
            new PawnEnPassantMoveGenerator(),
            new PawnPromotionMoveGenerator()
    );

    private final Set<QueenMoveGenerator> queenMoveGenerators = Set.of(
            new QueenMoveMoveGenerator(),
            new QueenCaptureMoveGenerator()
    );

    @Override
    public Stream<Move> generate(Game game, Position currentPosition) {
        Board board = game.getBoard();
        if (!board.hasPiece(currentPosition)) {
            return Stream.empty();
        }
        Piece piece = board.getPiece(currentPosition);
        return switch (piece) {
            case Pawn pawn -> pawnMoveGenerators.stream()
                    .flatMap(gen -> gen.generate(game, pawn, currentPosition));
            case Queen queen -> queenMoveGenerators.stream()
                    .flatMap(gen -> gen.generate(game, queen, currentPosition));
        };
    }

    @Override
    public Optional<Move> generate(Game game, Position currentPosition, Position endPosition, MoveType moveType) {
        Board board = game.getBoard();
        if (!board.hasPiece(currentPosition)) {
            return Optional.empty();
        }
        PositionFactory positionFactory = board.getPositionFactory();
        IndexChange indexChange = positionFactory.create(currentPosition, endPosition);
        Piece piece = board.getPiece(currentPosition);
        return switch (piece) {
            case Pawn pawn -> pawnMoveGenerators.stream()
                    .filter(gen -> gen.getMoveType() == moveType)
                    .findFirst()
                    .orElseThrow()
                    .generate(game, pawn, currentPosition, indexChange);
            case Queen queen -> queenMoveGenerators.stream()
                    .filter(gen -> gen.getMoveType() == moveType)
                    .findFirst()
                    .orElseThrow()
                    .generate(game, queen, currentPosition, indexChange);
        };
    }
}
