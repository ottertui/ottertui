package com.ottertui.widgets;

import com.ottertui.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class ParagraphWidgetTest {

    @Test
    @DisplayName("default constructor with text")
    void defaultConstructor() {
        ParagraphWidget w = new ParagraphWidget("Hello");
        assertNotNull(w);
    }

    @Test
    @DisplayName("full constructor with style, alignment, wrap")
    void fullConstructor() {
        ParagraphWidget w = new ParagraphWidget("Hello", Style.DEFAULT, Alignment.CENTER, false);
        assertNotNull(w);
    }

    @Test
    @DisplayName("render left aligned with wrap")
    void renderLeftAligned() {
        ParagraphWidget w = new ParagraphWidget("Hello World");
        Buffer b = new Buffer(20, 3);
        w.render(new Rect(0, 0, 20, 3), b);
        assertEquals('H', b.getCell(0, 0).ch());
        assertEquals('e', b.getCell(1, 0).ch());
    }

    @Test
    @DisplayName("render center aligned")
    void renderCenterAligned() {
        ParagraphWidget w = new ParagraphWidget("Hi", Style.DEFAULT, Alignment.CENTER, false);
        Buffer b = new Buffer(20, 3);
        w.render(new Rect(0, 0, 20, 3), b);
        assertEquals('H', b.getCell(9, 0).ch());
    }

    @Test
    @DisplayName("render right aligned")
    void renderRightAligned() {
        ParagraphWidget w = new ParagraphWidget("Hi", Style.DEFAULT, Alignment.RIGHT, false);
        Buffer b = new Buffer(20, 3);
        w.render(new Rect(0, 0, 20, 3), b);
        assertEquals('H', b.getCell(18, 0).ch());
    }

    @Test
    @DisplayName("render no-wrap truncates text")
    void renderNoWrapTruncates() {
        ParagraphWidget w = new ParagraphWidget("HelloWorld", Style.DEFAULT, Alignment.LEFT, false);
        Buffer b = new Buffer(5, 3);
        w.render(new Rect(0, 0, 5, 3), b);
        assertEquals('H', b.getCell(0, 0).ch());
        assertEquals('o', b.getCell(4, 0).ch());
    }

    @Test
    @DisplayName("render wraps long text to multiple lines")
    void renderWrapsText() {
        ParagraphWidget w = new ParagraphWidget("Hello World Test");
        Buffer b = new Buffer(6, 5);
        w.render(new Rect(0, 0, 6, 5), b);
        assertEquals('H', b.getCell(0, 0).ch());
        assertNotEquals(Cell.EMPTY.ch(), b.getCell(0, 1).ch());
    }

    @Test
    @DisplayName("render wraps on word boundaries")
    void renderWrapsOnWordBoundaries() {
        ParagraphWidget w = new ParagraphWidget("Hello World");
        Buffer b = new Buffer(6, 5);
        w.render(new Rect(0, 0, 6, 5), b);
        assertEquals('H', b.getCell(0, 0).ch());
        assertEquals('W', b.getCell(0, 1).ch());
    }

    @Test
    @DisplayName("render clips at area height with wrap")
    void renderClipsAtHeight() {
        ParagraphWidget w = new ParagraphWidget("A\nB\nC\nD\nE");
        Buffer b = new Buffer(10, 2);
        w.render(new Rect(0, 0, 10, 2), b);
        assertEquals('A', b.getCell(0, 0).ch());
        assertEquals('B', b.getCell(0, 1).ch());
        assertEquals(Cell.EMPTY, b.getCell(0, 2));
    }

    @Test
    @DisplayName("render with empty paragraph lines")
    void renderEmptyLines() {
        ParagraphWidget w = new ParagraphWidget("\n\n");
        Buffer b = new Buffer(10, 5);
        w.render(new Rect(0, 0, 10, 5), b);
        assertEquals(Cell.EMPTY.ch(), b.getCell(0, 0).ch());
    }

    @Test
    @DisplayName("render wrap with zero width does nothing")
    void renderZeroWidth() {
        ParagraphWidget w = new ParagraphWidget("Hello");
        Buffer b = new Buffer(10, 5);
        w.render(new Rect(0, 0, 0, 5), b);
        assertEquals(Cell.EMPTY, b.getCell(0, 0));
    }
}
