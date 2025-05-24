package org.czareg.piece;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

    @Test
    void allPieceClassesShouldHavePlayerConstructor() {
        for (Class<? extends Piece> clazz : Piece.getPieceClasses()) {
            assertDoesNotThrow(() -> clazz.getConstructor(Player.class));
        }
    }

    @Test
    void pieceCreateShouldInstantiateCorrectSubclassWithPlayer() {
        Player testPlayer = Player.WHITE;

        for (Class<? extends Piece> clazz : Piece.getPieceClasses()) {
            Piece piece = assertDoesNotThrow(() -> Piece.create(clazz, testPlayer));

            assertNotNull(piece);
            assertEquals(clazz, piece.getClass());
            assertEquals(testPlayer, piece.getPlayer());
        }
    }
}