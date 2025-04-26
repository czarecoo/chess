package org.czareg;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
class PawnMoveGenerator implements MoveGenerator {

    @Override
    public Set<LegalMove> generate(Board board, Piece movingPiece, Position currentPosition) {
        Set<LegalMove> legalMoves = new HashSet<>();
        Set<Move> potentialMoves = movingPiece.getPotentialMoves(currentPosition);

        for (Move move : potentialMoves) {
            //deal with check state

            IndexPosition indexPosition = move.getIndexPosition();
            if (!board.isValid(indexPosition)) {
                log.debug("Rejecting move:{} because it end position is not valid on the board", move);
                continue;
            }
            Position endPosition = board.getPositionOrThrow(indexPosition);
            if (board.hasPiece(endPosition)) {
                Piece targetPositionPiece = board.getPieceOrThrow(endPosition);
                Player targetPiecePlayer = targetPositionPiece.getPlayer();
                if (targetPiecePlayer == movingPiece.getPlayer()) {
                    log.debug("Rejecting move:{} because it end position:{} is occupied by friendly piece", move,endPosition);
                    continue;
                }
                //deal with capture logic
                if (move.canCapture()) {

                }
            }
            //deal with pieces in between
            if (move.canJump()) {

            }
            //board.getPositionsBetween(currentPosition, endPosition); ????

            //all checks done
            legalMoves.add(new LegalMove(movingPiece, currentPosition, endPosition));
        }

        return legalMoves;
    }
}
