package org.czareg;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
class PawnMoveGenerator implements MoveGenerator {

    @Override
    public Set<LegalMove> generate(Game game, Board board, Piece movingPiece, ClassicPosition currentClassicPosition) {
        Set<LegalMove> legalMoves = new HashSet<>();
        Set<Move> potentialMoves = movingPiece.getPotentialMoves();

        for (Move move : potentialMoves) {
            if (move instanceof PawnCaptureLeftMove ||
                    move instanceof PawnCaptureRightMove ||
                    move instanceof PawnEnPassantLeftMove ||
                    move instanceof PawnEnPassantRightMove ||
                    move instanceof PawnPromotionMove

            ) {
                // TODO implement
                continue;
            }
            if (move instanceof PawnDoubleForwardMove) {
                if (game.hasPieceMovedBefore(movingPiece)) {
                    log.debug("Rejecting move: {} because the pawn was already moved", move);
                    continue;
                }
                // TODO check if something is on the way
            }
            IndexChange indexChange = move.getIndexChange();
            IndexPosition indexPosition = currentClassicPosition.toIndexPosition(indexChange);
            if (!ClassicPosition.isValid(indexPosition)) {
                log.debug("Rejecting move: {} because it end position is not valid on the board", move);
                continue;
            }
            ClassicPosition endClassicPosition = indexPosition.toPosition();
            if (board.hasPiece(endClassicPosition)) {
                Piece targetPositionPiece = board.getPiece(endClassicPosition);
                Player targetPiecePlayer = targetPositionPiece.getPlayer();
                if (targetPiecePlayer == movingPiece.getPlayer()) {
                    log.debug("Rejecting move: {} because it end position: {} is occupied by friendly piece", move, endClassicPosition);
                    continue;
                }
                //TODO deal with capture logic, Pawn forward and double forward cannot capture
            }
            // TODO check if something is on the way

            legalMoves.add(new LegalMove(movingPiece, currentClassicPosition, endClassicPosition));
        }

        return legalMoves;
    }
}
