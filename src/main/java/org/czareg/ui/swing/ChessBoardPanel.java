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
                Optional<Position> clickedPositionOptional = getClickedPosition(e.getX(), e.getY());
                if (clickedPositionOptional.isEmpty()) {
                    return;
                }
                Position clickedPosition = clickedPositionOptional.get();
                handleClick(clickedPosition);
                repaint();
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle rectangle = createRectangle();
        drawer.drawBoard(g2d, rectangle);
        drawer.drawHighlights(g2d, rectangle, selection);
        drawer.drawPieces(g2d, rectangle);
    }

    private void handleClick(Position clickedPosition) {
        if (selection.isNoPositionSelected()) {
            handleSelect(clickedPosition);
        } else {
            List<Move> matchedHighlightedMoves = selection.highlightedMoves()
                    .stream()
                    .filter(move -> move.getEnd().equals(clickedPosition))
                    .toList();
            if (matchedHighlightedMoves.isEmpty()) {
                handleSelect(clickedPosition);
            } else {
                handleMoves(matchedHighlightedMoves);
            }
        }
    }

    private void handleMoves(List<Move> moves) {
        if (moves.size() > 1) {
            boolean areAllMatchedMovesPromotion = moves.stream()
                    .allMatch(Move::isPromotion);
            if (!areAllMatchedMovesPromotion) {
                throw new IllegalStateException("Detected multiple moves and not all of them are promotion: " + moves);
            }
            Rectangle rectangle = createRectangle();
            PromotionDialog.show(
                    this,
                    moves,
                    rectangle,
                    chosenMove -> {
                        game.makeMove(chosenMove);
                        selection = Selection.EMPTY;
                        repaint();
                    }
            );
            return;
        }
        Move move = moves.getFirst();
        game.makeMove(move);
        selection = Selection.EMPTY;
    }

    private void handleSelect(Position clickedPosition) {
        if (game.isCurrentPlayerPiece(clickedPosition)) {
            selection = new Selection(clickedPosition, game.findMovesStarting(clickedPosition));
        } else {
            selection = Selection.EMPTY;
        }
    }

    private Rectangle createRectangle() {
        int size = Math.min(getWidth(), getHeight());
        int rectangleWidth = size / game.getMaxFile();
        int rectangleHeight = size / game.getMaxRank();
        return new Rectangle(rectangleWidth, rectangleHeight);
    }

    private Optional<Position> getClickedPosition(int mouseX, int mouseY) {
        Rectangle rectangle = createRectangle();
        int file = mouseX / rectangle.width();
        int rank = game.getMaxRank() - 1 - (mouseY / rectangle.height());
        if (file < 0 || file >= game.getMaxFile() || rank < 0 || rank >= game.getMaxRank()) {
            log.info("Click outside board, x={} y={}", mouseX, mouseY);
            return Optional.empty();
        }
        return Optional.of(game.create(file, rank));
    }
}
