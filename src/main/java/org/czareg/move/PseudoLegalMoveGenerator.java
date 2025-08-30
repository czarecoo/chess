package org.czareg.move;

import org.czareg.board.Board;
import org.czareg.board.PiecePosition;
import org.czareg.game.Context;
import org.czareg.game.GeneratedMoves;
import org.czareg.game.Move;
import org.czareg.move.piece.PieceMoveGeneratorFactory;
import org.czareg.piece.Player;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PseudoLegalMoveGenerator implements MoveGenerator {

    @Override
    public GeneratedMoves generate(Context context) {
        Map<Player, Set<Move>> playerMoves = new EnumMap<>(Player.class);
        for (Player player : Player.values()) {
            Set<Move> moves = generateMovesForPlayer(context, player);
            playerMoves.put(player, moves);
        }
        return new GeneratedMoves(playerMoves);
    }

    private Set<Move> generateMovesForPlayer(Context context, Player player) {
        Board board = context.getBoard();
        return board.getAllPiecePositions().stream()
                .filter(piecePosition -> piecePosition.piece().getPlayer() == player)
                .flatMap(piecePosition -> generateMovesForPiece(context, piecePosition))
                .collect(Collectors.toSet());
    }

    private Stream<Move> generateMovesForPiece(Context context, PiecePosition piecePosition) {
        PieceMoveGeneratorFactory factory = context.getPieceMoveGeneratorFactory();
        return factory.getPieceMoveGenerators(piecePosition.piece()).stream()
                .flatMap(generator -> generator.generate(
                        context,
                        piecePosition.piece(),
                        piecePosition.position()
                ));
    }
}
