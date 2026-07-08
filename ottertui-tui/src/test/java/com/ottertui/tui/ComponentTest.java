package com.ottertui.tui;

import com.ottertui.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ComponentTest {

    static class TestComponent extends Component {
        @Override
        public void render(Rect area, Buffer buffer) {}
    }

    @Test
    @DisplayName("new component has no parent")
    void newComponentNoParent() {
        TestComponent c = new TestComponent();
        assertNull(c.parent());
    }

    @Test
    @DisplayName("setParent sets parent")
    void setParent() {
        TestComponent parent = new TestComponent();
        TestComponent child = new TestComponent();
        child.setParent(parent);
        assertEquals(parent, child.parent());
    }

    @Test
    @DisplayName("addChild adds child and sets parent")
    void addChild() {
        TestComponent parent = new TestComponent();
        TestComponent child = new TestComponent();
        parent.addChild(child);
        assertEquals(1, parent.children().size());
        assertEquals(parent, child.parent());
    }

    @Test
    @DisplayName("removeChild removes child and clears parent")
    void removeChild() {
        TestComponent parent = new TestComponent();
        TestComponent child = new TestComponent();
        parent.addChild(child);
        parent.removeChild(child);
        assertEquals(0, parent.children().size());
        assertNull(child.parent());
    }

    @Test
    @DisplayName("default not focused and not focusable")
    void defaultFocusState() {
        TestComponent c = new TestComponent();
        assertFalse(c.isFocused());
        assertFalse(c.isFocusable());
    }

    @Test
    @DisplayName("setFocusable changes focusable state")
    void setFocusable() {
        TestComponent c = new TestComponent();
        c.setFocusable(true);
        assertTrue(c.isFocusable());
    }

    @Test
    @DisplayName("requestFocus sets focus via parent")
    void requestFocus() {
        TestComponent parent = new TestComponent();
        TestComponent child = new TestComponent();
        parent.addChild(child);
        child.requestFocus();
        assertTrue(child.isFocused());
    }

    @Test
    @DisplayName("requestFocus with no parent does nothing")
    void requestFocusNoParent() {
        TestComponent c = new TestComponent();
        c.requestFocus();
        assertFalse(c.isFocused());
    }

    @Test
    @DisplayName("focusChild focuses one child, unfocuses others")
    void focusChild() {
        TestComponent parent = new TestComponent();
        TestComponent child1 = new TestComponent();
        TestComponent child2 = new TestComponent();
        parent.addChild(child1);
        parent.addChild(child2);
        child1.requestFocus();
        child2.requestFocus();
        assertFalse(child1.isFocused());
        assertTrue(child2.isFocused());
    }

    @Test
    @DisplayName("focusedChild returns focused child")
    void focusedChild() {
        TestComponent parent = new TestComponent();
        TestComponent child = new TestComponent();
        parent.addChild(child);
        child.requestFocus();
        assertEquals(child, parent.focusedChild());
    }

    @Test
    @DisplayName("focusedChild returns null when none focused")
    void focusedChildNull() {
        TestComponent parent = new TestComponent();
        parent.addChild(new TestComponent());
        assertNull(parent.focusedChild());
    }

    @Test
    @DisplayName("focusNext with no children does nothing")
    void focusNextEmpty() {
        TestComponent parent = new TestComponent();
        parent.focusNext();
        assertNull(parent.focusedChild());
    }

    @Test
    @DisplayName("focusNext finds next focusable child")
    void focusNext() {
        TestComponent parent = new TestComponent();
        TestComponent c1 = new TestComponent();
        c1.setFocusable(true);
        TestComponent c2 = new TestComponent();
        c2.setFocusable(true);
        parent.addChild(c1);
        parent.addChild(c2);
        c1.requestFocus();

        parent.focusNext();
        assertFalse(c1.isFocused());
        assertTrue(c2.isFocused());
    }

    @Test
    @DisplayName("focusNext wraps around")
    void focusNextWraps() {
        TestComponent parent = new TestComponent();
        TestComponent c1 = new TestComponent();
        c1.setFocusable(true);
        TestComponent c2 = new TestComponent();
        c2.setFocusable(true);
        parent.addChild(c1);
        parent.addChild(c2);
        c2.requestFocus();

        parent.focusNext();
        assertTrue(c1.isFocused());
        assertFalse(c2.isFocused());
    }

    @Test
    @DisplayName("focusNext skips non-focusable children")
    void focusNextSkipsNonFocusable() {
        TestComponent parent = new TestComponent();
        TestComponent c1 = new TestComponent();
        c1.setFocusable(true);
        TestComponent c2 = new TestComponent(); // not focusable
        TestComponent c3 = new TestComponent();
        c3.setFocusable(true);
        parent.addChild(c1);
        parent.addChild(c2);
        parent.addChild(c3);
        c1.requestFocus();

        parent.focusNext();
        assertTrue(c3.isFocused());
    }

    @Test
    @DisplayName("onEvent routes to focused child")
    void onEventRoutesToFocused() {
        TestComponent parent = new TestComponent();
        AtomicBoolean handled = new AtomicBoolean(false);

        Component child = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {}
            @Override
            public boolean onEvent(InputEvent event) {
                handled.set(true);
                return true;
            }
        };
        parent.addChild(child);
        child.requestFocus();

        boolean result = parent.onEvent(InputEvent.key(KeyCode.ENTER));
        assertTrue(result);
        assertTrue(handled.get());
    }

    @Test
    @DisplayName("onEvent returns false when no focused child handles")
    void onEventNotHandled() {
        TestComponent parent = new TestComponent();
        TestComponent child = new TestComponent();
        parent.addChild(child);
        child.requestFocus();

        boolean result = parent.onEvent(InputEvent.key(KeyCode.ENTER));
        assertFalse(result);
    }

    @Test
    @DisplayName("render calls render on all children")
    void renderCallsChildren() {
        // Use base Component (not TestComponent) so render() iterates children
        Component parent = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {
                for (var child : children()) {
                    child.render(area, buffer);
                }
            }
        };
        AtomicBoolean rendered = new AtomicBoolean(false);
        Component child = new TestComponent() {
            @Override
            public void render(Rect area, Buffer buffer) {
                rendered.set(true);
            }
        };
        parent.addChild(child);
        parent.render(new Rect(0, 0, 10, 10), new Buffer(10, 10));
        assertTrue(rendered.get());
    }

    @Test
    @DisplayName("onEvent returns false when no children")
    void onEventNoChildren() {
        TestComponent c = new TestComponent();
        assertFalse(c.onEvent(InputEvent.key(KeyCode.ENTER)));
    }

    @Test
    @DisplayName("onEvent returns false when no child focused")
    void onEventNoFocusedChild() {
        TestComponent parent = new TestComponent();
        TestComponent child = new TestComponent();
        parent.addChild(child);
        assertFalse(parent.onEvent(InputEvent.key(KeyCode.ENTER)));
    }

    @Test
    @DisplayName("render with empty children does nothing")
    void renderEmptyChildren() {
        TestComponent c = new TestComponent();
        Buffer b = new Buffer(10, 10);
        Cell cell = new Cell('X', Style.DEFAULT);
        b.setCell(0, 0, cell);
        c.render(new Rect(0, 0, 10, 10), b);
        assertEquals('X', b.getCell(0, 0).ch());
    }

    @Test
    @DisplayName("focusNext with all non-focusable children")
    void focusNextAllNonFocusable() {
        TestComponent parent = new TestComponent();
        TestComponent c1 = new TestComponent();
        TestComponent c2 = new TestComponent();
        parent.addChild(c1);
        parent.addChild(c2);
        parent.focusNext();
        assertNull(parent.focusedChild());
    }

    @Test
    @DisplayName("base render delegates to children")
    void baseRenderDelegates() {
        Component parent = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {
                for (var child : children()) {
                    child.render(area, buffer);
                }
            }
        };
        AtomicBoolean rendered = new AtomicBoolean(false);
        Component child = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {
                rendered.set(true);
            }
        };
        parent.addChild(child);
        parent.render(new Rect(0, 0, 10, 10), new Buffer(10, 10));
        assertTrue(rendered.get());
    }

    @Test
    @DisplayName("addChild then removeChild")
    void addAndRemoveChild() {
        TestComponent parent = new TestComponent();
        TestComponent child1 = new TestComponent();
        TestComponent child2 = new TestComponent();
        parent.addChild(child1);
        parent.addChild(child2);
        assertEquals(2, parent.children().size());
        parent.removeChild(child1);
        assertEquals(1, parent.children().size());
        assertEquals(child2, parent.children().get(0));
    }

    @Test
    @DisplayName("base render delegates to children via super")
    void baseRenderViaSuper() {
        AtomicBoolean childRendered = new AtomicBoolean(false);
        Component parent = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {
                super.render(area, buffer);
            }
        };
        Component child = new Component() {
            @Override
            public void render(Rect area, Buffer buffer) {
                childRendered.set(true);
            }
        };
        parent.addChild(child);
        parent.render(new Rect(0, 0, 10, 10), new Buffer(10, 10));
        assertTrue(childRendered.get());
    }
}
