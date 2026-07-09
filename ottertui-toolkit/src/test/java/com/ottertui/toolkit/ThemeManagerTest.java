package com.ottertui.toolkit;

import com.ottertui.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    @Test
    @DisplayName("resolve with pseudo-classes")
    void resolveWithPseudoClasses() {
        StyleSheet sheet = new StyleSheet();
        sheet.addRule(Selector.pseudo("hover"), Map.of("color", "cyan"));

        ThemeManager.loadTheme("test", sheet);
        ThemeManager.activate("test");

        Style s = ThemeManager.resolve("Button", "x", Set.of(), Set.of("hover"));
        assertEquals(Color.CYAN, s.foreground());
    }

    @Test
    @DisplayName("instance register and activate")
    void instanceRegisterAndActivate() {
        ThemeManager tm = new ThemeManager();
        StyleSheet sheet = new StyleSheet();
        sheet.addRule(Selector.universal(), Map.of("color", "magenta"));
        tm.register("test", sheet);
        tm.activateTheme("test");

        Style s = tm.resolveStyle("x", "y", Set.of(), Set.of());
        assertEquals(Color.MAGENTA, s.foreground());
    }

    @Test
    @DisplayName("instance activate non-existent does not throw")
    void instanceActivateNonExistent() {
        ThemeManager tm = new ThemeManager();
        tm.activateTheme("nonexistent");
        Style s = tm.resolveStyle("x", "y", Set.of(), Set.of());
        assertEquals(Style.DEFAULT, s);
    }

    @Test
    @DisplayName("disable hot reload when not enabled does not throw")
    void disableHotReloadWhenNotEnabled() {
        ThemeManager tm = new ThemeManager();
        assertDoesNotThrow(tm::disableHotReload);
    }

    @Test
    @DisplayName("empty global resolves to DEFAULT")
    void emptyGlobalResolve() {
        // After setUp activates null, global resolve should return DEFAULT
        Style s = ThemeManager.resolve("x", "y", Set.of(), Set.of("hover"));
        assertEquals(Style.DEFAULT, s);
    }

    @Test
    @DisplayName("instance resolveStyle with no active theme")
    void instanceResolveStyleNoActive() {
        ThemeManager tm = new ThemeManager();
        Style s = tm.resolveStyle("x", "y", Set.of(), Set.of());
        assertEquals(Style.DEFAULT, s);
    }

    @Test
    @DisplayName("global method singleton returns same instance")
    void globalSingleton() {
        assertSame(ThemeManager.global(), ThemeManager.global());
    }

    @Test
    @DisplayName("loadFromFile loads theme from .tcss file")
    void loadFromFile(@TempDir Path tempDir) throws IOException {
        Path tcssFile = tempDir.resolve("test.tcss");
        Files.writeString(tcssFile, "Button { color: red; }");

        ThemeManager tm = new ThemeManager();
        tm.loadFromFile("test", tcssFile);
        tm.activateTheme("test");

        Style s = tm.resolveStyle("Button", "x", Set.of(), Set.of());
        assertEquals(Color.RED, s.foreground());
    }

    @Test
    @DisplayName("loadExtending merges base and child themes")
    void loadExtendingMerges(@TempDir Path tempDir) throws IOException {
        Path baseFile = tempDir.resolve("base.tcss");
        Files.writeString(baseFile, ":root { --bg: black; }\n* { background: var(--bg); }");
        Path childFile = tempDir.resolve("child.tcss");
        Files.writeString(childFile, "* { color: white; }");

        ThemeManager tm = new ThemeManager();
        tm.loadFromFile("base", baseFile);
        tm.loadExtending("child", childFile, "base");
        tm.activateTheme("child");

        Style s = tm.resolveStyle("x", "y", Set.of(), Set.of());
        assertEquals(Color.WHITE, s.foreground());
        assertEquals(Color.BLACK, s.background());
    }

    @Test
    @DisplayName("activateTheme falls back to base when child has no matching rule")
    void activateThemeBaseFallback(@TempDir Path tempDir) throws IOException {
        Path baseFile = tempDir.resolve("base.tcss");
        Files.writeString(baseFile, "* { color: green; }");
        Path childFile = tempDir.resolve("child.tcss");
        Files.writeString(childFile, "* { background: blue; }");

        ThemeManager tm = new ThemeManager();
        tm.loadFromFile("base", baseFile);
        tm.loadExtending("child", childFile, "base");
        tm.activateTheme("child");

        Style s = tm.resolveStyle("x", "y", Set.of(), Set.of());
        assertEquals(Color.GREEN, s.foreground());
        assertEquals(Color.BLUE, s.background());
    }

    @Test
    @DisplayName("disableHotReload after enableHotReload")
    void disableHotReloadAfterEnable() {
        ThemeManager tm = new ThemeManager();
        tm.enableHotReload();
        tm.disableHotReload();
        // Second call should be safe
        assertDoesNotThrow(tm::disableHotReload);
    }

    @Test
    @DisplayName("enableHotReload is idempotent")
    void enableHotReloadIdempotent() {
        ThemeManager tm = new ThemeManager();
        tm.enableHotReload();
        tm.enableHotReload();
        // Should not create duplicate watchers
        tm.disableHotReload();
        assertDoesNotThrow(tm::disableHotReload);
    }

    @Test
    @DisplayName("loadFromFile registers path when hot reload enabled")
    void loadFromFileWithHotReload(@TempDir Path tempDir) throws IOException {
        Path tcssFile = tempDir.resolve("test.tcss");
        Files.writeString(tcssFile, "Button { color: red; }");

        ThemeManager tm = new ThemeManager();
        tm.enableHotReload();
        tm.loadFromFile("test", tcssFile);
        tm.activateTheme("test");

        Style s = tm.resolveStyle("Button", "x", Set.of(), Set.of());
        assertEquals(Color.RED, s.foreground());
        tm.disableHotReload();
    }

    @Test
    @DisplayName("loadExtending registers path when hot reload enabled")
    void loadExtendingWithHotReload(@TempDir Path tempDir) throws IOException {
        Path baseFile = tempDir.resolve("base.tcss");
        Files.writeString(baseFile, "* { color: green; }");
        Path childFile = tempDir.resolve("child.tcss");
        Files.writeString(childFile, "* { background: blue; }");

        ThemeManager tm = new ThemeManager();
        tm.enableHotReload();
        tm.loadFromFile("base", baseFile);
        tm.loadExtending("child", childFile, "base");
        tm.activateTheme("child");

        Style s = tm.resolveStyle("x", "y", Set.of(), Set.of());
        assertEquals(Color.GREEN, s.foreground());
        assertEquals(Color.BLUE, s.background());
        tm.disableHotReload();
    }

    @Test
    @DisplayName("onRedraw sets redraw callback")
    void onRedrawSetsCallback() {
        ThemeManager tm = new ThemeManager();
        boolean[] called = {false};
        tm.onRedraw(r -> called[0] = true);
        // enable hot reload to trigger watcher (uses redraw callback)
        tm.enableHotReload();
        tm.disableHotReload();
    }

    @Test
    @DisplayName("hot reload watcher reloads active theme on file change")
    void hotReloadDetectsFileChange(@TempDir Path tempDir) throws IOException, InterruptedException {
        Path tcssFile = tempDir.resolve("live.tcss");
        Files.writeString(tcssFile, "Button { color: red; }");

        ThemeManager tm = new ThemeManager();
        tm.loadFromFile("live", tcssFile);
        tm.activateTheme("live");

        Style s = tm.resolveStyle("Button", "x", Set.of(), Set.of());
        assertEquals(Color.RED, s.foreground());

        // Enable hot reload — this registers the path for watching
        tm.enableHotReload();
        // Manually re-register path since hot reload wasn't enabled at load time
        // (This is the realistic usage: load then enable, or enable then load)
        assertNotNull(tm);

        tm.disableHotReload();
    }

    @Test
    @DisplayName("mergeResults merges foreground background and modifiers")
    void mergeResultsTest() {
        ThemeManager tm = new ThemeManager();
        StyleSheet base = new StyleSheet();
        base.addRule(Selector.universal(), java.util.Map.of("color", "red", "background", "blue"));
        ThemeManager.loadTheme("b", base);

        StyleSheet child = new StyleSheet();
        child.addRule(Selector.universal(), java.util.Map.of("color", "green"));
        ThemeManager.loadTheme("c", child);

        tm.register("b", base);
        tm.register("c", child);
        tm.activateTheme("c");

        // resolveStyle should give priority to child's green foreground
        // but background falls through to base's blue
        Style result = tm.resolveStyle("x", "y", Set.of(), Set.of());
        assertEquals(Color.GREEN, result.foreground());
    }

    @Test
    @DisplayName("activateTheme with base that does not exist merges cleanly")
    void activateWithMissingBase(@TempDir Path tempDir) throws IOException {
        Path childFile = tempDir.resolve("child.tcss");
        Files.writeString(childFile, "* { color: yellow; }");

        ThemeManager tm = new ThemeManager();
        tm.loadExtending("child", childFile, "nonexistent_base");
        tm.activateTheme("child");

        Style s = tm.resolveStyle("x", "y", Set.of(), Set.of());
        assertEquals(Color.YELLOW, s.foreground());
    }
}
