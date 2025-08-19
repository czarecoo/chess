package org.czareg.position;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PositionFactoryTest {

    private PositionFactory positionFactory;

    @BeforeEach
    void setUp() {
        positionFactory = new PositionFactory(8, 8);
    }

    @Test
    void testBetweenIncorrect() {
        Index start = new Index(0, 0); // A1
        Index end = new Index(5, 2); // E3

        Exception e = assertThrows(IllegalArgumentException.class, () -> positionFactory.between(start, end));
        assertEquals("Indexes are neither orthogonal nor diagonal: Index(file=0, rank=0) -> Index(file=5, rank=2)", e.getMessage());
    }

    @Test
    void testBetweenHorizontal() {
        Index start = new Index(2, 3); // C4
        Index end = new Index(5, 3); // F4

        List<Position> result = positionFactory.between(start, end);

        assertEquals(2, result.size());
        assertEquals("D", result.getFirst().getFile());
        assertEquals(4, result.getFirst().getRank());
        assertEquals("E", result.getLast().getFile());
        assertEquals(4, result.getLast().getRank());
    }

    @Test
    void testBetweenVertical() {
        Index start = new Index(4, 1); // E2
        Index end = new Index(4, 5); // E6

        List<Position> result = positionFactory.between(start, end);

        assertEquals(3, result.size());
        assertEquals("E", result.getFirst().getFile());
        assertEquals(3, result.getFirst().getRank());
        assertEquals("E", result.get(1).getFile());
        assertEquals(4, result.get(1).getRank());
        assertEquals("E", result.getLast().getFile());
        assertEquals(5, result.getLast().getRank());
    }

    @Test
    void testBetweenDiagonal() {
        Index start = new Index(0, 0); // A1
        Index end = new Index(3, 3); // D4

        List<Position> result = positionFactory.between(start, end);

        assertEquals(2, result.size());
        assertEquals("B", result.getFirst().getFile());
        assertEquals(2, result.getFirst().getRank());
        assertEquals("C", result.getLast().getFile());
        assertEquals(3, result.getLast().getRank());
    }

    @Test
    void testBetweenDiagonalLong() {
        Index start = new Index(1, 0); // B1
        Index end = new Index(6, 5); // G6

        List<Position> result = positionFactory.between(start, end);

        assertEquals(4, result.size());
        assertEquals("C", result.get(0).getFile());
        assertEquals(2, result.get(0).getRank());
        assertEquals("D", result.get(1).getFile());
        assertEquals(3, result.get(1).getRank());
        assertEquals("E", result.get(2).getFile());
        assertEquals(4, result.get(2).getRank());
        assertEquals("F", result.get(3).getFile());
        assertEquals(5, result.get(3).getRank());
    }
}