package ch.epfl.imhof.painting;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public final class Java2DCanvas implements Canvas {

    private Point topRight;
    private Point bottomLeft;
    private int dpi;
    // Color on le garde même si il est stocké dans le contexte RAPPELLE TOI DE
    // FAIRE LA MÊME CHOSE DANS LE CONSTRUCTEUR
    private Color backgroundColor;
    private Function<Point, Point> alignedCoordinateChange;
    BufferedImage image;
    Graphics2D context;

    public Java2DCanvas(Point bottomLeft, Point topRight, int width,
            int height, int dpi, Color backgroundColor)
            throws IllegalArgumentException {
        if (width <= 0 || height <= 0 || dpi <= 0)
            throw new IllegalArgumentException("negative width, heigth or dpi");
        if (bottomLeft.x() >= topRight.x() || bottomLeft.y() >= topRight.y())
            throw new IllegalArgumentException();
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.dpi = dpi;
        this.backgroundColor = backgroundColor;
        this.alignedCoordinateChange = Point.alignedCoordinateChange(
                bottomLeft, new Point(0d, (double) height), topRight,
                new Point((double) width, 0d));
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        context = image.createGraphics();
        context.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        context.setBackground(backgroundColor.convert());
    }

    public BufferedImage image() {
        return image;
    }

    @Override
    public void drawPolyLine(PolyLine line, LineStyle style) {
        // TODO Auto-generated method stub

    }

    @Override
    public void drawPolygon(Polygon polygon, Color color) {
        // TODO Auto-generated method stub

    }

}
