package org.czareg.game;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.board.PiecePosition;
import org.czareg.move.MoveGenerators;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.piece.King;
import org.czareg.piece.Player;
import org.czareg.position.Position;

import java.util.List;

@Slf4j
public class ClassicThreatAnalyzer implements ThreatAnalyzer {

    @Override
    public boolean isInCheck(Context context, Player player) {
        Board board = context.getBoard();

        // Find the king position for the given player
        List<Position> kingPositions = board.getAllPiecePositions()
                .stream()
                .filter(pp -> pp.piece() instanceof King && pp.piece().getPlayer() == player)
                .map(PiecePosition::position)
                .toList();

        if (kingPositions.isEmpty()) {
            log.debug("King not found for player {}, skipping isInCheck calculations", player);
            return false;
        }
        if (kingPositions.size() > 1) {
            throw new IllegalStateException("More than one king found for player " + player);
        }

        Position kingPosition = kingPositions.getFirst();
        return isUnderAttack(context, kingPosition, player);
    }

    @Override
    public boolean isUnderAttack(Context context, Position position, Player player) {
        // Use pseudo-legal moves instead of legal ones to avoid recursion
        MoveGenerators moveGenerators = context.getMoveGenerators();
        GeneratedMoves generatedMoves = moveGenerators.generatePseudoLegal(context);
        return isUnderAttack(generatedMoves, position, player);
    }

    @Override
    public boolean isUnderAttack(GeneratedMoves generatedMoves, Position position, Player player) {
        // Check if *any* opponent move attacks this position
        return generatedMoves.getMoves()
                .stream()
                .anyMatch(move -> move.getEnd().equals(position));
    }

    @Override
    public boolean isKingUnderAttack(Context context, Player player) {
        Board board = context.getBoard();

        // Find the king position for the given player
        List<Position> kingPositions = board.getAllPiecePositions()
                .stream()
                .filter(pp -> pp.piece() instanceof King && pp.piece().getPlayer() == player)
                .map(PiecePosition::position)
                .toList();

        if (kingPositions.isEmpty()) {
            log.debug("King not found for player {}, skipping isInCheck calculations", player);
            return false;
        }
        if (kingPositions.size() > 1) {
            throw new IllegalStateException("More than one king found for player " + player);
        }

        Position kingPosition = kingPositions.getFirst();
        return isSquareUnderAttack(context, kingPosition, player);
    }

    @Override
    public boolean isSquareUnderAttack(Context context, Position square, Player owner) {
        Player opponent = owner.getOpponent();
        Board board = context.getBoard();

        // loop through all opponent pieces
        for (PiecePosition pp : board.getAllPiecePositions()) {
            if (pp.piece().getPlayer() != opponent) continue;

            // get their attack patterns (no legality filter)
            for (PieceMoveGenerator gen : context.getPieceMoveGeneratorFactory().getPieceMoveGenerators(pp.piece())) {
                if (gen.generate(context, pp.piece(), pp.position())
                        .anyMatch(move -> move.getEnd().equals(square))) {
                    return true;
                }
            }
        }
        return false;
    }

}