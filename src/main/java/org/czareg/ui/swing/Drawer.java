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

    private final Game game;

    Drawer(Game game) {
        this.game = game;
    }

    void drawBoard(Graphics2D graphics, Rectangle rectangle) {
        for (int rank = 0; rank < game.getMaxRank(); rank++) {
            for (int file = 0; file < game.getMaxFile(); file++) {
                graphics.setColor((file + rank) % 2 == 0 ? LIGHT_SQUARE_COLOR : DARK_SQUARE_COLOR);
                Point topLeft = getTopLeftPoint(file, rank, rectangle);
                graphics.fillRect(topLeft.x, topLeft.y, rectangle.width(), rectangle.height());
            }
        }
    }

    void drawPieces(Graphics2D graphics, Rectangle rectangle) {
        for (int rank = 0; rank < game.getMaxRank(); rank++) {
            for (int file = 0; file < game.getMaxFile(); file++) {
                Position pos = game.create(file, rank);
                if (!game.hasPiece(pos)) {
                    continue;
                }
                Piece piece = game.getPiece(pos);
                Image image = ImageCache.getPieceImage(piece);
                Point topLeft = getTopLeftPoint(file, rank, rectangle);
                graphics.drawImage(image, topLeft.x, topLeft.y, rectangle.width(), rectangle.height(), null);
            }
        }
    }

    void drawHighlights(Graphics2D graphics, Rectangle rectangle, Selection selection) {
        Position selectedPosition = selection.selectedPosition();
        if (selectedPosition != null) {
            Index index = game.create(selectedPosition);
            graphics.setColor(SEMI_TRANSPARENT_YELLOW);
            Point topLeft = getTopLeftPoint(index, rectangle);
            graphics.fillRect(topLeft.x, topLeft.y, rectangle.width(), rectangle.height());
        }

        graphics.setColor(SEMI_TRANSPARENT_GREEN);
        for (Move move : selection.highlightedMoves()) {
            Index index = game.create(move.getEnd());
            Point topLeft = getTopLeftPoint(index, rectangle);

            drawCenteredCircle(graphics, topLeft, rectangle);
        }
    }

    private void drawCenteredCircle(Graphics2D graphics, Point rectangleTopLeft, Rectangle rectangle) {
        int percentOfRectangle = 65;
        int size = (int) (rectangle.getShorterSide() * percentOfRectangle / 100f);

        int x = rectangleTopLeft.x + (rectangle.width() - size) / 2;
        int y = rectangleTopLeft.y + (rectangle.height() - size) / 2;

        graphics.fillOval(x, y, size, size);
    }

    private Point getTopLeftPoint(Index index, Rectangle rectangle) {
        return getTopLeftPoint(index.getFile(), index.getRank(), rectangle);
    }

    private Point getTopLeftPoint(int file, int rank, Rectangle rectangle) {
        int x = file * rectangle.width();
        int y = (game.getMaxRank() - 1 - rank) * rectangle.height();
        return new Point(x, y);
    }
}
