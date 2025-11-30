package org.czareg.piece;

import java.lang.reflect.Constructor;
import java.util.List;

public sealed interface Piece permits Pawn, Knight, Bishop, Rook, Queen, King {

    static List<Class<? extends Piece>> getAllPieceClasses() {
        return List.of(Pawn.class, Knight.class, Bishop.class, Rook.class, Queen.class, King.class);
    }

    static List<Class<? extends Piece>> getPromotionPieceClasses() {
        return List.of(Knight.class, Bishop.class, Rook.class, Queen.class);
    }

    static Piece create(Class<? extends Piece> clazz, Player player) {
        try {
            Constructor<? extends Piece> constructor = clazz.getConstructor(Player.class);
            return constructor.newInstance(player);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Cannot instantiate piece class: " + clazz.getSimpleName(), e);
        }
    }

    Player getPlayer();
}
