package org.czareg.board;

import org.czareg.piece.*;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;

public class PromotionTestingPieceStartingPositionPlacer implements PiecePlacer {

    @Override
    public void place(Board board) {
        place(board, WHITE, Rook.class, "A", 1);
        place(board, WHITE, Knight.class, "B", 1);
        place(board, WHITE, Bishop.class, "C", 1);
        place(board, WHITE, Queen.class, "D", 1);
        place(board, WHITE, Bishop.class, "F", 1);
        place(board, WHITE, Knight.class, "G", 1);
        place(board, WHITE, Rook.class, "H", 1);

        place(board, WHITE, King.class, "A", 4);

        place(board, WHITE, Pawn.class, "A", 7);
        place(board, WHITE, Pawn.class, "B", 7);
        place(board, WHITE, Pawn.class, "C", 7);
        place(board, WHITE, Pawn.class, "D", 7);
        place(board, WHITE, Pawn.class, "E", 7);
        place(board, WHITE, Pawn.class, "F", 7);
        place(board, WHITE, Pawn.class, "G", 7);
        place(board, WHITE, Pawn.class, "H", 7);


        place(board, BLACK, Pawn.class, "A", 2);
        place(board, BLACK, Pawn.class, "B", 2);
        place(board, BLACK, Pawn.class, "C", 2);
        place(board, BLACK, Pawn.class, "D", 2);
        place(board, BLACK, Pawn.class, "E", 2);
        place(board, BLACK, Pawn.class, "F", 2);
        place(board, BLACK, Pawn.class, "G", 2);
        place(board, BLACK, Pawn.class, "H", 2);

        place(board, BLACK, King.class, "H", 5);

        place(board, BLACK, Rook.class, "A", 8);
        place(board, BLACK, Knight.class, "B", 8);
        place(board, BLACK, Bishop.class, "C", 8);
        place(board, BLACK, Queen.class, "D", 8);
        place(board, BLACK, Bishop.class, "F", 8);
        place(board, BLACK, Knight.class, "G", 8);
        place(board, BLACK, Rook.class, "H", 8);
    }

    private void place(Board board,
                       Player player,
                       Class<? extends Piece> pieceClass,
                       String file,
                       int rank) {
        PositionFactory pf = board.getPositionFactory();
        Position pos = pf.create(file, rank);
        Piece piece = Piece.create(pieceClass, player);
        board.placePiece(pos, piece);
    }
}
