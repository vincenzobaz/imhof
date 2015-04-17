package ch.epfl.imhof.geometry;

import java.util.function.Function;

/**
 * Classe représentant un point en coordonnées cartésiennes. Elle est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 * 
 */
public final class Point {
    private final double x;
    private final double y;

    /**
     * Construit un point avec les coordonnées données.
     * 
     * @param x
     *            la coordonnée horizontale du point
     * @param y
     *            la coordonnée verticale du point
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retourne la coordonnée x du point.
     * 
     * @return la coordonnée horizontale, un double
     */
    public double x() {
        return x;
    }

    /**
     * Retourne la coordonnée y du point.
     * 
     * @return la coordonnée verticale, un double
     */
    public double y() {
        return y;
    }

    /**
     * Retourne une fonction correspondant au changement de repères alignés.
     * Prend en paramètre deux <code>Point</code>, exprimés dans le repère
     * d'origine et celui après changement, et calcule la fonction permettant de
     * convertir les coordonnées d'un point d'un repère à l'autre.
     * 
     * @param a1
     *            le premier point exprimé dans le repère d'origine
     * @param a2
     *            le premier point exprimé dans le repère d'arrivée
     * @param b1
     *            le second point exprimé dans le repère d'origine
     * @param b2
     *            le second point exprimé dans le repère d'arrivée
     * @return la fonction permettant le changement de repère
     * @throws IllegalArgumentException
     *             lève une exception si les deux points du repère d'origine
     *             sont situés sur une même ligne horizontale ou verticale, car
     *             il est alors impossible d'effectuer le changement de repère
     */
    public static Function<Point, Point> alignedCoordinateChange(Point a1,
            Point a2, Point b1, Point b2) throws IllegalArgumentException {
        if (a1.x() == b1.x()) {
            throw new IllegalArgumentException(
                    "Les deux points sont situés sur une même ligne horizontale.");
        }
        if (a1.y() == b1.y()) {
            throw new IllegalArgumentException(
                    "Les deux points sont situés sur une même ligne verticale.");
        }

        double a = (a2.x() - b2.x()) / (a1.x() - b1.x());
        double b = (b2.x() * a1.x() - a2.x() * b1.x()) / (a1.x() - b1.x());
        double c = (a2.y() - b2.y()) / (a1.y() - b1.y());
        double d = (b2.y() * a1.y() - a2.y() * b1.y()) / (a1.y() - b1.y());

        return p -> new Point(a * p.x() + b, c * p.y() + d);
    }
}
