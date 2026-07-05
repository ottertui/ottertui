package com.ottertui.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    @DisplayName("EMPTY cell uses space and DEFAULT style")
    void emptyCell() {
        assertEquals(' ', Cell.EMPTY.ch());
        assertEquals(Style.DEFAULT, Cell.EMPTY.style());
    }

    @Test
    @DisplayName("custom cell stores character and style")
    void customCell() {
        Style style = new Style(Color.RED, Color.BLUE, java.util.Set.of(Modifier.BOLD));
        Cell cell = new Cell('X', style);
        assertEquals('X', cell.ch());
        assertEquals(style, cell.style());
    }

    @Test
    @DisplayName("cell equality")
    void cellEquality() {
        Cell a = new Cell('A', Style.DEFAULT);
        Cell b = new Cell('A', Style.DEFAULT);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("cell inequality different char")
    void cellInequalityChar() {
        Cell a = new Cell('A', Style.DEFAULT);
        Cell b = new Cell('B', Style.DEFAULT);
        assertNotEquals(a, b);
    }
}
