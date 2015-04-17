package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public interface Canvas {
    void drawPolyLine(PolyLine polyline, LineStyle style);

    void drawPolygon(Polygon polygon, Color color);
}
