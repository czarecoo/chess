package org.czareg.piece.move;

import org.czareg.piece.*;
import org.czareg.piece.move.bishop.BishopCaptureMoveGenerator;
import org.czareg.piece.move.bishop.BishopMoveMoveGenerator;
import org.czareg.piece.move.knight.KnightCaptureMoveGenerator;
import org.czareg.piece.move.knight.KnightMoveMoveGenerator;
import org.czareg.piece.move.pawn.*;
import org.czareg.piece.move.queen.QueenCaptureMoveGenerator;
import org.czareg.piece.move.queen.QueenMoveMoveGenerator;
import org.czareg.piece.move.rook.RookCaptureMoveGenerator;
import org.czareg.piece.move.rook.RookMoveMoveGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ClassicPieceMoveGeneratorFactory implements PieceMoveGeneratorFactory {

    private final Map<Class<? extends Piece>, List<PieceMoveGenerator>> moveGenerators;

    public ClassicPieceMoveGeneratorFactory() {
        moveGenerators = new HashMap<>();
        moveGenerators.put(Pawn.class, createPawnMoveGenerators());
        moveGenerators.put(Knight.class, createKnightMoveGenerators());
        moveGenerators.put(Bishop.class, createBishopMoveGenerators());
        moveGenerators.put(Rook.class, createRookMoveGenerators());
        moveGenerators.put(Queen.class, createQueenMoveGenerators());
    }

    public Stream<PieceMoveGenerator> getMoveGenerators(Piece piece) {
        return moveGenerators.getOrDefault(piece.getClass(), List.of()).stream();
    }

    private List<PieceMoveGenerator> createPawnMoveGenerators() {
        return List.of(
                new PawnForwardMoveGenerator(),
                new PawnCaptureMoveGenerator(),
                new PawnPromotionMoveGenerator(),
                new PawnDoubleForwardMoveGenerator(),
                new PawnEnPassantMoveGenerator()
        );
    }

    private List<PieceMoveGenerator> createKnightMoveGenerators() {
        return List.of(
                new KnightMoveMoveGenerator(),
                new KnightCaptureMoveGenerator()
        );
    }

    private List<PieceMoveGenerator> createBishopMoveGenerators() {
        return List.of(
                new BishopMoveMoveGenerator(),
                new BishopCaptureMoveGenerator()
        );
    }

    private List<PieceMoveGenerator> createRookMoveGenerators() {
        return List.of(
                new RookMoveMoveGenerator(),
                new RookCaptureMoveGenerator()
        );
    }

    private List<PieceMoveGenerator> createQueenMoveGenerators() {
        return List.of(
                new QueenMoveMoveGenerator(),
                new QueenCaptureMoveGenerator()
        );
    }
}
