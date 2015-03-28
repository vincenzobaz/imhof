package ch.epfl.imhof.geometry;

/**
 * Classe définissant l'objet point en coordonnées cartésiennes. Celui-ci est
 * caractérisé par une coordonnée horizontale x et une coordonnée verticale y.
 * On fournit deux méthodes d'accès aux attributs. La classe est immuable.
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
}
