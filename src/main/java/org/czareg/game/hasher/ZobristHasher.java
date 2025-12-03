package org.czareg.game.hasher;

import org.czareg.board.Board;
import org.czareg.board.PiecePosition;
import org.czareg.game.Context;
import org.czareg.piece.Piece;
import org.czareg.piece.Player;
import org.czareg.position.Index;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ZobristHasher {

    private final long[][][] zobristTable; // [pieceType][color][squareIndex]
    private final long[] sideToMoveKeys; // one per player
    private final Map<Class<? extends Piece>, Integer> pieceTypeIndex;
    private final Map<Player, Integer> playerIndexMap;

    public ZobristHasher(Board board) {
        Random rng = new Random(123456); // fixed seed for reproducibility

        PositionFactory pf = board.getPositionFactory();
        List<Class<? extends Piece>> pieceTypes = Piece.getAllPieceClasses();
        zobristTable = new long[pieceTypes.size()][Player.values().length][pf.getMaxFile() * pf.getMaxRank()];
        for (int p = 0; p < pieceTypes.size(); p++) {
            for (int c = 0; c < Player.values().length; c++) {
                for (int sq = 0; sq < pf.getMaxFile() * pf.getMaxRank(); sq++) {
                    zobristTable[p][c][sq] = rng.nextLong();
                }
            }
        }
        pieceTypeIndex = new HashMap<>();
        for (int i = 0; i < pieceTypes.size(); i++) {
            pieceTypeIndex.put(pieceTypes.get(i), i);
        }

        playerIndexMap = new HashMap<>();
        Player[] players = Player.values();
        for (int i = 0; i < players.length; i++) {
            playerIndexMap.put(players[i], i);
        }

        sideToMoveKeys = new long[players.length];
        for (int i = 0; i < players.length; i++) {
            sideToMoveKeys[i] = rng.nextLong();
        }
    }

    public long computeHash(Context context) {
        Board board = context.getBoard();
        Player toMove = context.getHistory().getCurrentPlayer();
        PositionFactory pf = board.getPositionFactory();

        long hash = 0L;
        for (PiecePosition pp : board.getAllPiecePositions()) {
            Piece piece = pp.piece();
            int pieceIndex = pieceTypeIndex.get(piece.getClass());
            int colorIndex = playerIndexMap.get(piece.getPlayer());
            int squareIndex = toIndex(pp.position(), pf);

            hash ^= zobristTable[pieceIndex][colorIndex][squareIndex];
        }

        int toMoveIdx = playerIndexMap.get(toMove);
        hash ^= sideToMoveKeys[toMoveIdx];
        return hash;
    }

    private int toIndex(Position pos, PositionFactory pf) {
        Index idx = pf.create(pos);
        return idx.getRank() * pf.getMaxFile() + idx.getFile();
    }
}
