package org.czareg;

import java.util.*;

import static org.czareg.Player.*;

class ClassicGame implements Game {

    private final Board board;
    private final Map<Player, List<LegalMove>> history;

    ClassicGame(Board board){
        this.board = board;
        history = new EnumMap<>(Player.class);
        for (Player player : values()) {
            history.put(player, new ArrayList<>());
        }
    }

    @Override
    public boolean hasPieceMovedBefore(Piece piece) {
        return history
                .get(piece.getPlayer())
                .stream()
                .anyMatch(legalMove -> legalMove.piece() == piece);
    }

    @Override
    public Map<Player, List<LegalMove>> getPlayerMoves() {
        return history;
    }

    @Override
    public void makeMove(LegalMove legalMove) {
        // TODO check who's turn it is
        board.movePiece(legalMove.start(), legalMove.end());
        Player player = legalMove.piece().getPlayer();
        history.get(player).add(legalMove);
    }
}
