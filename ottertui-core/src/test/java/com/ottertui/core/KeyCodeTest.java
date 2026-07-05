package com.ottertui.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class KeyCodeTest {

    @Test
    @DisplayName("fromChar returns CHAR for any character")
    void fromCharReturnsChar() {
        assertEquals(KeyCode.CHAR, KeyCode.fromChar('a'));
        assertEquals(KeyCode.CHAR, KeyCode.fromChar('Z'));
        assertEquals(KeyCode.CHAR, KeyCode.fromChar('1'));
    }

    @Test
    @DisplayName("all key codes exist")
    void allKeyCodesExist() {
        assertNotNull(KeyCode.UP);
        assertNotNull(KeyCode.DOWN);
        assertNotNull(KeyCode.LEFT);
        assertNotNull(KeyCode.RIGHT);
        assertNotNull(KeyCode.ENTER);
        assertNotNull(KeyCode.ESC);
        assertNotNull(KeyCode.TAB);
        assertNotNull(KeyCode.F1);
        assertNotNull(KeyCode.F12);
    }
}
