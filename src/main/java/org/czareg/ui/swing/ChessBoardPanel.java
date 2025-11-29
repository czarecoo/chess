package org.czareg.ui.swing;

import lombok.extern.slf4j.Slf4j;
import org.czareg.game.Move;
import org.czareg.position.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;

@Slf4j
class ChessBoardPanel extends JPanel {

    private static final int DEFAULT_PANEL_SIZE_IN_PIXELS = 800;

    private final Game game;
    private final Drawer drawer;

    private Selection selection;

    ChessBoardPanel() {
        game = new Game();
        drawer = new Drawer(game);
        selection = Selection.EMPTY;

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
                    selection = Selection.EMPTY;
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
        drawer.drawHighlights(g2d, square, selection);
        drawer.drawPieces(g2d, square);
    }

    private void handleClick(int mouseX, int mouseY) {
        Optional<Position> clickedPosition = getClickedPosition(mouseX, mouseY);
        if (clickedPosition.isEmpty()) {
            return;
        }
        Position clicked = clickedPosition.get();

        if (selection.isNoPositionSelected()) {
            handleSelect(clicked);
        } else {
            List<Move> matchedHighlightedMoves = selection.highlightedMoves()
                    .stream()
                    .filter(move -> move.getEnd().equals(clicked))
                    .toList();
            if (matchedHighlightedMoves.isEmpty()) {
                handleSelect(clicked);
            } else {
                Move move = matchedHighlightedMoves.getFirst(); // TODO handle promotion moves better
                handleMove(move);
            }
        }
        repaint();
    }

    private void handleMove(Move move) {
        game.makeMove(move);
        selection = Selection.EMPTY;
    }

    private void handleSelect(Position clicked) {
        if (game.isCurrentPlayerPiece(clicked)) {
            selection = new Selection(clicked, game.findMovesStarting(clicked));
        } else {
            selection = Selection.EMPTY;
        }
    }

    private Square createSquare() {
        int size = Math.min(getWidth(), getHeight());
        int squareWidth = size / game.getMaxFile();
        int squareHeight = size / game.getMaxRank();
        return new Square(squareWidth, squareHeight);
    }

    private Optional<Position> getClickedPosition(int mouseX, int mouseY) {
        Square square = createSquare();
        int file = mouseX / square.width();
        int rank = game.getMaxRank() - 1 - (mouseY / square.height());
        if (file < 0 || file >= game.getMaxFile() || rank < 0 || rank >= game.getMaxRank()) {
            log.info("Click outside board, x={} y={}", mouseX, mouseY);
            return Optional.empty();
        }
        return Optional.of(game.create(file, rank));
    }
}
