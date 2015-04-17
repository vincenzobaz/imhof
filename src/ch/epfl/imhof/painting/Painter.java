package ch.epfl.imhof.painting;

import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.util.Iterator;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public interface Painter<E> {
    void drawMap(Map map, Canvas canvas);

    public static Painter<Polygon> polygon(Color fillColor) {
        return (map, canvas) -> {
            for (Attributed<Polygon> attributed : map.polygons()) {
            }
        };
    }

    public static Painter<PolyLine> line(float width, Color color,
            LineStyle.LineCap cap, LineStyle.LineJoin join,
            float[] dashingPattern) {
        LineStyle style = new LineStyle(width, color, cap, join, dashingPattern);
        return (map, canvas) -> {
        };
    }
}
