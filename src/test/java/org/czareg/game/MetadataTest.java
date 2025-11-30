package org.czareg.game;

import org.czareg.piece.Piece;
import org.czareg.piece.Queen;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.czareg.game.Metadata.Key.MOVE_TYPE;
import static org.czareg.game.Metadata.Key.PROMOTION_PIECE_CLASS;
import static org.czareg.game.MoveType.PROMOTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MetadataTest {

    @Test
    void shouldBeAbleToGetPromotionMoveTypeOutOfMetadata() {
        Metadata metadata = new Metadata();
        metadata.put(MOVE_TYPE, PROMOTION);

        Optional<MoveType> moveType = metadata.get(MOVE_TYPE, MoveType.class);

        assertTrue(moveType.isPresent());
        assertEquals(PROMOTION, moveType.get());
    }

    @Test
    void shouldBeAbleToGetPromotionPieceClassOutOfMetadata() {
        Metadata metadata = new Metadata();
        metadata.put(PROMOTION_PIECE_CLASS, Queen.class);

        Optional<Class<? extends Piece>> pieceClassOptional = metadata.getClass(PROMOTION_PIECE_CLASS, Piece.class);

        assertTrue(pieceClassOptional.isPresent());
        Class<? extends Piece> pieceClass = pieceClassOptional.get();
        assertEquals(Queen.class, pieceClass);
        assertEquals("Queen", pieceClass.getSimpleName());
    }
}