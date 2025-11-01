package org.czareg.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomUtils {

    private static final Random RANDOM = new Random();

    public static int betweenZeroInclusiveAndEndExclusive(int endExclusive) {
        return RANDOM.nextInt(endExclusive);
    }

    public static int betweenZeroInclusiveAndEndInclusive(int endInclusive) {
        return RANDOM.nextInt(endInclusive + 1);
    }

    public static int betweenStartInclusiveAndEndInclusive(int start, int end) {
        return start + RANDOM.nextInt(end - start + 1);
    }
}
