package ch.epfl.imhof.geometry;

/**
 * Classe définissant l'objet point en coordonnées cartésiennes. Celui-ci est
 * caractérisé par une coordonnée horizontale x et une coordonnée verticale y.
 * On fournit deux méthodes d'accès aux attributs. La classe est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733), Nicolas Phan Van (239293)
 * 
 */
public final class Point {
    private final double x;
    private final double y;
/**
 * Le constructeur de l'entité point. Il prend en argument les coordonnées x et y du point
 * @param x
 * @param y
 */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
/** 
 * Accesseur de la coordonnée x
 * @return x
 */
    public double x() {
        return x;
    }
/**
 * Accesseur de la coordonnée y
 * @return
 */
    public double y() {
        return y;
    }
}
