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
     * Construit un point avec les coordonnées données
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
     * Getter pour la coordonnée horizontale
     * 
     * @return l'attribut x du point
     */
    public double x() {
        return x;
    }

    /**
     * Getter pour la coordonnée verticale
     * 
     * @return l'attribut y du point
     */
    public double y() {
        return y;
    }
}
