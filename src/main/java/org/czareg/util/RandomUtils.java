package org.czareg.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomUtils {

    private static final Random RANDOM = new Random();

    public static int betweenZeroInclusiveAndEndExclusive(int endExclusive) {
        if (endExclusive <= 0) {
            throw new IllegalArgumentException("endExclusive must be positive");
        }
        return RANDOM.nextInt(endExclusive);
    }

    public static int betweenZeroInclusiveAndEndInclusive(int endInclusive) {
        if (endInclusive < 0) {
            throw new IllegalArgumentException("endInclusive must be >= 0");
        }
        return RANDOM.nextInt(endInclusive + 1);
    }

    public static int betweenStartInclusiveAndEndInclusive(int start, int end) {
        if (end < start) {
            throw new IllegalArgumentException("end must be >= start");
        }
        return start + RANDOM.nextInt(end - start + 1);
    }
}
