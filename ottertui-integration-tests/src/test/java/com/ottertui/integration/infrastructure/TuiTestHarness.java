package com.ottertui.integration.infrastructure;

import com.ottertui.core.InputEvent;
import com.ottertui.core.Rect;
import com.ottertui.tui.Component;
import com.ottertui.tui.TuiRunner;

/**
 * One-shot harness that renders a Component through a StubBackend
 * without starting the full event loop. Useful for snapshot-style tests.
 */
public class TuiTestHarness {
    private final StubBackend backend;
    private final Component root;
    private final TuiRunner runner;

    public TuiTestHarness(Component root, int width, int height) {
        this.backend = new StubBackend();
        this.backend.withSize(width, height);
        this.root = root;
        this.runner = new TuiRunner(backend, root);
    }

    /** Render a single frame and return the backend. */
    public StubBackend render() {
        // Manually trigger one render cycle by requesting redraw and running briefly
        runner.requestRedraw();
        // Feed a stop signal after one frame
        backend.feeding(InputEvent.charKey('q'));
        runner.run();
        return backend;
    }

    /** Feed events then render one frame. */
    public StubBackend feedAndRender(InputEvent... events) {
        backend.feeding(events);
        return render();
    }

    public StubBackend backend() {
        return backend;
    }

    public Component root() {
        return root;
    }

    public TuiRunner runner() {
        return runner;
    }

    /** Convenience: render root component directly to a buffer at given size. */
    public static StubBackend renderStatic(Component component, int width, int height) {
        var backend = new StubBackend();
        backend.withSize(width, height);
        // Direct render without event loop
        var buffer = new com.ottertui.core.Buffer(width, height);
        component.render(new Rect(0, 0, width, height), buffer);
        backend.flush(buffer);
        return backend;
    }
}
