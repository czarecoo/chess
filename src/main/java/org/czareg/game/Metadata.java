package org.czareg.game;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@EqualsAndHashCode
@ToString
public class Metadata {

    public enum Key {
        SPECIAL_MOVE_TYPE,
        PROMOTION_PIECE,
        CASTLING_ROOK_START_POSITION,
        CASTLING_ROOK_END_POSITION,
        CAPTURED_PIECE
    }

    private final Map<Key, Object> data = new EnumMap<>(Key.class);

    public <T> void put(Key key, T value) {
        data.put(key, value);
    }

    public <T> Optional<T> get(Key key, Class<T> type) {
        return Optional.ofNullable(data.get(key))
                .filter(type::isInstance)
                .map(type::cast);
    }

    public boolean containsKey(Key key) {
        return data.containsKey(key);
    }
}
