package ch.epfl.imhof.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import java.util.function.Function;

public class PointTest {
    private static final double DELTA = 0.000001;

    @Test
    public void xGetterReturnsX() {
        for (double x = -100; x <= 100; x += 12.32)
            assertEquals(x, new Point(x, 0).x(), DELTA);
    }

    @Test
    public void yGetterReturnsY() {
        for (double y = -100; y <= 100; y += 12.32)
            assertEquals(y, new Point(0, y).y(), DELTA);
    }

    @Test
    public void coordinateChangeIsCorrect() {
        Function<Point, Point> blueToRed = Point.alignedCoordinateChange(
                new Point(1, -1), new Point(5, 4), new Point(-1.5, 1),
                new Point(0, 0));
        Point origin = blueToRed.apply(new Point(0, 0));
        assertEquals(origin.x(), 3, DELTA);
        assertEquals(origin.y(), 2, DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidPointsThrowException() {
        Function<Point, Point> blueToRed = Point.alignedCoordinateChange(
                new Point(1, -1), new Point(5, 4), new Point(-1.5, -1),
                new Point(0, 0));
    }
}
