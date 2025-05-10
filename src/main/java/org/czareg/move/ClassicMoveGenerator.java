package org.czareg.move;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.czareg.board.Board;
import org.czareg.game.Game;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.move.piece.PieceMoveGeneratorFactory;
import org.czareg.piece.Piece;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public class ClassicMoveGenerator implements MoveGenerator {

    private final PieceMoveGeneratorFactory pieceMoveGeneratorFactory;

    @Override
    public Stream<Move> generate(Game game, Position currentPosition) {
        Board board = game.getBoard();
        if (!board.hasPiece(currentPosition)) {
            return Stream.empty();
        }
        Piece piece = board.getPiece(currentPosition);
        return pieceMoveGeneratorFactory.getMoveGenerators(piece)
                .flatMap(pieceMoveGenerator -> pieceMoveGenerator.generate(game, piece, currentPosition));
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
        return pieceMoveGeneratorFactory.getMoveGenerators(piece)
                .filter(pieceMoveGenerator -> pieceMoveGenerator.getMoveType() == moveType)
                .findFirst()
                .orElseThrow()
                .generate(game, piece, currentPosition, indexChange);
    }
}
