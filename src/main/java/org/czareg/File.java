package org.czareg;

import lombok.Getter;
import lombok.ToString;
import lombok.Value;

import java.util.List;

@Value
class File {

    private static final List<String> ALLOWED_VALUES = List.of("A", "B", "C", "D", "E", "F", "G", "H");

    @Getter
    String value;
    @ToString.Exclude
    int index;

    File(String value) {
        if (!ALLOWED_VALUES.contains(value)) {
            String message = "Illegal value: %s allowed values: %s".formatted(value, ALLOWED_VALUES);
            throw new IllegalArgumentException(message);
        }
        this.value = value;
        this.index = ALLOWED_VALUES.indexOf(value);
    }

    static File fromIndex(int index) {
        if (index < 0 || index >= ALLOWED_VALUES.size()) {
            throw new IllegalArgumentException("Index out of bounds. Index: " + index);
        }
        return new File(ALLOWED_VALUES.get(index));
    }
}

