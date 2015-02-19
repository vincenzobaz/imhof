package ch.epfl.imhof.geometry;

import java.util.Collections;
import java.util.List;

/**
 * Classe abstraite définissant l'entité polyligne. On fournit deux getters:
 * l'un pour le premier point et l'autre pour la liste entière. La classe est
 * immuable
 * 
 * @author Vincenzo Bazzucchi (249733), Nicolas Phan Van (239293)
 *
 */
public abstract class PolyLine {
    private List<Point> points;

    /**
     * 
     * @param points
     *            La liste des points composant la ligne
     * 
     * @throws IllegalArgumentException
     *             lancée si la liste passé en paramètre est vide
     */
    public PolyLine(List<Point> points) throws IllegalArgumentException {
        try {
            if (points.size() == 0) {
                throw new IllegalArgumentException(
                        "La liste des points est vide");
            }
            this.points = Collections.unmodifiableList(points);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public abstract boolean isClosed();

    public List<Point> points() {
        return points;
    }

    public Point firstPoint() {
        return points.get(0);
    }

}
