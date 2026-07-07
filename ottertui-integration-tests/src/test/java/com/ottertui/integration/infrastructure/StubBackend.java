package com.ottertui.integration.infrastructure;

import com.ottertui.core.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

/**
 * In-memory TerminalBackend for integration testing.
 *
 * Captures every flush output as a list of strings (ANSI-resolved plain text),
 * accepts injected input events via a queue, and tracks raw mode / cursor state.
 */
public class StubBackend implements TerminalBackend {
    private TerminalSize size = new TerminalSize(80, 24);
    private boolean rawMode;
    private boolean cursorVisible = true;
    private final Queue<InputEvent> inputQueue = new ArrayDeque<>();
    private final List<String> flushHistory = new ArrayList<>();
    private Buffer lastBuffer;

    // ---- configuration ----

    public StubBackend withSize(int w, int h) {
        this.size = new TerminalSize(w, h);
        return this;
    }

    public StubBackend feeding(InputEvent... events) {
        for (var e : events) {
            inputQueue.offer(e);
        }
        return this;
    }

    public void clearInput() {
        inputQueue.clear();
    }

    // ---- inspection ----

    public int flushCount() {
        return flushHistory.size();
    }

    public String lastFlush() {
        return flushHistory.isEmpty() ? "" : flushHistory.get(flushHistory.size() - 1);
    }

    public List<String> flushHistory() {
        return flushHistory;
    }

    public Buffer lastBuffer() {
        return lastBuffer;
    }

    public boolean isRawMode() {
        return rawMode;
    }

    public boolean isCursorVisible() {
        return cursorVisible;
    }

    // ---- TerminalBackend implementation ----

    @Override
    public void flush(Buffer buffer) {
        this.lastBuffer = buffer;
        var sb = new StringBuilder();
        for (int y = 0; y < buffer.height(); y++) {
            for (int x = 0; x < buffer.width(); x++) {
                sb.append(buffer.getCell(x, y).ch());
            }
            if (y < buffer.height() - 1) {
                sb.append('\n');
            }
        }
        flushHistory.add(sb.toString());
    }

    @Override
    public TerminalSize size() {
        return size;
    }

    @Override
    public void enterRawMode() {
        rawMode = true;
    }

    @Override
    public void exitRawMode() {
        rawMode = false;
    }

    @Override
    public Optional<InputEvent> readInput() {
        return Optional.ofNullable(inputQueue.poll());
    }

    @Override
    public void showCursor() {
        cursorVisible = true;
    }

    @Override
    public void hideCursor() {
        cursorVisible = false;
    }

    @Override
    public void clearScreen() {
        // no-op for stub
    }
}
