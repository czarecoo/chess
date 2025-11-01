package org.czareg.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomUtilsTest {

    private static final int ITERATIONS = 1_000_000;

    @ParameterizedTest
    @CsvSource({
            "10",
            "1",
            "100"
    })
    @DisplayName("betweenZeroInclusiveAndEndExclusive: results are within [0, endExclusive)")
    void testBetweenZeroInclusiveAndEndExclusive_bounds(int endExclusive) {
        for (int i = 0; i < ITERATIONS; i++) {
            int result = RandomUtils.betweenZeroInclusiveAndEndExclusive(endExclusive);
            assertTrue(result >= 0, "Result should be >= 0 but was " + result);
            assertTrue(result < endExclusive, "Result should be < " + endExclusive + " but was " + result);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "10",
            "1",
            "100"
    })
    @DisplayName("betweenZeroInclusiveAndEndInclusive: results are within [0, endInclusive]")
    void testBetweenZeroInclusiveAndEndInclusive_bounds(int endInclusive) {
        for (int i = 0; i < ITERATIONS; i++) {
            int result = RandomUtils.betweenZeroInclusiveAndEndInclusive(endInclusive);
            assertTrue(result >= 0, "Result should be >= 0 but was " + result);
            assertTrue(result <= endInclusive, "Result should be <= " + endInclusive + " but was " + result);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "5,10",
            "0,0",
            "-3,3"
    })
    @DisplayName("betweenStartInclusiveAndEndInclusive: results are within [start, end]")
    void testBetweenStartInclusiveAndEndInclusive_bounds(int start, int end) {
        for (int i = 0; i < ITERATIONS; i++) {
            int result = RandomUtils.betweenStartInclusiveAndEndInclusive(start, end);
            assertTrue(result >= start, "Result should be >= " + start + " but was " + result);
            assertTrue(result <= end, "Result should be <= " + end + " but was " + result);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "0",
            "-1",
            "-100"
    })
    @DisplayName("betweenZeroInclusiveAndEndExclusive: throws for endExclusive <= 0")
    void testBetweenZeroInclusiveAndEndExclusive_invalidInputs(int endExclusive) {
        assertThrows(IllegalArgumentException.class,
                () -> RandomUtils.betweenZeroInclusiveAndEndExclusive(endExclusive),
                "Expected IllegalArgumentException for endExclusive=" + endExclusive);
    }

    @ParameterizedTest
    @CsvSource({
            "-1",
            "-10",
            "-100"
    })
    @DisplayName("betweenZeroInclusiveAndEndInclusive: throws for endInclusive < 0")
    void testBetweenZeroInclusiveAndEndInclusive_invalidInputs(int endInclusive) {
        assertThrows(IllegalArgumentException.class,
                () -> RandomUtils.betweenZeroInclusiveAndEndInclusive(endInclusive),
                "Expected IllegalArgumentException for endInclusive=" + endInclusive);
    }

    @ParameterizedTest
    @CsvSource({
            "10,5",
            "0,-1",
            "1,0"
    })
    @DisplayName("betweenStartInclusiveAndEndInclusive: throws for end < start")
    void testBetweenStartInclusiveAndEndInclusive_invalidInputs(int start, int end) {
        assertThrows(IllegalArgumentException.class,
                () -> RandomUtils.betweenStartInclusiveAndEndInclusive(start, end),
                "Expected IllegalArgumentException for start=" + start + ", end=" + end);
    }
}