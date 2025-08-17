package org.czareg.move;

import org.czareg.board.Board;
import org.czareg.board.PiecePosition;
import org.czareg.game.Context;
import org.czareg.game.GeneratedMoves;
import org.czareg.game.Move;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.PieceMoveGeneratorFactory;
import org.czareg.piece.Player;

import java.util.*;
import java.util.stream.Stream;

public class PseudoLegalMoveGenerator {

    public GeneratedMoves generate(Context context) {
        Map<Player, Set<Move>> playerMoves = new EnumMap<>(Player.class);
        PieceMoveGeneratorFactory pieceMoveGeneratorFactory = context.getPieceMoveGeneratorFactory();
        Board board = context.getBoard();
        for (Player player : Player.values()) {
            Set<Move> moves = new HashSet<>();
            List<PiecePosition> piecePositions = board.getAllPiecePositions().stream()
                    .filter(piecePosition -> piecePosition.piece().getPlayer() == player)
                    .toList();
            for (PiecePosition piecePosition : piecePositions) {
                List<PieceMoveGenerator> pieceMoveGenerators = pieceMoveGeneratorFactory.getPieceMoveGenerators(piecePosition.piece());
                for (PieceMoveGenerator pieceMoveGenerator : pieceMoveGenerators) {
                    Stream<Move> moveStream = pieceMoveGenerator.generate(context, piecePosition.piece(), piecePosition.position());
                    moves.addAll(moveStream.toList());
                }
            }
            playerMoves.put(player, moves);
        }
        return new GeneratedMoves(playerMoves);
    }
}
