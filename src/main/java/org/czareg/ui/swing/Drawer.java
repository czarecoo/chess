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

    void drawBoard(Graphics2D g, Square square) {
        for (int rank = 0; rank < game.getMaxRank(); rank++) {
            for (int file = 0; file < game.getMaxFile(); file++) {
                g.setColor((file + rank) % 2 == 0 ? LIGHT_SQUARE_COLOR : DARK_SQUARE_COLOR);
                int x = file * square.width();
                int y = (game.getMaxRank() - 1 - rank) * square.height();
                g.fillRect(x, y, square.width(), square.height());
            }
        }
    }

    void drawPieces(Graphics2D g, Square square) {
        for (int rank = 0; rank < game.getMaxRank(); rank++) {
            for (int file = 0; file < game.getMaxFile(); file++) {
                Position pos = game.create(file, rank);
                if (!game.hasPiece(pos)) {
                    continue;
                }

                Piece piece = game.getPiece(pos);
                Image img = imageCache.getPieceImage(piece);
                int x = file * square.width();
                int y = (game.getMaxRank() - 1 - rank) * square.height();
                g.drawImage(img, x, y, square.width(), square.height(), null);
            }
        }
    }

    void drawHighlights(Graphics2D g, Square square) {
        Position selectedPosition = game.getSelectedPosition();
        if (selectedPosition != null) {
            Index idx = game.create(selectedPosition);
            int x = idx.getFile() * square.width();
            int y = (game.getMaxRank() - 1 - idx.getRank()) * square.height();

            g.setColor(SEMI_TRANSPARENT_YELLOW);
            g.fillRect(x, y, square.width(), square.height());
        }

        g.setColor(SEMI_TRANSPARENT_GREEN);
        for (Move move : game.getHighlightedMoves()) {
            Index idx = game.create(move.getEnd());
            int x = idx.getFile() * square.width();
            int y = (game.getMaxRank() - 1 - idx.getRank()) * square.height();

            g.fillOval(x + square.width() / 4, y + square.height() / 4, square.width() / 2, square.height() / 2);
        }
    }
}
