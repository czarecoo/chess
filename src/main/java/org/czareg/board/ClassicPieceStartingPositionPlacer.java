package org.czareg.board;

import org.czareg.piece.*;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;

public class ClassicPieceStartingPositionPlacer implements PiecePlacer {

    public void place(Board board) {
        place(board, 1, WHITE, getPiecesFilePieceStream());
        place(board, 2, WHITE, getPawnsFilePieceStream());
        place(board, 7, BLACK, getPawnsFilePieceStream());
        place(board, 8, BLACK, getPiecesFilePieceStream());
    }

    private void place(Board board, int rank, Player player, Stream<FilePiece> filePieces) {
        PositionFactory positionFactory = board.getPositionFactory();
        filePieces
                .forEach(
                        filePiece -> {
                            String file = filePiece.file();
                            Position position = positionFactory.create(rank, file);
                            Class<? extends Piece> pieceClass = filePiece.piece();
                            Piece piece = Piece.create(pieceClass, player);
                            board.placePiece(position, piece);
                        }
                );
    }

    private Stream<FilePiece> getPawnsFilePieceStream() {
        return IntStream.rangeClosed('A', 'H')
                .mapToObj(i -> String.valueOf((char) i))
                .map(file -> new FilePiece(file, Pawn.class));
    }

    private Stream<FilePiece> getPiecesFilePieceStream() {
        return Stream.of(
                new FilePiece("A", Rook.class),
                new FilePiece("B", Knight.class),
                new FilePiece("C", Bishop.class),
                new FilePiece("D", Queen.class),
                new FilePiece("E", King.class),
                new FilePiece("F", Bishop.class),
                new FilePiece("G", Knight.class),
                new FilePiece("H", Rook.class)
        );
    }

    private record FilePiece(String file, Class<? extends Piece> piece) {
    }
}
