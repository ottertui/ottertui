package com.ottertui.integration.runner;

import com.ottertui.core.*;
import com.ottertui.integration.infrastructure.StubBackend;
import com.ottertui.tui.Component;
import com.ottertui.tui.TuiRunner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests: TuiRunner + StubBackend + Component.
 * Verifies the full event loop pipeline without a real terminal.
 */
class TuiRunnerIntegrationTest {

    /** Convenience: create a backend that feeds one Ctrl+C to stop the runner. */
    private static StubBackend stoppingBackend() {
        return new StubBackend().feeding(
            InputEvent.charKey('c', Set.of(Modifier.BOLD)));
    }

    @Nested
    @DisplayName("TuiRunner lifecycle")
    class Lifecycle {

        @Test
        @DisplayName("enters and exits raw mode")
        void entersExitsRawMode() {
            var backend = stoppingBackend();
            var root = new Component() {
                @Override
                public void render(Rect area, Buffer buffer) { }
            };
            new TuiRunner(backend, root).run();
            assertFalse(backend.isRawMode(), "Should exit raw mode after run");
        }

        @Test
        @DisplayName("flush is called at least once during run")
        void flushIsCalled() {
            var backend = stoppingBackend();
            var root = new Component() {
                @Override
                public void render(Rect area, Buffer buffer) {
                    buffer.setString(0, 0, "RENDERED", Style.DEFAULT);
                }
            };
            new TuiRunner(backend, root).run();

            assertTrue(backend.flushCount() > 0, "Should have flushed at least once");
            assertTrue(backend.lastFlush().contains("RENDERED"));
        }

        @Test
        @DisplayName("Ctrl+L triggers a redraw")
        void ctrlLTriggersRedraw() {
            var backend = new StubBackend().feeding(
                InputEvent.charKey('l', Set.of(Modifier.BOLD)),
                InputEvent.charKey('c', Set.of(Modifier.BOLD))
            );
            var rendered = new AtomicBoolean(false);
            var root = new Component() {
                @Override
                public void render(Rect area, Buffer buffer) {
                    rendered.set(true);
                }
            };
            new TuiRunner(backend, root).run();

            assertTrue(rendered.get());
            assertTrue(backend.flushCount() > 0);
        }

        @Test
        @DisplayName("requestRedraw forces a flush")
        void requestRedrawForcesFlush() {
            var backend = stoppingBackend();
            var root = new Component() {
                @Override
                public void render(Rect area, Buffer buffer) { }
            };
            var runner = new TuiRunner(backend, root);
            runner.requestRedraw();
            runner.run();

            assertTrue(backend.flushCount() > 0);
        }

        @Test
        @DisplayName("zero-size terminal skips render")
        void zeroSizeTerminalSkipsRender() {
            var backend = new StubBackend().withSize(0, 0);
            backend.feeding(InputEvent.charKey('c', Set.of(Modifier.BOLD)));

            var root = new Component() {
                @Override
                public void render(Rect area, Buffer buffer) { }
            };
            new TuiRunner(backend, root).run();

            assertEquals(0, backend.flushCount(),
                "Should not flush for zero-size terminal");
        }
    }

    @Nested
    @DisplayName("KeyBindings integration")
    class KeyBindings {

        @Test
        @DisplayName("custom key binding fires and stops")
        void customKeyBinding() {
            var backend = new StubBackend().feeding(InputEvent.charKey('x'));
            var fired = new AtomicBoolean(false);

            var root = new Component() {
                @Override
                public void render(Rect area, Buffer buffer) { }
            };
            var runner = new TuiRunner(backend, root);
            runner.keyBindings().bind(KeyCode.CHAR, Set.of(), 'x',
                () -> { fired.set(true); runner.stop(); });

            runner.run();
            assertTrue(fired.get());
        }

        @Test
        @DisplayName("key events reach component onEvent")
        void eventsReachComponent() {
            var backend = new StubBackend().feeding(
                InputEvent.key(KeyCode.UP),
                InputEvent.charKey('c', Set.of(Modifier.BOLD))
            );
            var upReceived = new AtomicBoolean(false);

            var root = new Component() {
                @Override
                public void render(Rect area, Buffer buffer) { }

                @Override
                public boolean onEvent(InputEvent event) {
                    if (event instanceof InputEvent.KeyEvent ke
                        && ke.code() == KeyCode.UP) {
                        upReceived.set(true);
                        return true;
                    }
                    return super.onEvent(event);
                }
            };
            new TuiRunner(backend, root).run();

            assertTrue(upReceived.get());
        }
    }
}
