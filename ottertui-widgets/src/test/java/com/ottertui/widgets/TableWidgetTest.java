package com.ottertui.widgets;

import com.ottertui.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TableWidgetTest {

    record Item(String name, int value) {}

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        var cols = List.of(new TableWidget.Column<>("Name", Item::name, 10));
        var rows = List.of(new Item("foo", 1));
        TableWidget<Item> w = new TableWidget<>(cols, rows);
        assertNotNull(w);
    }

    @Test
    @DisplayName("full constructor with styles")
    void fullConstructor() {
        var cols = List.of(new TableWidget.Column<>("Name", Item::name, 10));
        var rows = List.<Item>of();
        TableWidget<Item> w = new TableWidget<>(cols, rows,
            new Style(Color.WHITE, Color.RESET, Set.of(Modifier.BOLD)),
            new Style(Color.BLACK, Color.WHITE, Set.of()));
        assertNotNull(w);
    }

    @Test
    @DisplayName("render draws header")
    void renderDrawsHeader() {
        var cols = List.of(new TableWidget.Column<>("Name", Item::name, 10));
        var rows = List.<Item>of();
        TableWidget<Item> w = new TableWidget<>(cols, rows);
        Buffer b = new Buffer(20, 10);
        TableState state = new TableState();
        w.render(state, new Rect(0, 0, 20, 10), b);
        assertEquals('N', b.getCell(0, 0).ch());
        assertEquals('a', b.getCell(1, 0).ch());
        assertEquals('m', b.getCell(2, 0).ch());
        assertEquals('e', b.getCell(3, 0).ch());
    }

    @Test
    @DisplayName("render draws separator after header")
    void renderDrawsSeparator() {
        var cols = List.of(new TableWidget.Column<>("Name", Item::name, 10));
        var rows = List.<Item>of();
        TableWidget<Item> w = new TableWidget<>(cols, rows);
        Buffer b = new Buffer(20, 10);
        TableState state = new TableState();
        w.render(state, new Rect(0, 0, 20, 10), b);
        assertEquals('─', b.getCell(0, 1).ch());
    }

    @Test
    @DisplayName("render draws data rows")
    void renderDrawsDataRows() {
        TableWidget.Column<Item> nameCol = new TableWidget.Column<>("Name", Item::name, 6);
        TableWidget.Column<Item> valueCol = new TableWidget.Column<>("Value", i -> String.valueOf(i.value()), 6);
        var cols = List.of(nameCol, valueCol);
        var rows = List.of(new Item("foo", 42), new Item("bar", 99));
        TableWidget<Item> w = new TableWidget<>(cols, rows);
        Buffer b = new Buffer(20, 10);
        TableState state = new TableState();
        state.select(0);
        w.render(state, new Rect(0, 0, 20, 10), b);
        assertEquals('f', b.getCell(0, 2).ch());
        assertEquals('4', b.getCell(7, 2).ch());
        assertEquals('b', b.getCell(0, 3).ch());
    }

    @Test
    @DisplayName("render selected row uses selected style")
    void renderSelectedRowStyle() {
        Style sel = new Style(Color.BLACK, Color.WHITE, Set.of());
        var cols = List.of(new TableWidget.Column<>("Name", Item::name, 10));
        var rows = List.of(new Item("foo", 1));
        TableWidget<Item> w = new TableWidget<>(cols, rows,
            new Style(Color.WHITE, Color.RESET, Set.of(Modifier.BOLD)), sel);
        Buffer b = new Buffer(20, 10);
        TableState state = new TableState();
        state.select(0);
        w.render(state, new Rect(0, 0, 20, 10), b);
        assertEquals(sel, b.getCell(0, 2).style());
    }

    @Test
    @DisplayName("render header truncated if too long")
    void renderHeaderTruncated() {
        var cols = List.of(new TableWidget.Column<>("VeryLongHeader", Item::name, 5));
        var rows = List.<Item>of();
        TableWidget<Item> w = new TableWidget<>(cols, rows);
        Buffer b = new Buffer(20, 10);
        TableState state = new TableState();
        w.render(state, new Rect(0, 0, 20, 10), b);
        assertEquals('V', b.getCell(0, 0).ch());
        assertEquals('e', b.getCell(1, 0).ch());
    }

    @Test
    @DisplayName("render with empty rows renders only header and separator")
    void renderEmptyRows() {
        var cols = List.of(new TableWidget.Column<>("Col", Item::name, 10));
        var rows = List.<Item>of();
        TableWidget<Item> w = new TableWidget<>(cols, rows);
        Buffer b = new Buffer(20, 10);
        TableState state = new TableState();
        w.render(state, new Rect(0, 0, 20, 10), b);
        assertEquals('─', b.getCell(0, 1).ch());
        assertEquals(Cell.EMPTY, b.getCell(0, 2));
    }
}
