package org.czareg.ui.swing;

import org.czareg.board.Board;
import org.czareg.board.ClassicPieceStartingPositionPlacer;
import org.czareg.board.PiecePlacer;
import org.czareg.game.ClassicContext;
import org.czareg.game.Context;
import org.czareg.piece.Piece;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import javax.swing.*;
import java.awt.*;

class ChessBoardPanel extends JPanel {

    private static final int BOARD_SIZE = 8;
    private static final int DEFAULT_PANEL_SIZE = 800;

    private static final Color LIGHT_SQUARE_COLOR = new Color(240, 217, 181);
    private static final Color DARK_SQUARE_COLOR = new Color(181, 136, 99);

    private final Board board;
    private final PositionFactory pf;
    private final ImageCache imageCache = new ImageCache();

    ChessBoardPanel() {
        Context context = ClassicContext.create();
        PiecePlacer placer = new ClassicPieceStartingPositionPlacer();
        placer.place(context.getBoard());

        this.board = context.getBoard();
        this.pf = board.getPositionFactory();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int size = Math.min(getWidth(), getHeight());
        int squareSize = size / BOARD_SIZE;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBoard(g2d, squareSize);
        drawPieces(g2d, squareSize);
    }

    private void drawBoard(Graphics2D g, int squareSize) {
        for (int rank = 0; rank < BOARD_SIZE; rank++) {
            for (int file = 0; file < BOARD_SIZE; file++) {
                g.setColor((file + rank) % 2 == 0 ? LIGHT_SQUARE_COLOR : DARK_SQUARE_COLOR);
                int x = file * squareSize;
                int y = (BOARD_SIZE - 1 - rank) * squareSize;
                g.fillRect(x, y, squareSize, squareSize);
            }
        }
    }

    private void drawPieces(Graphics2D g, int squareSize) {
        for (int rank = 0; rank < BOARD_SIZE; rank++) {
            for (int file = 0; file < BOARD_SIZE; file++) {
                Position pos = pf.create(file, rank);
                if (!board.hasPiece(pos)) continue;

                Piece piece = board.getPiece(pos);
                Image img = imageCache.getPieceImage(piece);
                if (img != null) {
                    int x = file * squareSize;
                    int y = (BOARD_SIZE - 1 - rank) * squareSize;
                    g.drawImage(img, x, y, squareSize, squareSize, this);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_PANEL_SIZE, DEFAULT_PANEL_SIZE);
    }
}