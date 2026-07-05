package com.ottertui.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class ConstraintTest {

    @Test
    @DisplayName("percentage factory creates Percentage")
    void percentageFactory() {
        Constraint c = Constraint.percentage(50);
        assertInstanceOf(Constraint.Percentage.class, c);
        assertEquals(50, ((Constraint.Percentage) c).percent());
    }

    @Test
    @DisplayName("fixed factory creates Fixed")
    void fixedFactory() {
        Constraint c = Constraint.fixed(100);
        assertInstanceOf(Constraint.Fixed.class, c);
        assertEquals(100, ((Constraint.Fixed) c).size());
    }

    @Test
    @DisplayName("min factory creates Min")
    void minFactory() {
        Constraint c = Constraint.min(25);
        assertInstanceOf(Constraint.Min.class, c);
        assertEquals(25, ((Constraint.Min) c).min());
    }

    @Test
    @DisplayName("proportional factory creates Proportional")
    void proportionalFactory() {
        Constraint c = Constraint.proportional(3);
        assertInstanceOf(Constraint.Proportional.class, c);
        assertEquals(3, ((Constraint.Proportional) c).weight());
    }
}
