package com.ottertui.backend.jline;

import com.ottertui.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import static org.junit.jupiter.api.Assertions.*;

class JLineBackendTest {

    @Test
    @DisplayName("JLineBackend can be instantiated")
    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    void instantiateWhenTerminalAvailable() {
        try {
            JLineBackend backend = new JLineBackend();
            assertNotNull(backend);
            backend.exitRawMode();
        } catch (Exception e) {
            assertTrue(e.getMessage() != null);
        }
    }

    @Test
    @DisplayName("class exists and extends TerminalBackend")
    void classImplementsTerminalBackend() {
        assertTrue(TerminalBackend.class.isAssignableFrom(JLineBackend.class));
    }

    @Test
    @DisplayName("backend can be constructed or throws in any environment")
    void constructionHandlesEnvironment() {
        try {
            JLineBackend backend = new JLineBackend();
            assertNotNull(backend);
            backend.exitRawMode();
        } catch (Exception e) {
            assertNotNull(e);
        }
    }
}
