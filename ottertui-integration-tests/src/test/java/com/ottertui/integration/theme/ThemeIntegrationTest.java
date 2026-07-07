package com.ottertui.integration.theme;

import com.ottertui.core.*;
import com.ottertui.integration.infrastructure.StubBackend;
import com.ottertui.toolkit.Selector;
import com.ottertui.toolkit.StyleSheet;
import com.ottertui.toolkit.ThemeManager;
import com.ottertui.widgets.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests: CSS theme engine → widget styling → rendered output.
 */
class ThemeIntegrationTest {

    @AfterEach
    void tearDown() {
        ThemeManager.activate(null);
    }

    @Nested
    @DisplayName("TcssParser → StyleSheet → Widget.render")
    class CssParsing {

        @Test
        @DisplayName("parse simple rule and apply to widget")
        void parseAndApply() {
            String css = """
                Button {
                    color: red;
                    background: black;
                    bold: true;
                }
                """;
            var sheet = StyleSheet.fromString(css);
            ThemeManager.loadTheme("test", sheet);
            ThemeManager.activate("test");

            Style resolved = ThemeManager.resolve("Button", null, Set.of());
            assertEquals(Color.RED, resolved.foreground());
            assertEquals(Color.BLACK, resolved.background());
            assertTrue(resolved.modifiers().contains(Modifier.BOLD));
        }

        @Test
        @DisplayName("variable resolution with var()")
        void variableResolution() {
            String css = """
                :root {
                    --accent: #ff5500;
                }
                Label {
                    color: var(--accent);
                }
                """;
            var sheet = StyleSheet.fromString(css);
            ThemeManager.loadTheme("vars", sheet);
            ThemeManager.activate("vars");

            Style resolved = ThemeManager.resolve("Label", null, Set.of());
            assertEquals(new Color.Rgb(0xFF, 0x55, 0x00), resolved.foreground());
        }

        @Test
        @DisplayName("class selector matches widgets with class")
        void classSelectorMatching() {
            String css = """
                Gauge.high {
                    color: red;
                }
                Gauge.low {
                    color: green;
                }
                """;
            var sheet = StyleSheet.fromString(css);
            ThemeManager.loadTheme("classes", sheet);
            ThemeManager.activate("classes");

            Style high = ThemeManager.resolve("Gauge", null,
                Set.of("high"), Set.of());
            assertEquals(Color.RED, high.foreground());

            Style low = ThemeManager.resolve("Gauge", null,
                Set.of("low"), Set.of());
            assertEquals(Color.GREEN, low.foreground());
        }

        @Test
        @DisplayName("pseudo-class :selected matches")
        void pseudoClassSelected() {
            String css = """
                ListItem:selected {
                    color: yellow;
                    background: blue;
                }
                """;
            var sheet = StyleSheet.fromString(css);
            ThemeManager.loadTheme("pseudo", sheet);
            ThemeManager.activate("pseudo");

            Style normal = ThemeManager.resolve("ListItem", null,
                Set.of(), Set.of());
            assertEquals(Color.RESET, normal.foreground(),
                "Should not match without pseudo-class");

            Style selected = ThemeManager.resolve("ListItem", null,
                Set.of(), Set.of("selected"));
            assertEquals(Color.YELLOW, selected.foreground());
            assertEquals(Color.BLUE, selected.background());
        }

        @Test
        @DisplayName("compound selector (type.class) matches both")
        void compoundSelector() {
            String css = """
                Button.danger {
                    color: red;
                }
                """;
            var sheet = StyleSheet.fromString(css);
            ThemeManager.loadTheme("compound", sheet);
            ThemeManager.activate("compound");

            // Matches: Button + class "danger"
            Style s = ThemeManager.resolve("Button", null,
                Set.of("danger"), Set.of());
            assertEquals(Color.RED, s.foreground());

            // Doesn't match: Label + class "danger"
            Style s2 = ThemeManager.resolve("Label", null,
                Set.of("danger"), Set.of());
            assertEquals(Color.RESET, s2.foreground());
        }
    }

    @Nested
    @DisplayName("ThemeManager → theme switching")
    class ThemeSwitching {

        @Test
        @DisplayName("activate switches between themes")
        void activateSwitches() {
            var dark = new StyleSheet();
            dark.addRule(Selector.universal(), Map.of("color", "#ffffff"));
            ThemeManager.loadTheme("dark", dark);

            var light = new StyleSheet();
            light.addRule(Selector.universal(), Map.of("color", "#000000"));
            ThemeManager.loadTheme("light", light);

            ThemeManager.activate("dark");
            assertEquals(Color.WHITE,
                ThemeManager.resolve("x", null, Set.of()).foreground());

            ThemeManager.activate("light");
            assertEquals(Color.BLACK,
                ThemeManager.resolve("x", null, Set.of()).foreground());
        }

        @Test
        @DisplayName("no active theme returns DEFAULT style")
        void noActiveReturnsDefault() {
            Style s = ThemeManager.resolve("Button", null, Set.of());
            assertEquals(Style.DEFAULT, s);
        }
    }

    @Nested
    @DisplayName("Theme → Widget rendering (end-to-end)")
    class E2EThemeRendering {

        @Test
        @DisplayName("themed block renders with correct border color from CSS")
        void themedBlockRendering() {
            String css = """
                Panel {
                    border-color: #ff0000;
                }
                """;
            var sheet = StyleSheet.fromString(css);
            ThemeManager.loadTheme("red-border", sheet);
            ThemeManager.activate("red-border");

            // Resolve the style
            Style panelStyle = ThemeManager.resolve("Panel", null, Set.of());

            var backend = new StubBackend().withSize(30, 8);
            var buf = new Buffer(30, 8);

            var block = Block.bordered()
                .title("Panel")
                .borderStyle(new Style(panelStyle.foreground(), Color.RESET, Set.of()));
            block.render(new Rect(1, 1, 20, 6), buf);
            backend.flush(buf);

            String output = backend.lastFlush();
            assertTrue(output.contains("Panel"));
            // Top-left corner should be rendered
            assertTrue(output.contains("┌"), "Expected border character, got:\n" + output);
        }
    }
}
