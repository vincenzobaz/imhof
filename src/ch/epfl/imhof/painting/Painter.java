package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

public interface Painter<E> {
    void drawMap(Map map, Canvas canvas);

    public static Painter<Polygon> polygon(Color fillColor) {
        return (map, canvas) -> {
            /*
             * for (Attributed<Polygon> attributedPolygon : map.polygons()) {
             * canvas.drawPolygon(attributedPolygon.value(), fillColor); }
             */

            map.polygons().forEach(
                    x -> canvas.drawPolygon(x.value(), fillColor));
        };
    }

    public static Painter<PolyLine> line(float width, Color color, LineCap cap,
            LineJoin join, float[] dashingPattern) {
        return (map, canvas) -> {
            /*
             * for (Attributed<PolyLine> attributedLine : map.polyLines()) {
             * canvas.drawPolyLine(attributedLine.value(), new LineStyle( width,
             * color, cap, join, dashingPattern)); }
             */

            map.polyLines().forEach(
                    x -> canvas.drawPolyLine(x.value(), new LineStyle(width,
                            color, cap, join, dashingPattern)));
        };
    }

    public static Painter<PolyLine> line(float width, Color color) {
        // return line(width, color, LineCap.BUTT, LineJoin.MITER, new
        // float[0]);
        return (map, canvas) -> {
            map.polyLines().forEach(
                    x -> canvas.drawPolyLine(x.value(), new LineStyle(width,
                            color)));
        };
    }

    public static Painter<PolyLine> outline(float width, Color color,
            LineCap cap, LineJoin join, float[] dashingPattern) {
        return (map, canvas) -> {
            for (Attributed<Polygon> attributedPolygon : map.polygons()) {
                Polygon polygon = attributedPolygon.value();
                LineStyle style = new LineStyle(width, color, cap, join,
                        dashingPattern);
                canvas.drawPolyLine(polygon.shell(), style);
                for (PolyLine hole : polygon.holes()) {
                    canvas.drawPolyLine(hole, style);
                }
            }
        };
        
        
    }

    public static Painter<PolyLine> outline(float width, Color color) {
        return outline(width, color, LineCap.BUTT, LineJoin.MITER, new float[0]);
    }

    public default Painter<?> when(Predicate<Attributed<?>> predicate) {

    }

    public default Painter<?> above(Painter<?> painter) {

    }

    public default Painter<?> layered() {

    }
}
