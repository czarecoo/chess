package org.czareg.piece.move;

import org.czareg.board.Board;
import org.czareg.game.Game;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.piece.*;
import org.czareg.piece.move.bishop.BishopCaptureMoveGenerator;
import org.czareg.piece.move.bishop.BishopMoveMoveGenerator;
import org.czareg.piece.move.pawn.*;
import org.czareg.piece.move.queen.QueenCaptureMoveGenerator;
import org.czareg.piece.move.queen.QueenMoveMoveGenerator;
import org.czareg.piece.move.rook.RookCaptureMoveGenerator;
import org.czareg.piece.move.rook.RookMoveMoveGenerator;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ClassicMoveGenerator implements MoveGenerator {

    private final Map<Class<? extends Piece>, List<PieceMoveGenerator>> moveGenerators;

    public ClassicMoveGenerator() {
        moveGenerators = new HashMap<>();
        moveGenerators.put(Pawn.class, List.of(
                new PawnForwardMoveGenerator(),
                new PawnCaptureMoveGenerator(),
                new PawnPromotionMoveGenerator(),
                new PawnDoubleForwardMoveGenerator(),
                new PawnEnPassantMoveGenerator()
        ));
        moveGenerators.put(Queen.class, List.of(
                new QueenMoveMoveGenerator(),
                new QueenCaptureMoveGenerator()
        ));
        moveGenerators.put(Rook.class, List.of(
                new RookMoveMoveGenerator(),
                new RookCaptureMoveGenerator()
        ));
        moveGenerators.put(Bishop.class, List.of(
                new BishopMoveMoveGenerator(),
                new BishopCaptureMoveGenerator()
        ));
    }

    @Override
    public Stream<Move> generate(Game game, Position currentPosition) {
        Board board = game.getBoard();
        if (!board.hasPiece(currentPosition)) {
            return Stream.empty();
        }
        Piece piece = board.getPiece(currentPosition);
        return getStreamOfGeneratorsFor(piece)
                .flatMap(gen -> gen.generate(game, piece, currentPosition));
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
        return getStreamOfGeneratorsFor(piece)
                .filter(gen -> gen.getMoveType() == moveType)
                .findFirst()
                .orElseThrow()
                .generate(game, piece, currentPosition, indexChange);
    }

    private Stream<PieceMoveGenerator> getStreamOfGeneratorsFor(Piece piece) {
        return moveGenerators.getOrDefault(piece.getClass(), List.of()).stream();
    }
}
