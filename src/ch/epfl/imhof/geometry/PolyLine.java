package ch.epfl.imhof.geometry;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe abstraite définissant l'entité polyligne. On fournit deux getters:
 * l'un pour le premier point et l'autre pour la liste entière. La classe est
 * immuable
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 * 
 */
public abstract class PolyLine {
    private List<Point> points;

    /**
     * Construit une polyligne à partir d'une liste de points
     * 
     * @param points
     *            la liste des points composant la ligne
     * 
     * @throws IllegalArgumentException
     *             lancée si la liste passée en paramètre est vide
     */
    public PolyLine(List<Point> points) throws IllegalArgumentException {
        try {
            if (points.size() == 0) {
                throw new IllegalArgumentException(
                        "La liste des points est vide");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        ArrayList<Point> temp = new ArrayList<>();
        temp.addAll(0, points);
        // for (int i = 0; i < points.size(); i++) {
        // temp.add(points.get(i));
        // }
        this.points = Collections.unmodifiableList(temp);
    }

    /**
     * On impose aux sous-classes de définir une méthode permettant de
     * déterminer si la polyligne est ouverte ou fermée
     * 
     * @return true si la polyligne est ouverte, false dans le cas contraire
     */
    public abstract boolean isClosed();

    /**
     * Accesseur de la liste de points qui constituent la polyligne
     * 
     * @return la liste des points
     */
    public List<Point> points() {
        return points;
    }

    /**
     * Accesseur du premier point
     * 
     * @return le premier point de la polyligne
     */
    public Point firstPoint() {
        return points.get(0);
    }

    /**
     * Bâtisseur
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     * 
     */
    public static final class Builder {
        private List<Point> points = new ArrayList<>();

        /**
         * On ajoute un point à la polyligne
         * 
         * @param newPoint
         *            le point à ajouter
         */
        public void addPoint(Point newPoint) {
            points.add(newPoint);
        }

        /**
         * Construit une polyligne ouverte immuable
         * 
         * @return la polyligne ouverte immuable
         */
        public OpenPolyLine buildOpen() {
            return new OpenPolyLine(points);
        }

        /**
         * Construit une polyligne fermée immuable
         * 
         * @return la polyligne fermée immuable
         */
        public ClosedPolyLine buildClosed() {
            return new ClosedPolyLine(points);
        }
    }
}
