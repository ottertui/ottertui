package com.ottertui.tui;

import com.ottertui.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class KeyBindingsTest {

    @Test
    @DisplayName("bind simple key triggers action")
    void bindSimpleKey() {
        KeyBindings kb = new KeyBindings();
        AtomicBoolean fired = new AtomicBoolean(false);
        kb.bind(KeyCode.ENTER, () -> fired.set(true));

        boolean handled = kb.handle(InputEvent.key(KeyCode.ENTER));
        assertTrue(handled);
        assertTrue(fired.get());
    }

    @Test
    @DisplayName("handle returns false for non-matching key")
    void handleNonMatching() {
        KeyBindings kb = new KeyBindings();
        kb.bind(KeyCode.ENTER, () -> {});

        boolean handled = kb.handle(InputEvent.key(KeyCode.TAB));
        assertFalse(handled);
    }

    @Test
    @DisplayName("handle returns false for non-KeyEvent")
    void handleNonKeyEvent() {
        KeyBindings kb = new KeyBindings();
        kb.bind(KeyCode.ENTER, () -> {});

        boolean handled = kb.handle(new InputEvent.Unknown());
        assertFalse(handled);
    }

    @Test
    @DisplayName("bind with modifiers matches exact modifiers")
    void bindWithModifiers() {
        KeyBindings kb = new KeyBindings();
        AtomicBoolean fired = new AtomicBoolean(false);
        kb.bind(KeyCode.CHAR, Set.of(Modifier.BOLD), 'x', () -> fired.set(true));

        boolean handled = kb.handle(InputEvent.charKey('x', Set.of(Modifier.BOLD)));
        assertTrue(handled);
        assertTrue(fired.get());
    }

    @Test
    @DisplayName("modifiers must match exactly")
    void modifiersMustMatch() {
        KeyBindings kb = new KeyBindings();
        AtomicBoolean fired = new AtomicBoolean(false);
        kb.bind(KeyCode.CHAR, Set.of(Modifier.BOLD), 'x', () -> fired.set(true));

        boolean handled = kb.handle(InputEvent.charKey('x'));
        assertFalse(handled);
        assertFalse(fired.get());
    }

    @Test
    @DisplayName("multiple bindings checked in order")
    void multipleBindings() {
        KeyBindings kb = new KeyBindings();
        AtomicBoolean first = new AtomicBoolean(false);
        AtomicBoolean second = new AtomicBoolean(false);
        kb.bind(KeyCode.TAB, () -> first.set(true));
        kb.bind(KeyCode.TAB, () -> second.set(true));

        kb.handle(InputEvent.key(KeyCode.TAB));
        assertTrue(first.get());
        assertFalse(second.get());
    }

    @Test
    @DisplayName("ch=0 matches any character for CHAR key")
    void chZeroMatchesAnyChar() {
        KeyBindings kb = new KeyBindings();
        AtomicBoolean fired = new AtomicBoolean(false);
        kb.bind(KeyCode.CHAR, Set.of(), 0, () -> fired.set(true));

        boolean handled = kb.handle(InputEvent.charKey('z'));
        assertTrue(handled);
        assertTrue(fired.get());
    }

    @Test
    @DisplayName("specific char match with CHAR key")
    void specificCharMatch() {
        KeyBindings kb = new KeyBindings();
        AtomicBoolean fired = new AtomicBoolean(false);
        kb.bind(KeyCode.CHAR, Set.of(), 'c', () -> fired.set(true));

        assertTrue(kb.handle(InputEvent.charKey('c')));
        assertFalse(kb.handle(InputEvent.charKey('x')));
    }
}
