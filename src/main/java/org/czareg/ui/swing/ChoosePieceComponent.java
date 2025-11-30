package org.czareg.ui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class ChoosePieceComponent extends JComponent {

    private final Image image;
    private final int cellSize;
    private boolean hover = false;

    ChoosePieceComponent(Image image, int cellSize, Runnable onClick) {
        this.image = image;
        this.cellSize = cellSize;

        setPreferredSize(new Dimension(cellSize, cellSize));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                onClick.run();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(image, 0, 0, cellSize, cellSize, null);

        if (hover) {
            g2.setColor(new Color(0, 0, 0));
            g2.setStroke(new BasicStroke(3f));
            g2.drawRect(1, 1, getWidth() - 2, getHeight() - 2);
        }
    }
}

