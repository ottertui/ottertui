package com.ottertui.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextTest {

    @Test
    @DisplayName("Span renders text at position")
    void spanRendersText() {
        Buffer b = new Buffer(20, 5);
        Text.Span span = new Text.Span("Hello", Style.DEFAULT);
        span.render(new Rect(0, 0, 20, 5), b);
        assertEquals('H', b.getCell(0, 0).ch());
        assertEquals('o', b.getCell(4, 0).ch());
    }

    @Test
    @DisplayName("Span with styled text")
    void spanWithStyle() {
        Buffer b = new Buffer(20, 5);
        Style style = new Style(Color.RED, Color.BLUE, java.util.Set.of());
        Text.Span span = new Text.Span("Hi", style);
        span.render(new Rect(0, 0, 20, 5), b);
        assertEquals(style, b.getCell(0, 0).style());
    }

    @Test
    @DisplayName("Line renders multiple spans sequentially")
    void lineRendersSpans() {
        Buffer b = new Buffer(20, 5);
        Text.Line line = new Text.Line(List.of(
            new Text.Span("AB", Style.DEFAULT),
            new Text.Span("CD", Style.DEFAULT)
        ));
        line.render(new Rect(5, 2, 20, 5), b);
        assertEquals('A', b.getCell(5, 2).ch());
        assertEquals('B', b.getCell(6, 2).ch());
        assertEquals('C', b.getCell(7, 2).ch());
        assertEquals('D', b.getCell(8, 2).ch());
    }

    @Test
    @DisplayName("Paragraph left aligned")
    void paragraphLeftAligned() {
        Buffer b = new Buffer(20, 10);
        Text.Paragraph p = new Text.Paragraph(List.of(
            new Text.Line(List.of(new Text.Span("Hi", Style.DEFAULT))),
            new Text.Line(List.of(new Text.Span("World", Style.DEFAULT)))
        ), Alignment.LEFT);
        p.render(new Rect(2, 1, 20, 10), b);
        assertEquals('H', b.getCell(2, 1).ch());
        assertEquals('W', b.getCell(2, 2).ch());
    }

    @Test
    @DisplayName("Paragraph center aligned")
    void paragraphCenterAligned() {
        Buffer b = new Buffer(20, 10);
        Text.Paragraph p = new Text.Paragraph(List.of(
            new Text.Line(List.of(new Text.Span("Hi", Style.DEFAULT)))
        ), Alignment.CENTER);
        p.render(new Rect(0, 0, 20, 10), b);
        assertEquals('H', b.getCell(9, 0).ch());
    }

    @Test
    @DisplayName("Paragraph right aligned")
    void paragraphRightAligned() {
        Buffer b = new Buffer(20, 10);
        Text.Paragraph p = new Text.Paragraph(List.of(
            new Text.Line(List.of(new Text.Span("Hi", Style.DEFAULT)))
        ), Alignment.RIGHT);
        p.render(new Rect(0, 0, 20, 10), b);
        assertEquals('H', b.getCell(18, 0).ch());
    }

    @Test
    @DisplayName("Paragraph clips at area height")
    void paragraphClipsAtHeight() {
        Buffer b = new Buffer(20, 3);
        Text.Paragraph p = new Text.Paragraph(List.of(
            new Text.Line(List.of(new Text.Span("A", Style.DEFAULT))),
            new Text.Line(List.of(new Text.Span("B", Style.DEFAULT))),
            new Text.Line(List.of(new Text.Span("C", Style.DEFAULT)))
        ), Alignment.LEFT);
        p.render(new Rect(0, 0, 20, 2), b);
        assertEquals('A', b.getCell(0, 0).ch());
        assertEquals('B', b.getCell(0, 1).ch());
        assertEquals(Cell.EMPTY, b.getCell(0, 2));
    }
}
