package ch.epfl.imhof.painting;

import static org.junit.Assert.*;
import org.junit.Test;

public final class ColorTest {
    private static final double DELTA = 0.000001;

    @Test
    public void correctlyBuildsGrayColor() {
        Color gray = Color.gray(0.2);
        assertEquals(gray.redRatio(), 0.2, DELTA);
        assertEquals(gray.greenRatio(), 0.2, DELTA);
        assertEquals(gray.blueRatio(), 0.2, DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsWhenPassingIncorrectValues() {

    }

    @Test
    public void correctlyBuildsColor() {
        Color color = Color.rgb(0.13226, 0.46551, 0.79778);
        assertEquals(color.redRatio(), 0.13226, DELTA);
        assertEquals(color.greenRatio(), 0.46551, DELTA);
        assertEquals(color.blueRatio(), 0.79778, DELTA);
    }

    @Test
    public void correctlyUnwrapsColorFromBinaryNumber() {
        int binaryContainer = 0b11001100_01101011_11110110;
        Color color = Color.rgb(binaryContainer);
        assertEquals(color.redRatio() * 255d, 0b11001100, DELTA);
        assertEquals(color.greenRatio() * 255d, 0b01101011, DELTA);
        assertEquals(color.blueRatio() * 255d, 0b11110110, DELTA);
    }

    @Test
    public void correctlyMultiplysTwoColors() {
        Color c1 = Color.rgb(0.25, 0.10, 0.70);
        Color c2 = Color.rgb(0d, 0.45, 1);
        Color color = c1.multiplyWith(c2);
        assertEquals(color.redRatio(), 0, DELTA);
        assertEquals(color.greenRatio(), 0.045, DELTA);
        assertEquals(color.blueRatio(), 0.70, DELTA);
    }

    @Test
    public void correctlyConvertsColor() {
        Color c = Color.rgb(0d, 0.43, 1d);
        java.awt.Color color = c.convert();
        assertEquals(color.getRed(), 0, DELTA);
        assertEquals(color.getGreen(), 0.43 * 255, 0.5);
        assertEquals(color.getBlue(), 1 * 255, DELTA);
    }
}
