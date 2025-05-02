package org.czareg.piece.move;

import org.czareg.board.Board;
import org.czareg.game.Game;
import org.czareg.game.Move;
import org.czareg.piece.Pawn;
import org.czareg.piece.Piece;
import org.czareg.piece.move.pawn.PawnCaptureMoveGenerator;
import org.czareg.piece.move.pawn.PawnDoubleForwardMoveGenerator;
import org.czareg.piece.move.pawn.PawnForwardMoveGenerator;
import org.czareg.piece.move.pawn.PawnMoveGenerator;
import org.czareg.position.Position;

import java.util.Set;
import java.util.stream.Collectors;

public class ClassicMoveGenerator implements MoveGenerator {

    private final Set<PawnMoveGenerator> pawnMoveGenerators = Set.of(
            new PawnForwardMoveGenerator(),
            new PawnDoubleForwardMoveGenerator(),
            new PawnCaptureMoveGenerator()
    );

    @Override
    public Set<Move> generate(Game game, Position currentPosition) {
        Board board = game.getBoard();
        if (!board.hasPiece(currentPosition)) {
            return Set.of();
        }
        Piece piece = board.getPiece(currentPosition);
        return switch (piece) {
            case Pawn pawn -> pawnMoveGenerators.stream()
                    .flatMap(gen -> gen.generate(game, pawn, currentPosition).stream())
                    .collect(Collectors.toSet());
        };
    }
}
