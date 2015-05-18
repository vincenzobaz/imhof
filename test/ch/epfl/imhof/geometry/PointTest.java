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
        Point point1 = new Point(2.44444444444, 3.889898989);
        Point point2 = new Point(17.1989814, 25.234234324);
        Point point3 = new Point(-500, 230);
        Point point4 = new Point(6.12345678, 4.9876543);
        Function<Point, Point> blueToRed = Point.alignedCoordinateChange(
                point1, point2, point3, point4);
        Point origin = blueToRed.apply(point1); 
        assertEquals(origin.x(), point2.x(), DELTA);
        assertEquals(origin.y(), point2.y(), DELTA);
        Point origin2 = blueToRed.apply(point3);
        assertEquals(origin2.x(), point4.x(), DELTA);
        assertEquals(origin2.y(), point4.y(), DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidPointsThrowException() {
        Function<Point, Point> blueToRed = Point.alignedCoordinateChange(
                new Point(1, -1), new Point(5, 4), new Point(-1.5, -1),
                new Point(0, 0));
    }
}
