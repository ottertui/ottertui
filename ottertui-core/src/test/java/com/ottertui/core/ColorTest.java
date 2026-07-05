package com.ottertui.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    @DisplayName("Reset is a singleton")
    void resetIsReset() {
        assertInstanceOf(Color.Reset.class, Color.RESET);
    }

    @Test
    @DisplayName("Indexed color stores index")
    void indexedStoresIndex() {
        Color.Indexed c = new Color.Indexed(42);
        assertEquals(42, c.index());
    }

    @Test
    @DisplayName("Rgb stores components")
    void rgbStoresComponents() {
        Color.Rgb c = new Color.Rgb(10, 20, 30);
        assertEquals(10, c.r());
        assertEquals(20, c.g());
        assertEquals(30, c.b());
    }

    @Test
    @DisplayName("named color constants are Rgb instances")
    void namedColorsAreRgb() {
        assertInstanceOf(Color.Rgb.class, Color.RED);
        assertInstanceOf(Color.Rgb.class, Color.GREEN);
        assertInstanceOf(Color.Rgb.class, Color.BLUE);
        assertInstanceOf(Color.Rgb.class, Color.BLACK);
        assertInstanceOf(Color.Rgb.class, Color.WHITE);
        assertInstanceOf(Color.Rgb.class, Color.YELLOW);
        assertInstanceOf(Color.Rgb.class, Color.MAGENTA);
        assertInstanceOf(Color.Rgb.class, Color.CYAN);
        assertInstanceOf(Color.Rgb.class, Color.GRAY);
        assertInstanceOf(Color.Rgb.class, Color.DARK_GRAY);
    }

    @Test
    @DisplayName("RED has correct RGB values")
    void redValues() {
        Color.Rgb red = (Color.Rgb) Color.RED;
        assertEquals(255, red.r());
        assertEquals(0, red.g());
        assertEquals(0, red.b());
    }

    @Test
    @DisplayName("GREEN has correct RGB values")
    void greenValues() {
        Color.Rgb green = (Color.Rgb) Color.GREEN;
        assertEquals(0, green.r());
        assertEquals(255, green.g());
        assertEquals(0, green.b());
    }

    @Test
    @DisplayName("BLUE has correct RGB values")
    void blueValues() {
        Color.Rgb blue = (Color.Rgb) Color.BLUE;
        assertEquals(0, blue.r());
        assertEquals(0, blue.g());
        assertEquals(255, blue.b());
    }

    @Test
    @DisplayName("all light color variants exist")
    void lightColors() {
        assertNotNull(Color.LIGHT_RED);
        assertNotNull(Color.LIGHT_GREEN);
        assertNotNull(Color.LIGHT_YELLOW);
        assertNotNull(Color.LIGHT_BLUE);
        assertNotNull(Color.LIGHT_MAGENTA);
        assertNotNull(Color.LIGHT_CYAN);
    }
}
