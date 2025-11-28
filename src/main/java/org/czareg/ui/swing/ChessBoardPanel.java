package org.czareg.ui.swing;

import lombok.extern.slf4j.Slf4j;
import org.czareg.position.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Slf4j
class ChessBoardPanel extends JPanel {

    private static final int DEFAULT_PANEL_SIZE_IN_PIXELS = 800;

    private final Game game;
    private final Drawer drawer;

    ChessBoardPanel() {
        game = new Game();
        drawer = new Drawer(game);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });

        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    game.makeRandomMove();
                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    log.info("Escape pressed. Exiting gracefully.");
                    System.exit(0);
                }
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_PANEL_SIZE_IN_PIXELS, DEFAULT_PANEL_SIZE_IN_PIXELS);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Square square = createSquare();
        drawer.drawBoard(g2d, square);
        drawer.drawHighlights(g2d, square);
        drawer.drawPieces(g2d, square);
    }

    private void handleClick(int mouseX, int mouseY) {
        Square square = createSquare();

        int file = mouseX / square.width();
        int rank = game.getMaxRank() - 1 - (mouseY / square.height());
        if (file < 0 || file >= game.getMaxFile() || rank < 0 || rank >= game.getMaxRank()) {
            return;
        }

        Position clicked = game.create(file, rank);

        game.updateSelectedPositionAndHighlightedMoves(clicked);

        repaint();
    }

    private Square createSquare() {
        int size = Math.min(getWidth(), getHeight());
        int squareWidth = size / game.getMaxFile();
        int squareHeight = size / game.getMaxRank();
        return new Square(squareWidth, squareHeight);
    }
}
