package com.ottertui.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class ModifierTest {

    @Test
    @DisplayName("all modifier values exist")
    void allValuesExist() {
        assertEquals(8, Modifier.values().length);
        assertNotNull(Modifier.valueOf("BOLD"));
        assertNotNull(Modifier.valueOf("DIM"));
        assertNotNull(Modifier.valueOf("ITALIC"));
        assertNotNull(Modifier.valueOf("UNDERLINE"));
        assertNotNull(Modifier.valueOf("REVERSED"));
        assertNotNull(Modifier.valueOf("CROSSED_OUT"));
        assertNotNull(Modifier.valueOf("SLOW_BLINK"));
        assertNotNull(Modifier.valueOf("RAPID_BLINK"));
    }
}
