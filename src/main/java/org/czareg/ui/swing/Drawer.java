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

    void drawBoard(Graphics2D graphics, int cellSize) {
        for (int rank = 0; rank < game.getMaxRank(); rank++) {
            for (int file = 0; file < game.getMaxFile(); file++) {
                graphics.setColor((file + rank) % 2 == 0 ? LIGHT_SQUARE_COLOR : DARK_SQUARE_COLOR);
                Point topLeft = getTopLeftPoint(file, rank, cellSize);
                graphics.fillRect(topLeft.x, topLeft.y, cellSize, cellSize);
            }
        }
    }

    void drawPieces(Graphics2D graphics, int cellSize) {
        for (int rank = 0; rank < game.getMaxRank(); rank++) {
            for (int file = 0; file < game.getMaxFile(); file++) {
                Position pos = game.create(file, rank);
                if (!game.hasPiece(pos)) {
                    continue;
                }
                Piece piece = game.getPiece(pos);
                Image image = ImageCache.getPieceImage(piece);
                Point topLeft = getTopLeftPoint(file, rank, cellSize);
                graphics.drawImage(image, topLeft.x, topLeft.y, cellSize, cellSize, null);
            }
        }
    }

    void drawHighlights(Graphics2D graphics, int cellSize, Selection selection) {
        Position selectedPosition = selection.selectedPosition();
        if (selectedPosition != null) {
            Index index = game.create(selectedPosition);
            graphics.setColor(SEMI_TRANSPARENT_YELLOW);
            Point topLeft = getTopLeftPoint(index, cellSize);
            graphics.fillRect(topLeft.x, topLeft.y, cellSize, cellSize);
        }

        graphics.setColor(SEMI_TRANSPARENT_GREEN);
        for (Move move : selection.highlightedMoves()) {
            Index index = game.create(move.getEnd());
            Point topLeft = getTopLeftPoint(index, cellSize);

            drawCenteredCircle(graphics, topLeft, cellSize);
        }
    }

    private void drawCenteredCircle(Graphics2D graphics, Point topLeft, int cellSize) {
        int percentOfRectangle = 65;
        int size = (int) (cellSize * percentOfRectangle / 100f);

        int x = topLeft.x + (cellSize - size) / 2;
        int y = topLeft.y + (cellSize - size) / 2;

        graphics.fillOval(x, y, size, size);
    }

    private Point getTopLeftPoint(Index index, int cellSize) {
        return getTopLeftPoint(index.getFile(), index.getRank(), cellSize);
    }

    private Point getTopLeftPoint(int file, int rank, int cellSize) {
        int x = file * cellSize;
        int y = (game.getMaxRank() - 1 - rank) * cellSize;
        return new Point(x, y);
    }
}
