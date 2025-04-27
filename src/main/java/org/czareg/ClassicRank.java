package org.czareg;

import lombok.Value;

import java.util.List;
import java.util.stream.IntStream;

@Value
class ClassicRank implements Rank {

    static final List<Integer> ALLOWED_VALUES = IntStream.rangeClosed(1, 8).boxed().toList();

    int value;

    ClassicRank(int value) {
        if (!ALLOWED_VALUES.contains(value)) {
            String message = "Illegal value: %d allowed values: %s".formatted(value, ALLOWED_VALUES);
            throw new IllegalArgumentException(message);
        }
        this.value = value;
    }

    @Override
    public int getIndex() {
        return value - 1;
    }

    static ClassicRank fromIndex(int index) {
        if (!isIndexValid(index)) {
            throw new IllegalArgumentException("Index out of bounds. Index: " + index);
        }
        return new ClassicRank(ALLOWED_VALUES.get(index));
    }

    static boolean isIndexValid(int index) {
        return index >= 0 && index < ALLOWED_VALUES.size();
    }
}
