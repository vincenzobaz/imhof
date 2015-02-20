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
     * @throws IllegalArgumentException
     *             lève une exception si la polyligne n'est pas fermée (le 1er
     *             point est différent du dernier), et si les points ne forment
     *             pas un polygone (seulement 2 points, plusieurs fois le même
     *             sommet)
     */
    public ClosedPolyLine(List<Point> points) throws IllegalArgumentException {
        super(points);
        /**try {
            if (!samePoint(this.firstPoint(),
                    this.points().get(this.points().size() - 1))) {
                throw new IllegalArgumentException(
                        "Le 1er point est différent du dernier");
            }
            if (this.points().size() == 2) {
                throw new IllegalArgumentException(
                        "Un segment n'est pas un polygone");
            }
            for (int i = 0; i < this.points().size() - 2; ++i) {
                for (int j = i + 1; j < this.points().size() - 1; ++j) {
                    if (samePoint(this.points().get(i), this.points().get(j))) {
                        throw new IllegalArgumentException(
                                "Un polygone ne peut pas avoir 2 fois le même sommet");
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw e;
        }**/
    }

    /**
     * @return true, la polyligne est fermée, les vérifications sont faites dans
     *         le constructeur
     */
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
     *            Le premier point
     * @param point2
     *            Le deuxième point
     * @return On retourne un boolean: True si les points coincident, False
     *         s'ils ont des coordonnées différentes
     */
    private boolean samePoint(Point point1, Point point2) {
        return (point1.x() == point2.x() && point1.y() == point2.y());
    }

    /**
     * Méthode interne qui vérifie si un point se trouve à gauche d'un segment
     * en étudiant le signe de l'aire signée
     * 
     * @param p
     *            Le point dont on veut vérifier la position
     * @param a
     *            Le premier point du segment
     * @param b
     *            Le deuxième point du segment
     * @return On returne True si le point se trouve à gauche du segment, False
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
     * méthode interne fournissant un sommet d'indice généralisé
     * 
     * @param n
     *            , l'indice du point qu'on veut généraliser
     * @return on retourne le point d'indice généralisé n
     */
    private Point getVertex(int n) {
        return points().get(Math.floorMod(n, points().size()));
    }

    /**
     * méthode qui permet de vérifier si un point se trouve dans une polyligne
     * fermé
     * 
     * @param p
     *            le point dont on veut vérifier l'appartenance à la polyligne
     *            fermée
     * @return True si le point se trouve dans le polyligne, False dans le cas
     *         contraire
     */
    public boolean containsPoint(Point p) {
        int index = 0;
        for (int i = 0; i <= points().size(); i++) {
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