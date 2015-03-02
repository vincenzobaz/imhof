package ch.epfl.imhof.geometry;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

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
    private final List<Point> points;

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
            if (points.size() == 0 || points == null) {
                throw new IllegalArgumentException(
                        "La liste des points est vide");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        ArrayList<Point> temp = new ArrayList<>();
        temp.addAll(0, points);
        this.points = Collections.unmodifiableList(temp);
        //this.points = Collections.unmodifiableList(new ArrayList<>(points));
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
        private List<Point> points;

        public Builder() {
            points = new ArrayList<>();
        }
        
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
