package com.ottertui.toolkit;

import com.ottertui.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ThemeManagerTest {

    @BeforeEach
    void setUp() {
        ThemeManager.activate(null);
    }

    @Test
    @DisplayName("resolve without active theme returns DEFAULT")
    void resolveNoActive() {
        Style s = ThemeManager.resolve("Button", "x", Set.of());
        assertEquals(Style.DEFAULT, s);
    }

    @Test
    @DisplayName("loadTheme and activate")
    void loadAndActivate() {
        StyleSheet sheet = new StyleSheet();
        sheet.addRule(Selector.universal(), Map.of("color", "red"));

        ThemeManager.loadTheme("dark", sheet);
        ThemeManager.activate("dark");

        Style s = ThemeManager.resolve("Button", "x", Set.of());
        assertEquals(Color.RED, s.foreground());
    }

    @Test
    @DisplayName("activate non-existent theme resolves to DEFAULT")
    void activateNonExistent() {
        ThemeManager.activate("nonexistent");
        Style s = ThemeManager.resolve("Button", "x", Set.of());
        assertEquals(Style.DEFAULT, s);
    }

    @Test
    @DisplayName("switch between themes")
    void switchThemes() {
        StyleSheet dark = new StyleSheet();
        dark.addRule(Selector.universal(), Map.of("background", "black"));
        StyleSheet light = new StyleSheet();
        light.addRule(Selector.universal(), Map.of("background", "white"));

        ThemeManager.loadTheme("dark", dark);
        ThemeManager.loadTheme("light", light);

        ThemeManager.activate("dark");
        assertEquals(Color.BLACK, ThemeManager.resolve("x", "y", Set.of()).background());

        ThemeManager.activate("light");
        assertEquals(Color.WHITE, ThemeManager.resolve("x", "y", Set.of()).background());
    }
}
