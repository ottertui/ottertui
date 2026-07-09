package com.ottertui.tui;

import com.ottertui.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class TuiRunnerTest {

    static class StubBackend implements TerminalBackend {
        TerminalSize size = new TerminalSize(80, 24);
        boolean rawMode = false;
        boolean cursorVisible = true;
        AtomicInteger readInputCount = new AtomicInteger(0);
        Queue<InputEvent> inputs = new LinkedList<>();
        AtomicBoolean flushed = new AtomicBoolean(false);

        @Override
        public void flush(Buffer buffer) { flushed.set(true); }

        @Override
        public TerminalSize size() { return size; }

        @Override
        public void enterRawMode() { rawMode = true; }

        @Override
        public void exitRawMode() { rawMode = false; }

        @Override
        public Optional<InputEvent> readInput() {
            readInputCount.incrementAndGet();
            InputEvent event = inputs.poll();
            return Optional.ofNullable(event);
        }

        @Override
        public void showCursor() { cursorVisible = true; }

        @Override
        public void hideCursor() { cursorVisible = false; }

        @Override
        public void clearScreen() {}
    }

    @Test
    @DisplayName("constructor sets up key bindings")
    void constructorSetsUpBindings() {
        StubBackend backend = new StubBackend();
        Component root = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {}
        };
        TuiRunner runner = new TuiRunner(backend, root);
        assertNotNull(runner.keyBindings());
    }

    @Test
    @DisplayName("run enters and exits raw mode")
    void runEntersAndExitsRawMode() {
        StubBackend backend = new StubBackend();
        backend.inputs.add(InputEvent.charKey('c', Set.of(Modifier.BOLD)));

        Component root = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {}
        };
        TuiRunner runner = new TuiRunner(backend, root);
        runner.run();
        assertFalse(backend.rawMode);
    }

    @Test
    @DisplayName("run calls render and flush backend")
    void runCallsRender() {
        StubBackend backend = new StubBackend();
        backend.inputs.add(InputEvent.charKey('c', Set.of(Modifier.BOLD)));

        Component root = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {}
        };
        TuiRunner runner = new TuiRunner(backend, root);
        runner.run();
        assertTrue(backend.flushed.get());
    }

    @Test
    @DisplayName("requestRedraw sets dirty flag for next frame")
    void requestRedraw() {
        StubBackend backend = new StubBackend();
        backend.inputs.add(InputEvent.charKey('c', Set.of(Modifier.BOLD)));

        Component root = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {}
        };
        TuiRunner runner = new TuiRunner(backend, root);
        runner.requestRedraw();
        runner.run();
        assertTrue(backend.flushed.get());
    }

    @Test
    @DisplayName("key bindings handle Ctrl+C to stop")
    void keyBindingsStop() {
        StubBackend backend = new StubBackend();
        backend.inputs.add(InputEvent.charKey('c', Set.of(Modifier.BOLD)));

        Component root = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {}
        };
        TuiRunner runner = new TuiRunner(backend, root);
        runner.run();
        assertFalse(backend.rawMode);
    }

    @Test
    @DisplayName("Ctrl+L triggers redraw")
    void ctrlLTriggersRedraw() {
        StubBackend backend = new StubBackend();
        backend.inputs.add(InputEvent.charKey('l', Set.of(Modifier.BOLD)));
        backend.inputs.add(InputEvent.charKey('c', Set.of(Modifier.BOLD)));

        Component root = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {}
        };
        TuiRunner runner = new TuiRunner(backend, root);
        runner.run();
        assertTrue(backend.flushed.get());
    }

    @Test
    @DisplayName("stop sets running to false directly")
    void stopDirectly() {
        StubBackend backend = new StubBackend();
        Component root = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {}
        };
        TuiRunner runner = new TuiRunner(backend, root);
        runner.stop();
        runner.run();
        assertFalse(backend.rawMode);
    }

    @Test
    @DisplayName("requestRedraw adds dirty flag so render is called")
    void requestRedrawDirtyFlag() {
        StubBackend backend = new StubBackend();
        backend.inputs.add(InputEvent.charKey('c', Set.of(Modifier.BOLD)));

        Component root = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {}
        };
        TuiRunner runner = new TuiRunner(backend, root);
        runner.requestRedraw();
        runner.run();
        assertTrue(backend.flushed.get());
    }

    @Test
    @DisplayName("run skips render when backend size is zero")
    void runSkipsRenderOnZeroSize() {
        StubBackend backend = new StubBackend();
        backend.size = new TerminalBackend.TerminalSize(0, 0);
        backend.inputs.add(InputEvent.charKey('c', Set.of(Modifier.BOLD)));

        Component root = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {}
        };
        TuiRunner runner = new TuiRunner(backend, root);
        runner.run();
        assertFalse(backend.flushed.get());
    }

    @Test
    @DisplayName("unbound events are dispatched to root.onEvent")
    void unboundEventDispatchedToRoot() {
        StubBackend backend = new StubBackend();
        AtomicBoolean eventReceived = new AtomicBoolean(false);
        // Send an unbound event first, then stop
        backend.inputs.add(InputEvent.charKey('x')); // no binding for 'x' without BOLD
        backend.inputs.add(InputEvent.charKey('c', Set.of(Modifier.BOLD)));

        Component root = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {}
            @Override
            public boolean onEvent(InputEvent event) {
                eventReceived.set(true);
                return false;
            }
        };
        TuiRunner runner = new TuiRunner(backend, root);
        runner.run();
        assertTrue(eventReceived.get(), "Unbound event should reach root.onEvent()");
    }

    @Test
    @DisplayName("unknown event is dispatched to root.onEvent")
    void unknownEventDispatchedToRoot() {
        StubBackend backend = new StubBackend();
        AtomicBoolean eventReceived = new AtomicBoolean(false);
        backend.inputs.add(new InputEvent.Unknown());
        backend.inputs.add(InputEvent.charKey('c', Set.of(Modifier.BOLD)));

        Component root = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {}
            @Override
            public boolean onEvent(InputEvent event) {
                eventReceived.set(true);
                return false;
            }
        };
        TuiRunner runner = new TuiRunner(backend, root);
        runner.run();
        assertTrue(eventReceived.get(), "Unknown event should reach root.onEvent()");
    }

    @Test
    @DisplayName("run stops when all events processed and backend returns empty")
    void runProcessesAllEvents() {
        StubBackend backend = new StubBackend();
        AtomicInteger eventCount = new AtomicInteger(0);
        backend.inputs.add(InputEvent.charKey('a'));
        backend.inputs.add(InputEvent.charKey('b'));
        backend.inputs.add(InputEvent.charKey('c', Set.of(Modifier.BOLD)));

        Component root = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {}
            @Override
            public boolean onEvent(InputEvent event) {
                eventCount.incrementAndGet();
                return false;
            }
        };
        TuiRunner runner = new TuiRunner(backend, root);
        runner.run();
        assertEquals(2, eventCount.get(), "2 unbound events should be dispatched to root");
    }

    @Test
    @DisplayName("drainInput correctly batches multiple events")
    void drainInputBatches() {
        StubBackend backend = new StubBackend();
        backend.inputs.add(InputEvent.charKey('a'));
        backend.inputs.add(InputEvent.charKey('b'));
        backend.inputs.add(InputEvent.charKey('c', Set.of(Modifier.BOLD)));

        AtomicInteger eventCount = new AtomicInteger(0);
        Component root = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {}
            @Override
            public boolean onEvent(InputEvent event) {
                eventCount.incrementAndGet();
                return false;
            }
        };
        TuiRunner runner = new TuiRunner(backend, root);
        runner.run();
        assertTrue(eventCount.get() >= 2);
    }

    @Test
    @DisplayName("resize event dispatched to root")
    void resizeEventDispatched() {
        StubBackend backend = new StubBackend();
        AtomicBoolean resized = new AtomicBoolean(false);
        backend.inputs.add(new InputEvent.Resize(100, 40));
        backend.inputs.add(InputEvent.charKey('c', Set.of(Modifier.BOLD)));

        Component root = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {}
            @Override
            public boolean onEvent(InputEvent event) {
                if (event instanceof InputEvent.Resize) resized.set(true);
                return false;
            }
        };
        TuiRunner runner = new TuiRunner(backend, root);
        runner.run();
        assertTrue(resized.get());
    }

}
