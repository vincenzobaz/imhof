package ch.epfl.imhof.painting;

import java.util.HashSet;
import java.util.Set;
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
        return (map, canvas) -> {
            Map.Builder mapB = new Map.Builder();
            for (Attributed<Polygon> p : map.polygons()) {
                if (predicate.test(p))
                    mapB.addPolygon(p);
            }
            for (Attributed<PolyLine> l : map.polyLines()) {
                if (predicate.test(l)) {
                    mapB.addPolyLine(l);
                }
            }
            this.drawMap(mapB.build(), canvas);
        };
    }

    public default Painter<?> above(Painter<?> painter) {
        return (map, canvas) -> {
            painter.drawMap(map, canvas);
            this.drawMap(map, canvas);
        };
    }

    public default Painter<?> layered() {
        Painter<?> painter = this;
        for (int layer = 5; layer > -5; layer --){
            painter = painter.when(Filters.onLayer(layer)).above(painter.when(Filters.onLayer(layer-1)));
        }
        return painter;
    }
}
