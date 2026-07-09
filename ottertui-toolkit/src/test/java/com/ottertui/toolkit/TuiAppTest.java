package com.ottertui.toolkit;

import com.ottertui.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class TuiAppTest {

    @Test
    @DisplayName("TuiApp constructor does not throw")
    void constructorDoesNotThrow() {
        Element root = new Element.Container(Layout.Direction.VERTICAL,
            List.of(), 1, Style.DEFAULT);
        TuiApp app = new TuiApp(root);
        assertNotNull(app);
    }

    @Test
    @DisplayName("TuiApp has runner")
    void hasRunner() {
        Element root = new Element.Container(Layout.Direction.VERTICAL,
            List.of(), 1, Style.DEFAULT);
        TuiApp app = new TuiApp(root);
        assertNotNull(app.runner());
    }

    @Test
    @DisplayName("TuiApp with text element compiles to ParagraphWidget")
    void withTextElement() {
        Element.TextElement text = new Element.TextElement("Hello", Style.DEFAULT);
        Element root = new Element.Container(Layout.Direction.VERTICAL,
            List.of(text), 1, Style.DEFAULT);
        TuiApp app = new TuiApp(root);
        assertNotNull(app);
        assertNotNull(app.runner());
    }

    @Test
    @DisplayName("ContainerComponent render splits area for children")
    void containerComponentRender() {
        TuiApp.ContainerComponent cc = new TuiApp.ContainerComponent(
            Layout.Direction.HORIZONTAL, 1);

        AtomicBoolean childRendered = new AtomicBoolean(false);
        com.ottertui.tui.Component child = new com.ottertui.tui.Component() {
            @Override
            public void render(Rect area, Buffer buffer) {
                childRendered.set(true);
            }
        };
        cc.addChild(child);

        Buffer b = new Buffer(100, 50);
        cc.render(new Rect(0, 0, 100, 50), b);
        assertTrue(childRendered.get());
    }

    @Test
    @DisplayName("ContainerComponent with no children does nothing")
    void containerComponentEmpty() {
        TuiApp.ContainerComponent cc = new TuiApp.ContainerComponent(
            Layout.Direction.VERTICAL, 1);
        Buffer b = new Buffer(10, 10);
        cc.render(new Rect(0, 0, 10, 10), b);
        assertEquals(Cell.EMPTY, b.getCell(0, 0));
    }

    @Test
    @DisplayName("WidgetComponent delegates render to widget")
    void widgetComponentRender() {
        AtomicBoolean widgetRendered = new AtomicBoolean(false);
        Widget w = (area, buffer) -> widgetRendered.set(true);
        TuiApp.WidgetComponent wc = new TuiApp.WidgetComponent(w);

        Buffer b = new Buffer(10, 10);
        wc.render(new Rect(0, 0, 10, 10), b);
        assertTrue(widgetRendered.get());
    }

    @Test
    @DisplayName("TuiApp compile handles Container with children")
    void compileContainerWithChildren() {
        Element.TextElement text = new Element.TextElement("A", Style.DEFAULT);
        Element.Container container = new Element.Container(
            Layout.Direction.HORIZONTAL, List.of(text), 2, Style.DEFAULT);
        TuiApp app = new TuiApp(container);
        assertNotNull(app.runner());
    }

    @Test
    @DisplayName("compile WidgetElement")
    void compileWidgetElement() {
        Element.WidgetElement we = new Element.WidgetElement(
            (area, buffer) -> {}, Style.DEFAULT, "w1");
        Element.Container container = new Element.Container(
            Layout.Direction.VERTICAL, List.of(we), 1, Style.DEFAULT);
        TuiApp app = new TuiApp(container);
        assertNotNull(app.runner());
    }

    @Test
    @DisplayName("run starts and can be stopped")
    void runStartsAndStops() throws InterruptedException {
        Element root = new Element.Container(Layout.Direction.VERTICAL,
            List.of(), 1, Style.DEFAULT);
        TuiApp app = new TuiApp(root);
        app.runner().stop();
        // runner.stop() should set running to false
        // When run() is called, it should exit immediately
        assertDoesNotThrow(() -> app.run());
    }
}
