package org.czareg.piece.move;

import org.czareg.board.Board;
import org.czareg.game.Game;
import org.czareg.game.Move;
import org.czareg.piece.Pawn;
import org.czareg.piece.Piece;
import org.czareg.piece.move.pawn.*;
import org.czareg.position.Position;

import java.util.Set;
import java.util.stream.Stream;

public class ClassicMoveGenerator implements MoveGenerator {

    private final Set<PawnMoveGenerator> pawnMoveGenerators = Set.of(
            new PawnForwardMoveGenerator(),
            new PawnDoubleForwardMoveGenerator(),
            new PawnCaptureMoveGenerator(),
            new PawnEnPassantMoveGenerator()
    );

    @Override
    public Stream<Move> generate(Game game, Position currentPosition) {
        Board board = game.getBoard();
        if (!board.hasPiece(currentPosition)) {
            return Stream.empty();
        }
        Piece piece = board.getPiece(currentPosition);
        return switch (piece) {
            case Pawn pawn -> pawnMoveGenerators.stream().flatMap(gen -> gen.generate(game, pawn, currentPosition));
        };
    }
}
