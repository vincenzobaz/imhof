package ch.epfl.imhof.painting;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.util.Iterator;
import java.util.function.Function;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Classe représentant une toile. Elle permet de dessiner des polylignes et des
 * polygones dans une image discrète.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class Java2DCanvas implements Canvas {
    // pas besoin de tous ces trucs en fait
    private Point bottomLeft;
    private Point topRight;
    private int dpi;
    // Color on le garde même si il est stocké dans le contexte RAPPELLE TOI DE
    // FAIRE LA MÊME CHOSE DANS LE CONSTRUCTEUR
    private Color backgroundColor;

    // ceux_là on en a besoin
    private final Function<Point, Point> basisChange;
    private final BufferedImage image;
    private final Graphics2D context;

    /**
     * Construit une toile ayant pour coins les deux points fournis, la largeur,
     * la hauteur et la résolution données, et pour couleur de fond la couleur
     * donnée.
     * 
     * @param bottomLeft
     *            le coin bas-gauche de la toile
     * @param topRight
     *            le coin haut-droite de la toile
     * @param width
     *            la largeur de la toile
     * @param height
     *            l hauteur de la toile
     * @param dpi
     *            la résolution de la toile
     * @param backgroundColor
     *            la couleur de fond de la toile
     * @throws IllegalArgumentException
     *             lève une exception si la largeur, la hauteur ou la résolution
     *             sont négatives ou nulles, ou si le premier point se trouve
     *             plus en haut ou plus à doite que le deuxième point
     */
    // changer le type de width, height et dpi en float?
    public Java2DCanvas(Point bottomLeft, Point topRight, int width,
            int height, int dpi, Color backgroundColor)
            throws IllegalArgumentException {
        if (width <= 0 || height <= 0 || dpi <= 0) {
            throw new IllegalArgumentException("negative width, heigth or dpi");
        }
        if (bottomLeft.x() >= topRight.x() || bottomLeft.y() >= topRight.y()) {
            throw new IllegalArgumentException(
                    "La position des coins n'est pas valide.");
        }

        // pas besoin
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.dpi = dpi;
        this.backgroundColor = backgroundColor;

        double scale = dpi / 72.0;
        this.basisChange = Point.alignedCoordinateChange(bottomLeft, new Point(
                0d, height * scale), topRight, new Point(width * scale, 0d));

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        context = image.createGraphics();
        context.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        context.scale(scale, scale);
        context.setColor(backgroundColor.convert());
        context.fillRect(0, 0, width, height);
    }

    /**
     * Retourne l'image dessinée par la toile
     * 
     * @return l'image
     */
    public BufferedImage image() {
        return image;
    }

    @Override
    public Path2D drawPolyLine(PolyLine line, LineStyle style) {
        int cap = cap(style.getCap());
        int join = join(style.getJoin());
        context.setStroke(new BasicStroke(style.getWidth(), cap, join, 10.0f,
                style.getDashingPattern(), 0));
        context.setColor(style.getColor().convert());

        Path2D way = new Path2D.Double();
        Iterator<Point> iterator = line.points().iterator();
        Point firstPoint = basisChange.apply(iterator.next());
        way.moveTo(firstPoint.x(), firstPoint.y());
        while (iterator.hasNext()) {
            Point nextPoint = basisChange.apply(iterator.next());
            way.lineTo(nextPoint.x(), nextPoint.y());
        }
        if (line.isClosed()) {
            way.closePath();
        }
        context.draw(way);
        return way;
    }

    @Override
    public void drawPolygon(Polygon polygon, LineStyle style, Color color) {
        context.setColor(color.convert());
        Area area = new Area(drawPolyLine(polygon.shell(), style));
        for (ClosedPolyLine hole : polygon.holes()) {
            area.subtract(new Area(drawPolyLine(hole, style)));
        }
        context.fill(area);
    }

    /**
     * Retoune la valeur entière correspondant au type de terminaison de la
     * polyligne.
     * 
     * @param capStyle
     *            la terminaison de la polyligne
     * @return l'entier correspondant à la terminaison dans la classe
     *         <code>BasicStroke</code>
     */
    private int cap(LineStyle.LineCap capStyle) {
        switch (capStyle) {
        case BUTT:
            return BasicStroke.CAP_BUTT;
        case ROUND:
            return BasicStroke.CAP_ROUND;
        case SQUARE:
            return BasicStroke.CAP_SQUARE;
        default:
            return 0;
        }
    }

    /**
     * Retourne la valeur entière correspondant au type de jointure entre deux
     * segments.
     * 
     * @param joinStyle
     *            la jointure
     * @return l'entier correspondant à la jointure dans la classe
     *         <code>BasicStroke</code>
     */
    private int join(LineStyle.LineJoin joinStyle) {
        switch (joinStyle) {
        case BEVEL:
            return BasicStroke.JOIN_BEVEL;
        case MITER:
            return BasicStroke.JOIN_MITER;
        case ROUND:
            return BasicStroke.JOIN_ROUND;
        default:
            return 0;
        }
    }
}
