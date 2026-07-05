package com.ottertui.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StyleTest {

    @Test
    @DisplayName("DEFAULT has reset colors and empty modifiers")
    void defaultStyle() {
        assertEquals(Color.RESET, Style.DEFAULT.foreground());
        assertEquals(Color.RESET, Style.DEFAULT.background());
        assertTrue(Style.DEFAULT.modifiers().isEmpty());
    }

    @Test
    @DisplayName("fg sets foreground color")
    void fgSetsForeground() {
        Style s = Style.DEFAULT.fg(Color.RED);
        assertEquals(Color.RED, s.foreground());
        assertEquals(Color.RESET, s.background());
    }

    @Test
    @DisplayName("bg sets background color")
    void bgSetsBackground() {
        Style s = Style.DEFAULT.bg(Color.BLUE);
        assertEquals(Color.RESET, s.foreground());
        assertEquals(Color.BLUE, s.background());
    }

    @Test
    @DisplayName("bold modifier")
    void boldModifier() {
        Style s = Style.DEFAULT.bold();
        assertTrue(s.modifiers().contains(Modifier.BOLD));
    }

    @Test
    @DisplayName("dim modifier")
    void dimModifier() {
        Style s = Style.DEFAULT.dim();
        assertTrue(s.modifiers().contains(Modifier.DIM));
    }

    @Test
    @DisplayName("italic modifier")
    void italicModifier() {
        Style s = Style.DEFAULT.italic();
        assertTrue(s.modifiers().contains(Modifier.ITALIC));
    }

    @Test
    @DisplayName("underline modifier")
    void underlineModifier() {
        Style s = Style.DEFAULT.underline();
        assertTrue(s.modifiers().contains(Modifier.UNDERLINE));
    }

    @Test
    @DisplayName("reversed modifier")
    void reversedModifier() {
        Style s = Style.DEFAULT.reversed();
        assertTrue(s.modifiers().contains(Modifier.REVERSED));
    }

    @Test
    @DisplayName("crossedOut modifier")
    void crossedOutModifier() {
        Style s = Style.DEFAULT.crossedOut();
        assertTrue(s.modifiers().contains(Modifier.CROSSED_OUT));
    }

    @Test
    @DisplayName("removeModifier removes existing modifier")
    void removeModifier() {
        Style s = Style.DEFAULT.bold().italic();
        Style removed = s.removeModifier(Modifier.BOLD);
        assertFalse(removed.modifiers().contains(Modifier.BOLD));
        assertTrue(removed.modifiers().contains(Modifier.ITALIC));
    }

    @Test
    @DisplayName("chaining multiple modifiers")
    void chainingModifiers() {
        Style s = Style.DEFAULT.bold().italic().underline();
        assertEquals(3, s.modifiers().size());
        assertTrue(s.modifiers().contains(Modifier.BOLD));
        assertTrue(s.modifiers().contains(Modifier.ITALIC));
        assertTrue(s.modifiers().contains(Modifier.UNDERLINE));
    }

    @Test
    @DisplayName("modifiers set is unmodifiable")
    void modifiersSetUnmodifiable() {
        Style s = Style.DEFAULT.bold();
        assertThrows(UnsupportedOperationException.class, () -> s.modifiers().add(Modifier.DIM));
    }

    @Test
    @DisplayName("fluent style building")
    void fluentBuilding() {
        Style s = Style.DEFAULT
            .fg(Color.GREEN)
            .bg(Color.BLACK)
            .bold()
            .italic();
        assertEquals(Color.GREEN, s.foreground());
        assertEquals(Color.BLACK, s.background());
        assertEquals(2, s.modifiers().size());
    }

    @Test
    @DisplayName("custom constructor preserves values")
    void customConstructor() {
        Style s = new Style(Color.RED, Color.BLUE, Set.of(Modifier.BOLD, Modifier.UNDERLINE));
        assertEquals(Color.RED, s.foreground());
        assertEquals(Color.BLUE, s.background());
        assertEquals(2, s.modifiers().size());
    }
}
