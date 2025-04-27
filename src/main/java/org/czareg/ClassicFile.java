package org.czareg;

import lombok.Value;

import java.util.List;
import java.util.stream.IntStream;

@Value
class ClassicFile implements File {

    static final List<String> ALLOWED_VALUES = IntStream.rangeClosed('A','H').mapToObj(Character::toString).toList();

    String value;

    ClassicFile(String value) {
        if (!ALLOWED_VALUES.contains(value)) {
            String message = "Illegal value: %s allowed values: %s".formatted(value, ALLOWED_VALUES);
            throw new IllegalArgumentException(message);
        }
        this.value = value;
    }

    @Override
    public int getIndex() {
        return ALLOWED_VALUES.indexOf(value);
    }

    static ClassicFile fromIndex(int index) {
        if (!isIndexValid(index)) {
            throw new IllegalArgumentException("Index out of bounds. Index: " + index);
        }
        return new ClassicFile(ALLOWED_VALUES.get(index));
    }

    static boolean isIndexValid(int index){
        return index >= 0 && index < ALLOWED_VALUES.size();
    }
}

