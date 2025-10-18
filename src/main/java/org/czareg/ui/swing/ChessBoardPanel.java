package org.czareg.ui.swing;

import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.board.ClassicPieceStartingPositionPlacer;
import org.czareg.board.PiecePlacer;
import org.czareg.game.*;
import org.czareg.move.MoveGenerators;
import org.czareg.piece.Piece;
import org.czareg.position.Index;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.util.Set;

@Slf4j
class ChessBoardPanel extends JPanel {

    private static final int DEFAULT_PANEL_SIZE_IN_PIXELS = 800;

    private static final Color LIGHT_SQUARE_COLOR = new Color(240, 217, 181);
    private static final Color DARK_SQUARE_COLOR = new Color(181, 136, 99);
    private static final Color SEMI_TRANSPARENT_YELLOW = new Color(255, 255, 0, 128);
    private static final Color SEMI_TRANSPARENT_GREEN = new Color(0, 255, 0, 128);

    private final ImageCache imageCache = new ImageCache();

    private final Context context;
    private final Board board;
    private final PositionFactory pf;
    private final MoveGenerators moveGenerators;
    private final History history;

    private Position selectedPosition;
    private Set<Move> highlightedMoves = Set.of();

    private GeneratedMoves generatedMoves;

    ChessBoardPanel() {
        context = ClassicContext.create();
        PiecePlacer placer = new ClassicPieceStartingPositionPlacer();
        placer.place(context.getBoard());
        this.board = context.getBoard();
        this.pf = board.getPositionFactory();
        this.moveGenerators = context.getMoveGenerators();
        this.history = context.getHistory();

        generateLegalMoves();

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
                    makeAnyMove();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight());
        int squareWidth = size / pf.getMaxFile();
        int squareHeight = size / pf.getMaxRank();
        drawBoard(g2d, squareWidth, squareHeight);
        drawHighlights(g2d, squareWidth, squareHeight);
        drawPieces(g2d, squareWidth, squareHeight);
    }

    private void drawBoard(Graphics2D g, int squareWidth, int squareHeight) {
        for (int rank = 0; rank < pf.getMaxRank(); rank++) {
            for (int file = 0; file < pf.getMaxFile(); file++) {
                g.setColor((file + rank) % 2 == 0 ? LIGHT_SQUARE_COLOR : DARK_SQUARE_COLOR);
                int x = file * squareWidth;
                int y = (pf.getMaxRank() - 1 - rank) * squareHeight;
                g.fillRect(x, y, squareWidth, squareHeight);
            }
        }
    }

    private void drawPieces(Graphics2D g, int squareWidth, int squareHeight) {
        for (int rank = 0; rank < pf.getMaxRank(); rank++) {
            for (int file = 0; file < pf.getMaxFile(); file++) {
                Position pos = pf.create(file, rank);
                if (!board.hasPiece(pos)) {
                    continue;
                }

                Piece piece = board.getPiece(pos);
                Image img = imageCache.getPieceImage(piece);
                int x = file * squareWidth;
                int y = (pf.getMaxRank() - 1 - rank) * squareHeight;
                g.drawImage(img, x, y, squareWidth, squareHeight, this);
            }
        }
    }

    private void drawHighlights(Graphics2D g, int squareWidth, int squareHeight) {
        if (selectedPosition != null) {
            Index idx = pf.create(selectedPosition);
            int x = idx.getFile() * squareWidth;
            int y = (pf.getMaxRank() - 1 - idx.getRank()) * squareHeight;

            g.setColor(SEMI_TRANSPARENT_YELLOW);
            g.fillRect(x, y, squareWidth, squareHeight);
        }

        g.setColor(SEMI_TRANSPARENT_GREEN);
        for (Move move : highlightedMoves) {
            Index idx = pf.create(move.getEnd());
            int x = idx.getFile() * squareWidth;
            int y = (pf.getMaxRank() - 1 - idx.getRank()) * squareHeight;

            g.fillOval(x + squareWidth / 4, y + squareHeight / 4, squareWidth / 2, squareHeight / 2);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_PANEL_SIZE_IN_PIXELS, DEFAULT_PANEL_SIZE_IN_PIXELS);
    }

    private void handleClick(int mouseX, int mouseY) {
        int size = Math.min(getWidth(), getHeight());
        int squareWidth = size / pf.getMaxFile();
        int squareHeight = size / pf.getMaxRank();

        int file = mouseX / squareWidth;
        int rank = pf.getMaxRank() - 1 - (mouseY / squareHeight);
        if (file < 0 || file >= pf.getMaxFile() || rank < 0 || rank >= pf.getMaxRank()) {
            return;
        }

        Position clicked = pf.create(file, rank);

        for (Move move : highlightedMoves) {
            if (move.getEnd().equals(clicked)) {
                context.getMoveMaker().make(context, move);
                generateLegalMoves();
                this.selectedPosition = null;
                this.highlightedMoves = Set.of();
                repaint();
                return;
            }
        }

        if (board.hasPiece(clicked) && board.getPiece(clicked).getPlayer() == history.getCurrentPlayer()) {
            this.selectedPosition = clicked;
            this.highlightedMoves = generatedMoves.getMovesStarting(clicked);
        } else {
            this.selectedPosition = null;
            this.highlightedMoves = Set.of();
        }

        repaint();
    }

    private void generateLegalMoves() {
        this.generatedMoves = moveGenerators.generateLegal(context);
    }

    private void makeAnyMove() {
        Optional<Move> optionalMove = generatedMoves.findAny();
        if (optionalMove.isEmpty()) {
            log.warn("No moves generated");
            return;
        }
        Move move = optionalMove.orElseThrow();
        context.getMoveMaker().make(context, move);

        generateLegalMoves();
        selectedPosition = null;
        highlightedMoves = Set.of();
        repaint();
    }
}
