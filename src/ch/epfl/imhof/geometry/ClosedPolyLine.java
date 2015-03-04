package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * Classe définissant l'objet polyligne fermée. On fournit un constructeur
 * public et une méthode fournissant l'aire comprise dans la polyligne. La
 * classe est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 * 
 */
public final class ClosedPolyLine extends PolyLine {
    /**
     * Construit une polyligne fermée à partir d'une liste de points
     * 
     * @param points
     *            la liste des points qui constituent la polyligne
     */
    public ClosedPolyLine(List<Point> points) {
        super(points);
    }

    /**
     * @return true car la polyligne est fermée
     */
    @Override
    public boolean isClosed() {
        return true;
    }

    /**
     * Calcule l'aire de la polyligne
     * 
     * @return l'aire toujours positive de la polyligne
     */
    public double area() {
        Point origine = new Point(0, 0);
        double area = 0;
        for (int i = 0; i < points().size(); i++) {
            area += signedTriangleArea(origine, getVertex(i), getVertex(i + 1));
        }
        return Math.abs(area);
    }

    /**
     * Méthode interne utile pour vérifier si deux points ont les mêmes
     * coordonnées.
     * 
     * @param point1
     *            le premier point
     * @param point2
     *            le deuxième point
     * @return On retourne un boolean: true si les points coincident, false
     *         s'ils ont des coordonnées différentes
     *
     *         private boolean samePoint(Point point1, Point point2) { return
     *         (point1.x() == point2.x() && point1.y() == point2.y()); }
     */

    /**
     * Méthode interne qui vérifie si un point se trouve à gauche d'un segment
     * en étudiant le signe de l'aire signée
     * 
     * @param p
     *            le point dont on veut vérifier la position
     * @param a
     *            le premier point du segment
     * @param b
     *            le deuxième point du segment
     * @return On returne true si le point se trouve à gauche du segment, false
     *         s'il est à sa droite
     */
    private boolean isOnTheLeft(Point p, Point a, Point b) {
        return (signedTriangleArea(p, a, b) > 0);
    }

    /**
     * Méthode interne qui calcule l'aire signée d'un triangle définit par ses
     * trois sommets.
     * 
     * @param a
     *            le premier sommet
     * @param b
     *            le deuxième sommet
     * @param c
     *            le troisième sommet
     * @return On retourne un double, l'aire signée du triangle
     */
    private double signedTriangleArea(Point a, Point b, Point c) {
        return 0.5 * ((b.x() - a.x()) * (c.y() - a.y()) - (c.x() - a.x())
                * (b.y() - a.y()));
    }

    /**
     * Méthode interne fournissant un sommet d'indice généralisé
     * 
     * @param n
     *            l'indice du point qu'on veut généraliser
     * 
     * @return on retourne le point d'indice généralisé n
     */
    private Point getVertex(int n) {
        return points().get(Math.floorMod(n, points().size()));
    }

    /**
     * Méthode qui permet de vérifier si un point se trouve dans une polyligne
     * fermée
     * 
     * @param p
     *            le point dont on veut vérifier l'appartenance à la polyligne
     *            fermée
     * @return true si le point se trouve dans le polyligne, false dans le cas
     *         contraire
     */
    public boolean containsPoint(Point p) {
        int index = 0;
        for (int i = 0; i < points().size(); i++) {
            if (getVertex(i).y() <= p.y()) {
                if (getVertex(i + 1).y() > p.y()
                        && isOnTheLeft(p, getVertex(i), getVertex(i + 1))) {
                    index++;
                }
            } else if (getVertex(i + 1).y() <= p.y()
                    && isOnTheLeft(p, getVertex(i + 1), getVertex(i))) {
                index--;
            }
        }
        return index != 0;
    }
}
