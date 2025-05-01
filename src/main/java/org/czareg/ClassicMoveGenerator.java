package org.czareg;

import org.czareg.position.Position;

import java.util.Set;
import java.util.stream.Collectors;

class ClassicMoveGenerator implements MoveGenerator {

    private final Set<PawnMoveGenerator> pawnMoveGenerators = Set.of(
            new PawnForwardMoveGenerator(),
            new PawnDoubleForwardMoveGenerator(),
            new PawnCaptureMoveGenerator()
    );

    @Override
    public Set<LegalMove> generate(Game game, Position currentPosition) {
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
