package com.ottertui.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InputEventTest {

    @Test
    @DisplayName("key factory creates KeyEvent")
    void keyFactory() {
        InputEvent e = InputEvent.key(KeyCode.ENTER);
        assertInstanceOf(InputEvent.KeyEvent.class, e);
        InputEvent.KeyEvent ke = (InputEvent.KeyEvent) e;
        assertEquals(KeyCode.ENTER, ke.code());
        assertTrue(ke.mods().isEmpty());
        assertEquals(0, ke.ch());
    }

    @Test
    @DisplayName("key factory with modifiers")
    void keyWithModifiers() {
        InputEvent e = InputEvent.key(KeyCode.TAB, Set.of(Modifier.BOLD));
        InputEvent.KeyEvent ke = (InputEvent.KeyEvent) e;
        assertEquals(KeyCode.TAB, ke.code());
        assertTrue(ke.mods().contains(Modifier.BOLD));
    }

    @Test
    @DisplayName("charKey factory creates CHAR KeyEvent")
    void charKeyFactory() {
        InputEvent e = InputEvent.charKey('a');
        InputEvent.KeyEvent ke = (InputEvent.KeyEvent) e;
        assertEquals(KeyCode.CHAR, ke.code());
        assertEquals('a', ke.ch());
    }

    @Test
    @DisplayName("charKey factory with modifiers")
    void charKeyWithModifiers() {
        InputEvent e = InputEvent.charKey('x', Set.of(Modifier.ITALIC));
        InputEvent.KeyEvent ke = (InputEvent.KeyEvent) e;
        assertEquals(KeyCode.CHAR, ke.code());
        assertEquals('x', ke.ch());
        assertTrue(ke.mods().contains(Modifier.ITALIC));
    }

    @Test
    @DisplayName("Resize event")
    void resizeEvent() {
        InputEvent.Resize r = new InputEvent.Resize(24, 80);
        assertEquals(24, r.rows());
        assertEquals(80, r.cols());
    }

    @Test
    @DisplayName("Unknown event")
    void unknownEvent() {
        InputEvent.Unknown u = new InputEvent.Unknown();
        assertNotNull(u);
    }

    @Test
    @DisplayName("MouseButton enum values")
    void mouseButtonValues() {
        assertNotNull(InputEvent.MouseButton.LEFT);
        assertNotNull(InputEvent.MouseButton.MIDDLE);
        assertNotNull(InputEvent.MouseButton.RIGHT);
        assertNotNull(InputEvent.MouseButton.NONE);
    }

    @Test
    @DisplayName("MouseEvent fields")
    void mouseEventFields() {
        InputEvent.MouseEvent me = new InputEvent.MouseEvent(
            InputEvent.MouseButton.LEFT, 5, 10, true);
        assertEquals(InputEvent.MouseButton.LEFT, me.button());
        assertEquals(5, me.row());
        assertEquals(10, me.col());
        assertTrue(me.pressed());
    }
}
