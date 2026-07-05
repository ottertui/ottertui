package com.ottertui.widgets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class BorderStyleTest {

    @Test
    @DisplayName("PLAIN style characters")
    void plainStyle() {
        assertEquals("─", BorderStyle.PLAIN.horizontal);
        assertEquals("│", BorderStyle.PLAIN.vertical);
        assertEquals("┌", BorderStyle.PLAIN.topLeft);
        assertEquals("┐", BorderStyle.PLAIN.topRight);
        assertEquals("└", BorderStyle.PLAIN.bottomLeft);
        assertEquals("┘", BorderStyle.PLAIN.bottomRight);
    }

    @Test
    @DisplayName("ROUNDED style characters")
    void roundedStyle() {
        assertEquals("─", BorderStyle.ROUNDED.horizontal);
        assertEquals("│", BorderStyle.ROUNDED.vertical);
        assertEquals("╭", BorderStyle.ROUNDED.topLeft);
        assertEquals("╮", BorderStyle.ROUNDED.topRight);
        assertEquals("╰", BorderStyle.ROUNDED.bottomLeft);
        assertEquals("╯", BorderStyle.ROUNDED.bottomRight);
    }

    @Test
    @DisplayName("DOUBLE style characters")
    void doubleStyle() {
        assertEquals("═", BorderStyle.DOUBLE.horizontal);
        assertEquals("║", BorderStyle.DOUBLE.vertical);
        assertEquals("╔", BorderStyle.DOUBLE.topLeft);
        assertEquals("╗", BorderStyle.DOUBLE.topRight);
        assertEquals("╚", BorderStyle.DOUBLE.bottomLeft);
        assertEquals("╝", BorderStyle.DOUBLE.bottomRight);
    }

    @Test
    @DisplayName("THICK style characters")
    void thickStyle() {
        assertEquals("━", BorderStyle.THICK.horizontal);
        assertEquals("┃", BorderStyle.THICK.vertical);
        assertEquals("┏", BorderStyle.THICK.topLeft);
        assertEquals("┓", BorderStyle.THICK.topRight);
        assertEquals("┗", BorderStyle.THICK.bottomLeft);
        assertEquals("┛", BorderStyle.THICK.bottomRight);
    }

    @Test
    @DisplayName("all styles have non-null characters")
    void allStylesNonNull() {
        for (BorderStyle style : BorderStyle.values()) {
            assertNotNull(style.horizontal);
            assertNotNull(style.vertical);
            assertNotNull(style.topLeft);
            assertNotNull(style.topRight);
            assertNotNull(style.bottomLeft);
            assertNotNull(style.bottomRight);
        }
    }
}
