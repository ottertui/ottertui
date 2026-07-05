package com.ottertui.widgets;

import com.ottertui.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListWidgetTest {

    @Test
    @DisplayName("constructor with items")
    void constructorWithItems() {
        ListWidget w = new ListWidget(List.of("A", "B", "C"));
        assertNotNull(w);
    }

    @Test
    @DisplayName("default selected index is 0")
    void defaultSelectedIndex() {
        ListWidget w = new ListWidget(List.of("A", "B", "C"));
        assertEquals(0, w.selectedIndex());
    }

    @Test
    @DisplayName("select sets valid index")
    void selectValidIndex() {
        ListWidget w = new ListWidget(List.of("A", "B", "C"));
        w.select(2);
        assertEquals(2, w.selectedIndex());
    }

    @Test
    @DisplayName("select ignores invalid index")
    void selectInvalidIndex() {
        ListWidget w = new ListWidget(List.of("A", "B", "C"));
        w.select(-1);
        assertEquals(0, w.selectedIndex());
        w.select(5);
        assertEquals(0, w.selectedIndex());
    }

    @Test
    @DisplayName("moveUp decreases index")
    void moveUp() {
        ListWidget w = new ListWidget(List.of("A", "B", "C"));
        w.select(2);
        w.moveUp();
        assertEquals(1, w.selectedIndex());
    }

    @Test
    @DisplayName("moveUp at top does nothing")
    void moveUpAtTop() {
        ListWidget w = new ListWidget(List.of("A", "B", "C"));
        w.moveUp();
        assertEquals(0, w.selectedIndex());
    }

    @Test
    @DisplayName("moveDown increases index")
    void moveDown() {
        ListWidget w = new ListWidget(List.of("A", "B", "C"));
        w.moveDown();
        assertEquals(1, w.selectedIndex());
    }

    @Test
    @DisplayName("moveDown at bottom does nothing")
    void moveDownAtBottom() {
        ListWidget w = new ListWidget(List.of("A", "B", "C"));
        w.select(2);
        w.moveDown();
        assertEquals(2, w.selectedIndex());
    }

    @Test
    @DisplayName("selectedItem returns current item")
    void selectedItem() {
        ListWidget w = new ListWidget(List.of("Apple", "Banana", "Cherry"));
        w.select(1);
        assertEquals("Banana", w.selectedItem());
    }

    @Test
    @DisplayName("render draws items")
    void renderDrawsItems() {
        ListWidget w = new ListWidget(List.of("A", "B", "C"));
        Buffer b = new Buffer(10, 5);
        w.render(new Rect(0, 0, 10, 5), b);
        assertEquals('A', b.getCell(0, 0).ch());
        assertEquals('B', b.getCell(0, 1).ch());
        assertEquals('C', b.getCell(0, 2).ch());
    }

    @Test
    @DisplayName("render selected item uses selected style")
    void renderSelectedStyle() {
        Style sel = new Style(Color.BLACK, Color.WHITE, java.util.Set.of());
        Style norm = Style.DEFAULT;
        ListWidget w = new ListWidget(List.of("A", "B"), sel, norm);
        w.select(1);
        Buffer b = new Buffer(10, 5);
        w.render(new Rect(0, 0, 10, 5), b);
        assertEquals(sel, b.getCell(0, 1).style());
    }

    @Test
    @DisplayName("render truncates long items")
    void renderTruncatesLongItems() {
        ListWidget w = new ListWidget(List.of("VeryLongItem"));
        Buffer b = new Buffer(5, 3);
        w.render(new Rect(0, 0, 5, 3), b);
        assertEquals('V', b.getCell(0, 0).ch());
        assertEquals('e', b.getCell(1, 0).ch());
    }

    @Test
    @DisplayName("render pads remaining width")
    void renderPadsWidth() {
        ListWidget w = new ListWidget(List.of("AB"));
        Buffer b = new Buffer(5, 3);
        w.render(new Rect(0, 0, 5, 3), b);
        assertEquals(' ', b.getCell(2, 0).ch());
        assertEquals(' ', b.getCell(4, 0).ch());
    }

    @Test
    @DisplayName("render clips at area height")
    void renderClipsAtHeight() {
        ListWidget w = new ListWidget(List.of("A", "B", "C", "D", "E"));
        Buffer b = new Buffer(10, 2);
        w.render(new Rect(0, 0, 10, 2), b);
        assertEquals('A', b.getCell(0, 0).ch());
        assertEquals('B', b.getCell(0, 1).ch());
        assertEquals(Cell.EMPTY, b.getCell(0, 2));
    }
}
