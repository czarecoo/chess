package org.czareg.move;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.czareg.board.Board;
import org.czareg.game.Context;
import org.czareg.game.Move;
import org.czareg.move.piece.PieceMoveGeneratorFactory;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.position.Position;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public class ClassicMoveGenerator implements MoveGenerator {

    @Override
    public Stream<Move> generate(Context context, Position currentPosition) {
        Board board = context.getBoard();
        if (!board.hasPiece(currentPosition)) {
            return Stream.empty();
        }
        Piece piece = board.getPiece(currentPosition);
        PieceMoveGeneratorFactory pieceMoveGeneratorFactory = context.getPieceMoveGeneratorFactory();
        return pieceMoveGeneratorFactory.getPieceMoveGenerators(piece)
                .flatMap(pieceMoveGenerator -> pieceMoveGenerator.generate(context, piece, currentPosition));
    }

    @Override
    public Stream<Move> generate(Context context, Player attacker) {
        Board board = context.getBoard();
        PieceMoveGeneratorFactory pieceMoveGeneratorFactory = context.getPieceMoveGeneratorFactory();
        return board.getAllPiecePositions(attacker)
                .flatMap(piecePosition -> pieceMoveGeneratorFactory.getPieceMoveGenerators(piecePosition.piece())
                        .flatMap(pieceMoveGenerator -> pieceMoveGenerator.generate(context, piecePosition.piece(), piecePosition.position())));
    }
}
