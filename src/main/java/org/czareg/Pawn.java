package org.czareg;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

class Pawn implements Piece {

    private static final int FIRST_MOVE_RANK_CHANGE = 2;
    private static final int NEXT_MOVES_RANK_CHANGE = 1;
    @Getter
    private final Player player;
    private final Position startingPosition;

    Pawn(Player player, Position startingPosition) {
        this.player = player;
        this.startingPosition = startingPosition;
    }

    @Override
    public Set<Move> getPotentialMoves(Position currentPosition) {
        Set<Move> moves = new HashSet<>();
        if (hasNotMovedFromStartingRank(currentPosition.rank())) {
            moves.add(new PawnForwardMove(currentPosition.with(directionMultiplier() * FIRST_MOVE_RANK_CHANGE, 0)));
        }
        moves.add(new PawnForwardMove(currentPosition.with(directionMultiplier() * NEXT_MOVES_RANK_CHANGE, 0)));
        return moves;
    }

    private int directionMultiplier() {
        return switch (player) {
            case WHITE -> 1;
            case BLACK -> -1;
        };
    }

    private boolean hasNotMovedFromStartingRank(Rank currentRank) {
        return currentRank.equals(startingPosition.rank());
    }
}
