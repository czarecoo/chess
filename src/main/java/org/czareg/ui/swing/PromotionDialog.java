package org.czareg.ui.swing;

import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.piece.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class PromotionDialog {

    interface Callback {
        void onPromotionChosen(Move chosenMove);
    }

    static void show(Component parent, List<Move> promotionMoves, Callback callback) {
        Window window = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(window, "Choose promotion piece", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new GridLayout(1, promotionMoves.size()));
        dialog.setResizable(true);

        for (Move move : promotionMoves) {
            String player = move.getPiece().getPlayer().toString();
            String pieceName = move.getMetadata().getClass(Metadata.Key.PROMOTION_PIECE_CLASS, Piece.class)
                    .orElseThrow()
                    .getSimpleName();
            Image img = ImageCache.getPieceImage(player, pieceName);
            Image scaled = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JButton button = new JButton(new ImageIcon(scaled));
            button.setBackground(Color.WHITE);

            button.addActionListener(e -> {
                dialog.dispose();
                callback.onPromotionChosen(move);
            });

            dialog.add(button);
        }

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
