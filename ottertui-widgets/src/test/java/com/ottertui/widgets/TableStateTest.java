package com.ottertui.widgets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class TableStateTest {

    @Test
    @DisplayName("default selected index is 0")
    void defaultSelectedIndex() {
        TableState s = new TableState();
        assertEquals(0, s.selectedIndex());
    }

    @Test
    @DisplayName("select sets index")
    void selectSetsIndex() {
        TableState s = new TableState();
        s.select(5);
        assertEquals(5, s.selectedIndex());
    }

    @Test
    @DisplayName("moveUp decreases index")
    void moveUp() {
        TableState s = new TableState();
        s.select(2);
        s.moveUp();
        assertEquals(1, s.selectedIndex());
    }

    @Test
    @DisplayName("moveUp at zero stays at zero")
    void moveUpAtZero() {
        TableState s = new TableState();
        s.moveUp();
        assertEquals(0, s.selectedIndex());
    }

    @Test
    @DisplayName("moveDown increases index")
    void moveDown() {
        TableState s = new TableState();
        s.moveDown();
        assertEquals(1, s.selectedIndex());
    }

    @Test
    @DisplayName("multiple moves")
    void multipleMoves() {
        TableState s = new TableState();
        s.moveDown();
        s.moveDown();
        assertEquals(2, s.selectedIndex());
        s.moveUp();
        assertEquals(1, s.selectedIndex());
    }
}
