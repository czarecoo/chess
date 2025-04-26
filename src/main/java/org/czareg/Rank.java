package org.czareg;

import lombok.Getter;
import lombok.ToString;
import lombok.Value;

import java.util.List;
import java.util.stream.IntStream;

@Value
class Rank {

    private static final List<Integer> ALLOWED_VALUES = IntStream.rangeClosed(1, 8).boxed().toList();

    @Getter
    int value;
    @ToString.Exclude
    int index;

    Rank(int value) {
        if (!ALLOWED_VALUES.contains(value)) {
            String message = "Illegal value: %d allowed values: %s".formatted(value, ALLOWED_VALUES);
            throw new IllegalArgumentException(message);
        }
        this.value = value;
        this.index = value - 1;
    }

    static Rank fromIndex(int index) {
        if (index < 0 || index >= ALLOWED_VALUES.size()) {
            throw new IllegalArgumentException("Index out of bounds. Index: " + index);
        }
        return new Rank(ALLOWED_VALUES.get(index));
    }
}
