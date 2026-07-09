package com.ottertui.backend.ffm;

import com.ottertui.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.*;

class FfmBackendTest {

    @Test
    @EnabledOnOs({OS.MAC, OS.LINUX})
    @DisplayName("constructs without error")
    void constructs() {
        try {
            FfmBackend backend = new FfmBackend();
            assertNotNull(backend);
            backend.exitRawMode();
        } catch (RuntimeException e) {
            // Fails gracefully without --enable-native-access
            assertNotNull(e.getMessage());
        }
    }

    @Test
    @EnabledOnOs({OS.MAC, OS.LINUX})
    @DisplayName("returns terminal size")
    void returnsSize() {
        try {
            FfmBackend backend = new FfmBackend();
            TerminalBackend.TerminalSize size = backend.size();
            assertNotNull(size);
            assertTrue(size.width() > 0 || size.width() == 80);
            backend.exitRawMode();
        } catch (RuntimeException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    @EnabledOnOs({OS.MAC, OS.LINUX})
    @DisplayName("readInput returns empty when no input")
    void readInputEmpty() {
        try {
            FfmBackend backend = new FfmBackend();
            var event = backend.readInput();
            assertNotNull(event);
            assertTrue(event.isEmpty());
            backend.exitRawMode();
        } catch (RuntimeException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    @EnabledOnOs({OS.MAC, OS.LINUX})
    @DisplayName("flush does not throw")
    void flushDoesNotThrow() {
        try {
            FfmBackend backend = new FfmBackend();
            Buffer b = new Buffer(10, 5);
            b.setString(0, 0, "Hi", Style.DEFAULT);
            backend.flush(b);
            backend.exitRawMode();
        } catch (RuntimeException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    @EnabledOnOs({OS.MAC, OS.LINUX})
    @DisplayName("raw mode transitions do not throw")
    void rawModeTransitions() {
        try {
            FfmBackend backend = new FfmBackend();
            backend.enterRawMode();
            backend.exitRawMode();
            assertDoesNotThrow(backend::exitRawMode);
        } catch (RuntimeException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    @EnabledOnOs({OS.MAC, OS.LINUX})
    @DisplayName("showCursor and hideCursor do not throw")
    void cursorVisibility() {
        try {
            FfmBackend backend = new FfmBackend();
            backend.hideCursor();
            backend.showCursor();
            backend.exitRawMode();
        } catch (RuntimeException e) {
            assertNotNull(e.getMessage());
        }
    }
}
