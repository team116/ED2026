package frc.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ConstantsTest {

    @Test
    void scalePowerValPositveRangeAboveZero() {
        double min = 1.0;
        double max = 10.0;

        assertEquals(10.0 , Constants.scalePowerVal(-1.0, min, max));
        assertEquals(1.0 , Constants.scalePowerVal(1.0, min, max));
        assertEquals(5.5 , Constants.scalePowerVal(0.0, min, max));
        assertEquals(7.75 , Constants.scalePowerVal(-0.5, min, max));
        assertEquals(3.25 , Constants.scalePowerVal(0.5, min, max));
    }
    
    @Test
    void scalePowerValPositveRangeBelowZero() {
        double min = -40.0;
        double max = 0.0;

        assertEquals(0.0 , Constants.scalePowerVal(-1.0, min, max));
        assertEquals(-40.0 , Constants.scalePowerVal(1.0, min, max));
        assertEquals(-20.0 , Constants.scalePowerVal(0.0, min, max));
        assertEquals(-10.0 , Constants.scalePowerVal(-0.5, min, max));
        assertEquals(-30.0 , Constants.scalePowerVal(0.5, min, max));
    }

    @Test
    void scalePowerValNegativeRangeBelowZero() {
        double min = 0.0;
        double max = -40.0;

        assertEquals(-40.0 , Constants.scalePowerVal(-1.0, min, max));
        assertEquals(0.0 , Constants.scalePowerVal(1.0, min, max));
        assertEquals(-20.0 , Constants.scalePowerVal(0.0, min, max));
        assertEquals(-30.0 , Constants.scalePowerVal(-0.5, min, max));
        assertEquals(-10.0 , Constants.scalePowerVal(0.5, min, max));
    }

    @Test
    void scalePowerValZeroRange() {
        double min = 5.0;
        double max = 5.0;

        assertEquals(5.0 , Constants.scalePowerVal(-1.0, min, max));
        assertEquals(5.0 , Constants.scalePowerVal(1.0, min, max));
        assertEquals(5.0 , Constants.scalePowerVal(0.0, min, max));
        assertEquals(5.0 , Constants.scalePowerVal(-0.5, min, max));
        assertEquals(5.0 , Constants.scalePowerVal(0.5, min, max));
    }
}
