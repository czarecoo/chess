package org.czareg.move.piece;

import org.czareg.move.piece.bishop.BishopCaptureMoveGenerator;
import org.czareg.move.piece.bishop.BishopMoveMoveGenerator;
import org.czareg.move.piece.king.KingCaptureMoveGenerator;
import org.czareg.move.piece.king.KingCastlingMoveGenerator;
import org.czareg.move.piece.king.KingMoveMoveGenerator;
import org.czareg.move.piece.knight.KnightCaptureMoveGenerator;
import org.czareg.move.piece.knight.KnightMoveMoveGenerator;
import org.czareg.move.piece.pawn.*;
import org.czareg.move.piece.queen.QueenCaptureMoveGenerator;
import org.czareg.move.piece.queen.QueenMoveMoveGenerator;
import org.czareg.move.piece.rook.RookCaptureMoveGenerator;
import org.czareg.move.piece.rook.RookMoveMoveGenerator;
import org.czareg.piece.*;

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
        moveGenerators.put(King.class, createKingMoveGenerators());
    }

    public Stream<PieceMoveGenerator> getMoveGenerator(Piece piece) {
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

    private List<PieceMoveGenerator> createKingMoveGenerators() {
        return List.of(
                new KingMoveMoveGenerator(),
                new KingCaptureMoveGenerator(),
                new KingCastlingMoveGenerator()
        );
    }
}
