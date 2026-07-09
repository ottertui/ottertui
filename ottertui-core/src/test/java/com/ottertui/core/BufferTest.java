package com.ottertui.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class BufferTest {

    @Test
    @DisplayName("constructor creates buffer of correct dimensions")
    void constructorDimensions() {
        Buffer b = new Buffer(10, 5);
        assertEquals(10, b.width());
        assertEquals(5, b.height());
    }

    @Test
    @DisplayName("all cells initialized to EMPTY")
    void cellsInitializedToEmpty() {
        Buffer b = new Buffer(5, 3);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 5; x++) {
                assertEquals(Cell.EMPTY, b.getCell(x, y));
            }
        }
    }

    @Test
    @DisplayName("setCell and getCell within bounds")
    void setAndGetCell() {
        Buffer b = new Buffer(5, 3);
        Cell cell = new Cell('X', Style.DEFAULT);
        b.setCell(2, 1, cell);
        assertEquals(cell, b.getCell(2, 1));
    }

    @Test
    @DisplayName("getCell out of bounds returns EMPTY")
    void getCellOutOfBounds() {
        Buffer b = new Buffer(5, 3);
        assertEquals(Cell.EMPTY, b.getCell(-1, 0));
        assertEquals(Cell.EMPTY, b.getCell(0, -1));
        assertEquals(Cell.EMPTY, b.getCell(5, 0));
        assertEquals(Cell.EMPTY, b.getCell(0, 3));
    }

    @Test
    @DisplayName("getCell with x in bounds but y out of bounds")
    void getCellPartialOutOfBounds() {
        Buffer b = new Buffer(5, 3);
        assertEquals(Cell.EMPTY, b.getCell(2, -1));
        assertEquals(Cell.EMPTY, b.getCell(2, 3));
    }

    @Test
    @DisplayName("setCell out of bounds does nothing")
    void setCellOutOfBounds() {
        Buffer b = new Buffer(5, 3);
        Cell cell = new Cell('X', Style.DEFAULT);
        b.setCell(-1, 0, cell);
        b.setCell(5, 0, cell);
        assertEquals(Cell.EMPTY, b.getCell(0, 0));
    }

    @Test
    @DisplayName("setCell with y out of bounds does nothing")
    void setCellYOutOfBounds() {
        Buffer b = new Buffer(5, 3);
        b.setCell(2, -1, new Cell('X', Style.DEFAULT));
        b.setCell(2, 3, new Cell('X', Style.DEFAULT));
        assertEquals(Cell.EMPTY, b.getCell(2, 0));
        assertEquals(Cell.EMPTY, b.getCell(2, 2));
    }

    @Test
    @DisplayName("setString writes ASCII text with style")
    void setStringAscii() {
        Buffer b = new Buffer(10, 3);
        Style style = new Style(Color.RED, Color.BLUE, java.util.Set.of());
        b.setString(0, 0, "Hello", style);
        assertEquals('H', b.getCell(0, 0).ch());
        assertEquals('e', b.getCell(1, 0).ch());
        assertEquals('l', b.getCell(2, 0).ch());
        assertEquals('l', b.getCell(3, 0).ch());
        assertEquals('o', b.getCell(4, 0).ch());
        assertEquals(style, b.getCell(0, 0).style());
    }

    @Test
    @DisplayName("setString clips at buffer width boundary")
    void setStringClipsAtWidth() {
        Buffer b = new Buffer(5, 3);
        b.setString(0, 0, "HelloWorld", Style.DEFAULT);
        assertEquals('H', b.getCell(0, 0).ch());
        assertEquals('o', b.getCell(4, 0).ch());
        assertEquals(Cell.EMPTY, b.getCell(0, 1));
    }

    @Test
    @DisplayName("setString with supplementary Unicode character becomes ?")
    void setStringSupplementaryUnicode() {
        Buffer b = new Buffer(10, 3);
        // U+1F600 (😀) is a supplementary character (> U+FFFF), encoded as 2 chars
        // The buffer stores '?' for multi-char codepoints
        b.setString(0, 0, "😀", Style.DEFAULT);
        assertEquals('?', b.getCell(0, 0).ch());
    }

    @Test
    @DisplayName("setString with negative x clips left side")
    void setStringNegativeX() {
        Buffer b = new Buffer(10, 3);
        b.setString(-3, 0, "Hello", Style.DEFAULT);
        assertEquals('l', b.getCell(0, 0).ch());
        assertEquals('o', b.getCell(1, 0).ch());
    }

    @Test
    @DisplayName("setString handles CJK wide characters")
    void setStringCjkWide() {
        Buffer b = new Buffer(10, 3);
        b.setString(0, 0, "你好", Style.DEFAULT);
        assertEquals('你', b.getCell(0, 0).ch());
        assertEquals('好', b.getCell(2, 0).ch());
    }

    @Test
    @DisplayName("setString skips zero-width characters")
    void setStringSkipsZeroWidth() {
        Buffer b = new Buffer(10, 3);
        b.setString(0, 0, "a\u200Bb", Style.DEFAULT);
        assertEquals('a', b.getCell(0, 0).ch());
        assertEquals('b', b.getCell(1, 0).ch());
    }

    @Test
    @DisplayName("fill writes cell to specified area")
    void fillArea() {
        Buffer b = new Buffer(10, 5);
        Cell cell = new Cell('X', Style.DEFAULT);
        b.fill(new Rect(2, 1, 3, 2), cell);
        assertEquals('X', b.getCell(2, 1).ch());
        assertEquals('X', b.getCell(4, 1).ch());
        assertEquals('X', b.getCell(2, 2).ch());
        assertEquals(' ', b.getCell(1, 1).ch());
        assertEquals(' ', b.getCell(5, 1).ch());
        assertEquals(' ', b.getCell(2, 3).ch());
    }

    @Test
    @DisplayName("fill clips at buffer edges")
    void fillClipsAtEdges() {
        Buffer b = new Buffer(5, 3);
        Cell cell = new Cell('X', Style.DEFAULT);
        b.fill(new Rect(3, 1, 5, 5), cell);
        assertEquals('X', b.getCell(3, 1).ch());
        assertEquals('X', b.getCell(4, 1).ch());
        assertEquals('X', b.getCell(3, 2).ch());
    }

    @Test
    @DisplayName("fill with negative x clips to 0")
    void fillNegativeX() {
        Buffer b = new Buffer(10, 5);
        Cell cell = new Cell('X', Style.DEFAULT);
        b.fill(new Rect(-2, 0, 5, 2), cell);
        assertEquals('X', b.getCell(0, 0).ch());
        assertEquals('X', b.getCell(2, 0).ch());
        assertEquals('X', b.getCell(0, 1).ch());
    }

    @Test
    @DisplayName("fill with negative y clips to 0")
    void fillNegativeY() {
        Buffer b = new Buffer(10, 5);
        Cell cell = new Cell('X', Style.DEFAULT);
        b.fill(new Rect(0, -2, 3, 5), cell);
        assertEquals('X', b.getCell(0, 0).ch());
        assertEquals('X', b.getCell(2, 0).ch());
        assertEquals('X', b.getCell(0, 2).ch());
    }

    @Test
    @DisplayName("region returns BufferView with correct offset")
    void regionReturnsView() {
        Buffer b = new Buffer(10, 10);
        Cell cell = new Cell('A', Style.DEFAULT);
        b.setCell(3, 2, cell);

        Buffer view = b.region(new Rect(2, 1, 5, 5));
        assertEquals(5, view.width());
        assertEquals(5, view.height());
        assertEquals('A', view.getCell(1, 1).ch());
    }

    @Test
    @DisplayName("region setCell writes through to parent")
    void regionSetCellWritesParent() {
        Buffer b = new Buffer(10, 10);
        Buffer view = b.region(new Rect(2, 1, 5, 5));
        Cell cell = new Cell('Z', Style.DEFAULT);
        view.setCell(0, 0, cell);
        assertEquals('Z', b.getCell(2, 1).ch());
    }

    @Test
    @DisplayName("region view getCell out of bounds returns EMPTY")
    void regionViewGetCellOutOfBounds() {
        Buffer b = new Buffer(10, 10);
        Buffer view = b.region(new Rect(2, 1, 5, 5));
        assertEquals(Cell.EMPTY, view.getCell(-1, 0));
        assertEquals(Cell.EMPTY, view.getCell(5, 0));
        assertEquals(Cell.EMPTY, view.getCell(0, -1));
        assertEquals(Cell.EMPTY, view.getCell(0, 5));
    }

    @Test
    @DisplayName("zero size buffer")
    void zeroSizeBuffer() {
        Buffer b = new Buffer(0, 0);
        assertEquals(0, b.width());
        assertEquals(0, b.height());
        assertEquals(Cell.EMPTY, b.getCell(0, 0));
    }
}
