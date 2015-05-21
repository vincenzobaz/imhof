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
 * Classe représentant une toile, implémentant l'interface
 * {@link ch.epfl.imhof.painting.Canvas Canvas}. Elle permet de dessiner des
 * {@link ch.epfl.imhof.geometry.PolyLine PolyLine} et des
 * {@link ch.epfl.imhof.geometry.Polygon Polygon} dans une image discrète.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class Java2DCanvas implements Canvas {
    // La fonction permettant d'effectuer un changement de repère: du repère
    // cartésien au repère de l'image
    private final Function<Point, Point> basisChange;
    // L'image sur laquelle on dessine
    private final BufferedImage image;
    // La toile correspondante à l'image
    private final Graphics2D graphicContext;

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
     *            la largeur de la toile, en pixels
     * @param height
     *            la hauteur de la toile, en pixels
     * @param dpi
     *            la résolution de la toile, en pixels par pouce
     * @param backgroundColor
     *            la couleur de fond de la toile
     * @throws IllegalArgumentException
     *             lève une exception si la largeur, la hauteur ou la résolution
     *             sont négatives ou nulles, ou si le premier point se trouve
     *             plus en haut ou plus à doite que le deuxième point
     */
    public Java2DCanvas(Point bottomLeft, Point topRight, int width,
            int height, int dpi, Color backgroundColor)
            throws IllegalArgumentException {
        if (width <= 0 || height <= 0 || dpi <= 0) {
            throw new IllegalArgumentException(
                    "La largeur, la hauteur et la résolution de l'image doivent être strictement positives.");
        }
        if (bottomLeft.x() >= topRight.x() || bottomLeft.y() >= topRight.y()) {
            throw new IllegalArgumentException(
                    "La position des coins n'est pas valide.");
        }

        float scale = dpi / 72f;
        basisChange = Point.alignedCoordinateChange(bottomLeft, new Point(0d,
                height / scale), topRight, new Point(width / scale, 0d));

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        graphicContext = image.createGraphics();
        graphicContext.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphicContext.scale(scale, scale);
        graphicContext.setColor(backgroundColor.convert());
        graphicContext.fillRect(0, 0, width, height);
    }

    @Override
    public void drawPolyLine(PolyLine polyline, LineStyle style) {
        int cap = style.cap().ordinal();
        int join = style.join().ordinal();
        float[] dashingPattern = style.dashingPattern();

        // Définition du style de trait: on utilise deux constructeurs de
        // BasicStroke différents selon que le trait est continu (dashingPattern
        // null) ou bien pointillé
        graphicContext.setStroke(dashingPattern == null ? new BasicStroke(style
                .width(), cap, join, 10f) : new BasicStroke(style.width(), cap,
                join, 10f, dashingPattern, 0f));
        graphicContext.setColor(style.color().convert());

        graphicContext.draw(newPath(polyline));
    }

    @Override
    public void drawPolygon(Polygon polygon, Color color) {
        // Création d'une nouvelle surface correspondant à l'enveloppe du
        // polygone
        Area area = new Area(newPath(polygon.shell()));

        // Parcours de l'ensemble des trous du polygone et soustraction de
        // chacun de ceux-ci de la surface formée par l'enveloppe
        for (ClosedPolyLine hole : polygon.holes()) {
            area.subtract(new Area(newPath(hole)));
        }

        graphicContext.setColor(color.convert());
        graphicContext.fill(area);
    }

    /**
     * Retourne l'image dessinée par la toile.
     * 
     * @return l'image
     */
    public BufferedImage image() {
        return image;
    }

    /**
     * Construit et retourne le chemin correspondant à la polyligne donnée.
     * 
     * @param polyline
     *            la poyligne à convertir en {@link java.awt.geom.Path2D Path2D}
     * @return le chemin correspondant à la polyligne
     */
    private Path2D newPath(PolyLine polyline) {
        Path2D newPath = new Path2D.Double();
        Iterator<Point> iterator = polyline.points().iterator();

        // Conversion du premier point de la polyligne dans le repère de la
        // toile
        Point firstPoint = basisChange.apply(iterator.next());

        // Création du premier point du chemin
        newPath.moveTo(firstPoint.x(), firstPoint.y());

        // Parcours des points de la polyligne et ajout au chemin
        while (iterator.hasNext()) {
            Point nextPoint = basisChange.apply(iterator.next());
            newPath.lineTo(nextPoint.x(), nextPoint.y());
        }

        // Fermeture du chemin si la polyligne est fermée
        if (polyline.isClosed()) {
            newPath.closePath();
        }
        return newPath;
    }
}
