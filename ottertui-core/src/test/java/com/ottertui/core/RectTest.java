package com.ottertui.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class RectTest {

    @Test
    @DisplayName("static factory creates Rect")
    void staticFactory() {
        Rect r = Rect.of(1, 2, 10, 20);
        assertEquals(1, r.x());
        assertEquals(2, r.y());
        assertEquals(10, r.width());
        assertEquals(20, r.height());
    }

    @Test
    @DisplayName("record constructor creates Rect")
    void recordConstructor() {
        Rect r = new Rect(5, 10, 15, 25);
        assertEquals(5, r.x());
        assertEquals(10, r.y());
        assertEquals(15, r.width());
        assertEquals(25, r.height());
    }

    @Test
    @DisplayName("inner with uniform margin")
    void innerUniformMargin() {
        Rect r = new Rect(0, 0, 20, 10);
        Rect inner = r.inner(2);
        assertEquals(2, inner.x());
        assertEquals(2, inner.y());
        assertEquals(16, inner.width());
        assertEquals(6, inner.height());
    }

    @Test
    @DisplayName("inner with separate margins")
    void innerSeparateMargins() {
        Rect r = new Rect(0, 0, 20, 10);
        Rect inner = r.inner(1, 3);
        assertEquals(3, inner.x());
        assertEquals(1, inner.y());
        assertEquals(14, inner.width());
        assertEquals(8, inner.height());
    }

    @Test
    @DisplayName("inner margin too large clamps to zero")
    void innerMarginClampsToZero() {
        Rect r = new Rect(0, 0, 5, 3);
        Rect inner = r.inner(3);
        assertEquals(0, inner.width());
        assertEquals(0, inner.height());
    }

    @Test
    @DisplayName("inner margin zero does nothing")
    void innerMarginZero() {
        Rect r = new Rect(1, 2, 10, 20);
        Rect inner = r.inner(0);
        assertEquals(1, inner.x());
        assertEquals(2, inner.y());
        assertEquals(10, inner.width());
        assertEquals(20, inner.height());
    }
}
