package org.czareg.game;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.czareg.game.Metadata.Key.MOVE_TYPE;

@EqualsAndHashCode
@ToString
public class Metadata {

    public enum Key {
        MOVE_TYPE,
        PROMOTION_PIECE,
        CASTLING_ROOK_START_POSITION,
        CASTLING_ROOK_END_POSITION,
        CAPTURE_PIECE,
        CAPTURE_PIECE_POSITION // in classic chess, this key should only be added for en passant
    }

    private final Map<Key, Object> data;

    public Metadata(MoveType moveType) {
        data = new EnumMap<>(Key.class);
        data.put(MOVE_TYPE, Objects.requireNonNull(moveType));
    }

    public <T> Metadata put(Key key, T value) {
        if (data.containsKey(key)) {
            throw new IllegalStateException("Metadata with %s already exists. Overwriting is forbidden.".formatted(key));
        }
        data.put(key, value);
        return this;
    }

    public <T> Optional<T> get(Key key, Class<T> type) {
        return Optional.ofNullable(data.get(key))
                .filter(type::isInstance)
                .map(type::cast);
    }

    public <T> boolean isExactly(Key key, T typeValueToCheck) {
        Class<?> typeClass = typeValueToCheck.getClass();
        return get(key, typeClass)
                .filter(typeValue -> typeValue == typeValueToCheck)
                .isPresent();
    }

    public boolean containsAll(Metadata metadataToCheck) {
        return data.entrySet().containsAll(metadataToCheck.data.entrySet());
    }
}
