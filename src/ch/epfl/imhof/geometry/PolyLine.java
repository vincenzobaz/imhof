package ch.epfl.imhof.geometry;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe abstraite représentant une polyligne. Elle est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 * 
 */
public abstract class PolyLine {
    private final List<Point> points;

    /**
     * Construit une polyligne avec les sommets donnés.
     * 
     * @param points
     *            la liste des sommets composant la ligne
     * 
     * @throws IllegalArgumentException
     *             lève une exception si la liste des sommets est vide
     */
    public PolyLine(List<Point> points) throws IllegalArgumentException {
        if (points == null || points.isEmpty()) {
            throw new IllegalArgumentException("La liste des points est vide");
        }
        this.points = Collections.unmodifiableList(new ArrayList<>(points));
    }

    /**
     * Retourne vrai si et seulement si la polyligne est fermée.
     * 
     * @return <code>true</code> si la polyligne est fermée, <code>false</code>
     *         dans le cas contraire
     */
    public abstract boolean isClosed();

    /**
     * Retourne la liste des sommets de la polyligne.
     * 
     * @return les sommets de la polyligne, sous forme de liste de
     *         {@link ch.epfl.imhof.geometry.Point Point}
     */
    public List<Point> points() {
        return points;
    }

    /**
     * Retourne le premier sommet de la polyligne.
     * 
     * @return le premier point de la liste de
     *         {@link ch.epfl.imhof.geometry.Point Point}
     */
    public Point firstPoint() {
        return points.get(0);
    }

    /**
     * Bâtisseur de la classe {@link ch.epfl.imhof.geometry.PolyLine PolyLine}.
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     * 
     */
    public static final class Builder {
        private final List<Point> points;

        /**
         * Constructeur par défaut du bâtisseur.
         */
        public Builder() {
            points = new ArrayList<>();
        }

        /**
         * Ajoute le point donné à la liste des sommets de la polyligne en cours
         * de construction.
         * 
         * @param newPoint
         *            le point à ajouter à la polyligne
         */
        public void addPoint(Point newPoint) {
            points.add(newPoint);
        }

        /**
         * Construit et retourne une polyligne ouverte avec les points ajoutés
         * jusqu'à présent.
         * 
         * @return une polyligne ouverte
         */
        public OpenPolyLine buildOpen() {
            return new OpenPolyLine(points);
        }

        /**
         * Construit et retourne une polyligne fermée avec les points ajoutés
         * jusqu'à présent.
         * 
         * @return une polyligne fermée
         */
        public ClosedPolyLine buildClosed() {
            return new ClosedPolyLine(points);
        }
    }
}
