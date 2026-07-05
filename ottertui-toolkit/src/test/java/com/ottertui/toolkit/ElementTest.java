package com.ottertui.toolkit;

import com.ottertui.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ElementTest {

    @Test
    @DisplayName("Container element")
    void containerElement() {
        Element.Container c = new Element.Container(Layout.Direction.VERTICAL,
            List.of(), 1, Style.DEFAULT);
        assertEquals(Layout.Direction.VERTICAL, c.direction());
        assertTrue(c.children().isEmpty());
        assertEquals(1, c.gap());
    }

    @Test
    @DisplayName("WidgetElement")
    void widgetElement() {
        Element.WidgetElement w = new Element.WidgetElement(
            (area, buffer) -> {}, Style.DEFAULT, "my-id");
        assertEquals("my-id", w.id());
    }

    @Test
    @DisplayName("TextElement")
    void textElement() {
        Element.TextElement t = new Element.TextElement("Hello",
            new Style(Color.RED, Color.RESET, java.util.Set.of()));
        assertEquals("Hello", t.text());
        assertEquals(Color.RED, t.style().foreground());
    }
}
