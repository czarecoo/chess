package org.czareg.ui.swing;

import javax.swing.*;
import java.awt.*;

public class SwingMain {

    private static final String TITLE = "Chess";
    private static final int DEFAULT_PANEL_SIZE_IN_PIXELS = 800;

    public static void main(String[] args) {
        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ChessBoardPanel panel = new ChessBoardPanel();
        panel.setPreferredSize(new Dimension(DEFAULT_PANEL_SIZE_IN_PIXELS, DEFAULT_PANEL_SIZE_IN_PIXELS));

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}