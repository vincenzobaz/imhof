package ch.epfl.imhof.geometry;

/**
 * Classe définissant l'objet point en coordonnées cartésiennes. Celui-ci est
 * caractérisé par une coordonnée horizontale x et une coordonnées verticale y.
 * On fournit deux méthodes d'accès aux attributs. La classe est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733), Nicolas Phan Van (239293)
 * 
 */
public final class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }
}
