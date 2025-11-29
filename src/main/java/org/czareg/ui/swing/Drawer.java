package org.czareg.ui.swing;

import lombok.extern.slf4j.Slf4j;
import org.czareg.game.Move;
import org.czareg.piece.Piece;
import org.czareg.position.Index;
import org.czareg.position.Position;

import java.awt.*;

@Slf4j
class Drawer {

    private static final Color LIGHT_SQUARE_COLOR = new Color(240, 217, 181);
    private static final Color DARK_SQUARE_COLOR = new Color(181, 136, 99);
    private static final Color SEMI_TRANSPARENT_YELLOW = new Color(255, 255, 0, 128);
    private static final Color SEMI_TRANSPARENT_GREEN = new Color(0, 255, 0, 128);

    private final ImageCache imageCache = new ImageCache();

    private final Game game;

    Drawer(Game game) {
        this.game = game;
    }

    void drawBoard(Graphics2D graphics, Square square) {
        for (int rank = 0; rank < game.getMaxRank(); rank++) {
            for (int file = 0; file < game.getMaxFile(); file++) {
                graphics.setColor((file + rank) % 2 == 0 ? LIGHT_SQUARE_COLOR : DARK_SQUARE_COLOR);
                Point topLeftSquarePoint = getTopLeftSquarePoint(file, rank, square);
                graphics.fillRect(topLeftSquarePoint.x, topLeftSquarePoint.y, square.width(), square.height());
            }
        }
    }

    void drawPieces(Graphics2D graphics, Square square) {
        for (int rank = 0; rank < game.getMaxRank(); rank++) {
            for (int file = 0; file < game.getMaxFile(); file++) {
                Position pos = game.create(file, rank);
                if (!game.hasPiece(pos)) {
                    continue;
                }
                Piece piece = game.getPiece(pos);
                Image image = imageCache.getPieceImage(piece);
                Point topLeftSquarePoint = getTopLeftSquarePoint(file, rank, square);
                graphics.drawImage(image, topLeftSquarePoint.x, topLeftSquarePoint.y, square.width(), square.height(), null);
            }
        }
    }

    void drawHighlights(Graphics2D graphics, Square square, Selection selection) {
        Position selectedPosition = selection.selectedPosition();
        if (selectedPosition != null) {
            Index index = game.create(selectedPosition);
            graphics.setColor(SEMI_TRANSPARENT_YELLOW);
            Point topLeftSquarePoint = getTopLeftSquarePoint(index, square);
            graphics.fillRect(topLeftSquarePoint.x, topLeftSquarePoint.y, square.width(), square.height());
        }

        graphics.setColor(SEMI_TRANSPARENT_GREEN);
        for (Move move : selection.highlightedMoves()) {
            Index index = game.create(move.getEnd());
            Point topLeftSquarePoint = getTopLeftSquarePoint(index, square);

            drawCenteredCircle(graphics, topLeftSquarePoint, square);
        }
    }

    private void drawCenteredCircle(Graphics2D graphics, Point squareTopLeft, Square square) {
        int percentOfSquare = 65;
        int size = (int) (square.width() * percentOfSquare / 100f);

        int x = squareTopLeft.x + (square.width() - size) / 2;
        int y = squareTopLeft.y + (square.height() - size) / 2;

        graphics.fillOval(x, y, size, size);
    }

    private Point getTopLeftSquarePoint(Index index, Square square) {
        return getTopLeftSquarePoint(index.getFile(), index.getRank(), square);
    }

    private Point getTopLeftSquarePoint(int file, int rank, Square square) {
        int x = file * square.width();
        int y = (game.getMaxRank() - 1 - rank) * square.height();
        return new Point(x, y);
    }
}
