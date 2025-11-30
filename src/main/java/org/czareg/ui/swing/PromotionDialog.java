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

    static void show(Component parent, List<Move> promotionMoves, Rectangle rectangle, Callback callback) {
        Window window = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(window, "Choose promotion piece", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new GridLayout(1, promotionMoves.size()));
        dialog.setResizable(false);

        String player = promotionMoves.getFirst().getPiece().getPlayer().toString();
        for (Class<? extends Piece> promotionPieceClass : Piece.getPromotionPieceClasses()) {
            String pieceName = promotionPieceClass.getSimpleName();
            Image image = ImageCache.getPieceImage(player, pieceName);

            int size = rectangle.getShorterSide();
            ImageChoice choice = new ImageChoice(image, size, () -> {
                dialog.dispose();
                Move chosenMove = promotionMoves.stream()
                        .filter(move -> move.getMetadata().isExactly(Metadata.Key.PROMOTION_PIECE_CLASS, promotionPieceClass))
                        .findFirst()
                        .orElseThrow();
                callback.onPromotionChosen(chosenMove);
            });

            dialog.add(choice);
        }

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
