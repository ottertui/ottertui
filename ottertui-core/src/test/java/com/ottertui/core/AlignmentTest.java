package com.ottertui.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class AlignmentTest {

    @Test
    @DisplayName("all alignment values exist")
    void allValuesExist() {
        assertEquals(3, Alignment.values().length);
        assertNotNull(Alignment.valueOf("LEFT"));
        assertNotNull(Alignment.valueOf("CENTER"));
        assertNotNull(Alignment.valueOf("RIGHT"));
    }
}
