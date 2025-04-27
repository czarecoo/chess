package org.czareg;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
final class Pawn implements Piece {

    private final Player player;

    @Override
    public Set<Move> getPotentialMoves() {
        Set<Move> moves = new HashSet<>();
        moves.add(new PawnDoubleForwardMove(player));
        moves.add(new PawnForwardMove(player));
        moves.add(new PawnCaptureLeftMove(player));
        moves.add(new PawnCaptureRightMove(player));
        moves.add(new PawnEnPassantLeftMove(player));
        moves.add(new PawnEnPassantRightMove(player));
        moves.add(new PawnPromotionMove(player));
        return moves;
    }
}
