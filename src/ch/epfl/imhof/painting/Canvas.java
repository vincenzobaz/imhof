package ch.epfl.imhof.painting;

import java.awt.image.BufferedImage;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public interface Canvas {
    void drawPolyLine(PolyLine line, LineStyle style);
    void drawPolygon(Polygon polygon, Color color);
}
