package org.czareg.ui.swing;

import javax.swing.*;

public class SwingMain {

    private static final String TITLE = "Chess";

    public static void main(String[] args) {
        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ChessBoardPanel panel = new ChessBoardPanel();

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}