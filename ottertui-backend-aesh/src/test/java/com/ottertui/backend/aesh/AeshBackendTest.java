package com.ottertui.backend.aesh;

import com.ottertui.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIf;

import static org.junit.jupiter.api.Assertions.*;

class AeshBackendTest {

    static boolean isTtyAvailable() {
        return System.console() != null;
    }

    @Test
    @EnabledIf("isTtyAvailable")
    @DisplayName("constructs without error")
    void constructs() throws Exception {
        AeshBackend backend = new AeshBackend();
        assertNotNull(backend);
        backend.exitRawMode();
    }

    @Test
    @EnabledIf("isTtyAvailable")
    @DisplayName("returns terminal size")
    void returnsSize() throws Exception {
        AeshBackend backend = new AeshBackend();
        TerminalBackend.TerminalSize size = backend.size();
        assertNotNull(size);
        assertTrue(size.width() > 0 || size.width() == 80);
        backend.exitRawMode();
    }

    @Test
    @EnabledIf("isTtyAvailable")
    @DisplayName("readInput returns empty when no input")
    void readInputEmpty() throws Exception {
        AeshBackend backend = new AeshBackend();
        var event = backend.readInput();
        assertNotNull(event);
        assertTrue(event.isEmpty());
        backend.exitRawMode();
    }

    @Test
    @EnabledIf("isTtyAvailable")
    @DisplayName("flush does not throw")
    void flushDoesNotThrow() throws Exception {
        AeshBackend backend = new AeshBackend();
        Buffer b = new Buffer(10, 5);
        b.setString(0, 0, "Hi", Style.DEFAULT);
        backend.flush(b);
        backend.exitRawMode();
    }

    @Test
    @EnabledIf("isTtyAvailable")
    @DisplayName("showCursor and hideCursor do not throw")
    void cursorVisibility() throws Exception {
        AeshBackend backend = new AeshBackend();
        backend.hideCursor();
        backend.showCursor();
        backend.exitRawMode();
    }

    @Test
    @EnabledIf("isTtyAvailable")
    @DisplayName("clearScreen does not throw")
    void clearScreen() throws Exception {
        AeshBackend backend = new AeshBackend();
        backend.clearScreen();
        backend.exitRawMode();
    }

    @Test
    @EnabledIf("isTtyAvailable")
    @DisplayName("enterRawMode and exitRawMode transitions")
    void rawModeTransitions() throws Exception {
        AeshBackend backend = new AeshBackend();
        backend.enterRawMode();
        backend.exitRawMode();
        // exitRawMode is idempotent, second call should not throw
        assertDoesNotThrow(backend::exitRawMode);
    }
}
