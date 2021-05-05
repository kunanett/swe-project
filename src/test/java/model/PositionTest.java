package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    Position position;

    @BeforeEach
    void init(){
        position = new Position(0, 0);
    }

    @org.junit.jupiter.api.Test
    void isNeighbour() {
        assertTrue(position.isNeighbour(new Position(0, 1)));
        assertTrue(position.isNeighbour(new Position(1, 1)));
        assertFalse(position.isNeighbour(new Position(2, 1)));
        assertFalse(position.isNeighbour(new Position(0, 0)));
    }

    @org.junit.jupiter.api.Test
    void getNeighbours() {
        assertEquals(Arrays.asList(new Position(0, 1), new Position(1, 0), new Position(1, 1)), position.getNeighbours());
        assertEquals(Arrays.asList(new Position(0, 0), new Position(0, 1), new Position(0, 2),
                                   new Position(1, 0), new Position(1, 2),
                                   new Position(2, 0), new Position(2, 1), new Position(2, 2)),
                new Position(1, 1).getNeighbours());

    }

    @Test
    void constructorShouldThrowIllegalArgumentException(){
        assertThrows(IllegalArgumentException.class, () -> new Position(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Position(0, 9));

    }

    @Test
    void testEquals(){
        assertTrue(position.equals(position));
        assertTrue(position.equals(new Position(position.row(), position.col())));
        assertFalse(position.equals(new Position(5, 7)));
        assertFalse(position.equals("this is a string"));
        assertFalse(position.equals(null));
    }


    @Test
    void testHashCode() {
        assertEquals(position.hashCode(), position.hashCode());
        assertEquals(position.hashCode(), new Position(position.row(), position.col()).hashCode());
    }
}