package ch.epfl.imhof.painting;

import java.awt.geom.Path2D;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public interface Canvas {
    Path2D drawPolyLine(PolyLine polyline, LineStyle style);

    void drawPolygon(Polygon polygon, LineStyle style, Color color);
}
