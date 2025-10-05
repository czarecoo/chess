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
    private final long sideToMoveKey;
    private final Map<Class<? extends Piece>, Integer> pieceTypeIndex;

    public ZobristHasher(Board board) {
        Random rng = new Random(123456); // fixed seed for reproducibility

        PositionFactory pf = board.getPositionFactory();
        List<Class<? extends Piece>> pieceTypes = Piece.getPieceClasses();
        zobristTable = new long[pieceTypes.size()][2][pf.getMaxFile() * pf.getMaxRank()];
        for (int p = 0; p < pieceTypes.size(); p++) {
            for (int c = 0; c < 2; c++) {
                for (int sq = 0; sq < pf.getMaxFile() * pf.getMaxRank(); sq++) {
                    zobristTable[p][c][sq] = rng.nextLong();
                }
            }
        }
        sideToMoveKey = rng.nextLong();

        pieceTypeIndex = new HashMap<>();
        for (int i = 0; i < pieceTypes.size(); i++) {
            pieceTypeIndex.put(pieceTypes.get(i), i);
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
            int colorIndex = piece.getPlayer() == Player.WHITE ? 0 : 1;
            int squareIndex = toIndex(pp.position(), pf);

            hash ^= zobristTable[pieceIndex][colorIndex][squareIndex];
        }

        if (toMove == Player.BLACK) {
            hash ^= sideToMoveKey;
        }

        return hash;
    }

    private int toIndex(Position pos, PositionFactory pf) {
        Index idx = pf.create(pos);
        return idx.getRank() * pf.getMaxFile() + idx.getFile();
    }
}
