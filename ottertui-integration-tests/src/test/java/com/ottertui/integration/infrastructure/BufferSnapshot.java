package com.ottertui.integration.infrastructure;

import com.ottertui.core.Buffer;

/**
 * Converts a Buffer to plain text for snapshot comparison.
 */
public final class BufferSnapshot {

    private BufferSnapshot() { }

    public static String capture(Buffer buffer) {
        var sb = new StringBuilder();
        for (int y = 0; y < buffer.height(); y++) {
            for (int x = 0; x < buffer.width(); x++) {
                sb.append(buffer.getCell(x, y).ch());
            }
            if (y < buffer.height() - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    /** Capture a rectangular region of the buffer. */
    public static String captureRegion(Buffer buffer, int x, int y, int w, int h) {
        var sb = new StringBuilder();
        for (int row = y; row < y + h && row < buffer.height(); row++) {
            for (int col = x; col < x + w && col < buffer.width(); col++) {
                sb.append(buffer.getCell(col, row).ch());
            }
            if (row < y + h - 1 && row < buffer.height() - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    /** Check if a buffer region contains the given string at the given position. */
    public static String readString(Buffer buffer, int x, int y, int len) {
        var sb = new StringBuilder();
        for (int i = 0; i < len && x + i < buffer.width(); i++) {
            sb.append(buffer.getCell(x + i, y).ch());
        }
        return sb.toString();
    }
}
