package com.ottertui.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LayoutTest {

    @Test
    @DisplayName("horizontal layout splits width evenly with proportional")
    void horizontalProportional() {
        Layout layout = Layout.horizontal(List.of(
            Constraint.proportional(1),
            Constraint.proportional(1)
        ));
        List<Rect> rects = layout.split(new Rect(0, 0, 100, 50));
        assertEquals(2, rects.size());
        assertEquals(0, rects.get(0).x());
        assertEquals(49, rects.get(0).width());
        assertEquals(50, rects.get(1).x());
        assertEquals(49, rects.get(1).width());
    }

    @Test
    @DisplayName("vertical layout splits height evenly with proportional")
    void verticalProportional() {
        Layout layout = Layout.vertical(List.of(
            Constraint.proportional(1),
            Constraint.proportional(1)
        ));
        List<Rect> rects = layout.split(new Rect(0, 0, 100, 50));
        assertEquals(2, rects.size());
        assertEquals(0, rects.get(0).y());
        assertEquals(24, rects.get(0).height());
        assertEquals(25, rects.get(1).y());
        assertEquals(24, rects.get(1).height());
    }

    @Test
    @DisplayName("fixed constraint allocates exact size")
    void fixedConstraint() {
        Layout layout = Layout.horizontal(List.of(
            Constraint.fixed(20),
            Constraint.proportional(1)
        ));
        List<Rect> rects = layout.split(new Rect(0, 0, 100, 50));
        assertEquals(20, rects.get(0).width());
        assertEquals(79, rects.get(1).width());
    }

    @Test
    @DisplayName("fixed constraint clamped to remaining space")
    void fixedClamped() {
        Layout layout = Layout.horizontal(List.of(
            Constraint.fixed(200),
            Constraint.proportional(1)
        ));
        List<Rect> rects = layout.split(new Rect(0, 0, 100, 50));
        assertEquals(99, rects.get(0).width());
        assertEquals(0, rects.get(1).width());
    }

    @Test
    @DisplayName("percentage constraint allocates percentage of total")
    void percentageConstraint() {
        Layout layout = Layout.horizontal(List.of(
            Constraint.percentage(30),
            Constraint.percentage(70)
        ));
        List<Rect> rects = layout.split(new Rect(0, 0, 100, 50));
        assertEquals(29, rects.get(0).width());
        assertEquals(69, rects.get(1).width());
    }

    @Test
    @DisplayName("min constraint allocates minimum size")
    void minConstraint() {
        Layout layout = Layout.horizontal(List.of(
            Constraint.min(10),
            Constraint.proportional(1)
        ));
        List<Rect> rects = layout.split(new Rect(0, 0, 100, 50));
        assertEquals(10, rects.get(0).width());
        assertEquals(89, rects.get(1).width());
    }

    @Test
    @DisplayName("gap subtracts from total space")
    void gapSubtractsFromTotal() {
        Layout layout = Layout.horizontal(List.of(
            Constraint.proportional(1),
            Constraint.proportional(1)
        )).gap(3);
        List<Rect> rects = layout.split(new Rect(0, 0, 100, 50));
        assertEquals(48, rects.get(0).width());
        assertEquals(51, rects.get(1).x());
        assertEquals(48, rects.get(1).width());
    }

    @Test
    @DisplayName("vertical layout with gap")
    void verticalGap() {
        Layout layout = Layout.vertical(List.of(
            Constraint.proportional(1),
            Constraint.proportional(1)
        )).gap(2);
        List<Rect> rects = layout.split(new Rect(0, 0, 100, 50));
        assertEquals(0, rects.get(0).y());
        assertEquals(24, rects.get(0).height());
        assertEquals(26, rects.get(1).y());
        assertEquals(24, rects.get(1).height());
    }

    @Test
    @DisplayName("proportional with zero remaining gives zero")
    void proportionalWithZeroRemaining() {
        Layout layout = Layout.horizontal(List.of(
            Constraint.fixed(100),
            Constraint.proportional(1)
        ));
        List<Rect> rects = layout.split(new Rect(0, 0, 100, 50));
        assertEquals(99, rects.get(0).width());
        assertEquals(0, rects.get(1).width());
    }

    @Test
    @DisplayName("mixed constraints with proportional")
    void mixedConstraints() {
        Layout layout = Layout.horizontal(List.of(
            Constraint.fixed(10),
            Constraint.percentage(20),
            Constraint.proportional(3),
            Constraint.proportional(1)
        ));
        List<Rect> rects = layout.split(new Rect(0, 0, 100, 50));
        assertEquals(10, rects.get(0).width());
        assertEquals(19, rects.get(1).width());
        assertTrue(rects.get(2).width() > 0);
        assertTrue(rects.get(3).width() > 0);
    }

    @Test
    @DisplayName("empty constraints list")
    void emptyConstraints() {
        Layout layout = Layout.horizontal(List.of());
        List<Rect> rects = layout.split(new Rect(0, 0, 100, 50));
        assertTrue(rects.isEmpty());
    }

    @Test
    @DisplayName("vertical layout maintains x and width")
    void verticalMaintainsXWidth() {
        Layout layout = Layout.vertical(List.of(
            Constraint.proportional(1)
        ));
        List<Rect> rects = layout.split(new Rect(5, 10, 80, 40));
        assertEquals(5, rects.get(0).x());
        assertEquals(80, rects.get(0).width());
    }

    @Test
    @DisplayName("default gap is 1")
    void defaultGap() {
        Layout layout = Layout.horizontal(List.of(
            Constraint.proportional(1),
            Constraint.proportional(1)
        ));
        List<Rect> rects = layout.split(new Rect(0, 0, 100, 50));
        assertEquals(49, rects.get(0).width());
        assertEquals(50, rects.get(1).x());
    }
}
