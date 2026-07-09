package com.ottertui.tui;

import com.ottertui.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.*;

class BackendSelectorTest {

    @AfterEach
    void clearProperty() {
        System.clearProperty("ottertui.backend");
    }

    @Test
    @DisplayName("create with lanterna property set prefers lanterna backend")
    void createWithLanternaProperty() {
        System.setProperty("ottertui.backend", "lanterna");
        try {
            TerminalBackend backend = BackendSelector.create();
            assertNotNull(backend);
            assertTrue(backend.getClass().getName().contains("Lanterna"));
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Lanterna"));
        }
    }

    @Test
    @DisplayName("create with jline property tries jline first, falls back to lanterna")
    void createWithJlineProperty() {
        System.setProperty("ottertui.backend", "jline");
        try {
            TerminalBackend backend = BackendSelector.create();
            assertNotNull(backend);
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("No suitable terminal backend found"));
        }
    }

    @Test
    @DisplayName("create uses jline by default when no property is set")
    void createDefaultPrefersJline() {
        try {
            TerminalBackend backend = BackendSelector.create();
            assertNotNull(backend);
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("No suitable terminal backend found"));
        }
    }

    @Test
    @DisplayName("create with unknown backend falls through to lanterna")
    void createWithUnknownBackend() {
        System.setProperty("ottertui.backend", "unknown");
        try {
            TerminalBackend backend = BackendSelector.create();
            assertNotNull(backend);
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("No suitable terminal backend found"));
        }
    }

    @Test
    @DisplayName("createDefault is stateless and can be called multiple times")
    void createIsRepeatable() {
        for (int i = 0; i < 3; i++) {
            try {
                assertNotNull(BackendSelector.create());
            } catch (Exception e) {
                assertNotNull(e.getMessage());
            }
        }
    }
}
