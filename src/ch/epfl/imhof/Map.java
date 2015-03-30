package ch.epfl.imhof;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Classe représentant une carte projetée contenant des entité géométriques
 * attribuées <code>Attributed</code>. La classe est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class Map {
    private final List<Attributed<PolyLine>> polyLines;
    private final List<Attributed<Polygon>> polygons;

    /**
     * Construit une carte à partir des listes de polylignes et polygones
     * attribués donnés.
     * 
     * @param polyLines
     *            la liste des polylignes attribuées de la carte
     * @param polygons
     *            la liste des polygones attribués de la carte
     */
    public Map(List<Attributed<PolyLine>> polyLines,
            List<Attributed<Polygon>> polygons) {
        this.polyLines = Collections
                .unmodifiableList(new ArrayList<>(polyLines));
        this.polygons = Collections.unmodifiableList(new ArrayList<>(polygons));
    }

    /**
     * Retourne la liste des polylignes attribuées de la carte.
     * 
     * @return les polylignes attribuées de la carte, sous forme de
     *         <code>List</code>
     */
    public List<Attributed<PolyLine>> polyLines() {
        return polyLines;
    }

    /**
     * Retourne la liste des polygones attribués de la carte.
     * 
     * @return les polygones attribués de la carte, sous forme de
     *         <code>List</code>
     */
    public List<Attributed<Polygon>> polygons() {
        return polygons;
    }

    /**
     * Bâtisseur de la classe <code>Map</code>.
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static final class Builder {
        private final List<Attributed<PolyLine>> polyLines;
        private final List<Attributed<Polygon>> polygons;

        /**
         * Constructeur par défaut du bâtisseur.
         */
        public Builder() {
            polyLines = new ArrayList<>();
            polygons = new ArrayList<>();
        }

        /**
         * Ajoute une polyligne attribuée au bâtisseur.
         * 
         * @param newPolyLine
         *            la polyligne attribuée à ajouter à la carte en
         *            construction
         */
        public void addPolyLine(Attributed<PolyLine> newPolyLine) {
            polyLines.add(newPolyLine);
        }

        /**
         * Ajoute un polygone attribué au bâtisseur.
         * 
         * @param newPolygon
         *            le polygone attribué à ajouter à la carte en construction
         */
        public void addPolygon(Attributed<Polygon> newPolygon) {
            polygons.add(newPolygon);
        }

        /**
         * Construit et retourne une carte avec les polylignes et polygones
         * ajoutés jusqu'à présent au bâtisseur.
         * 
         * @return un objet de type <code>Map</code> construit à partir des
         *         données fournies au bâtisseur
         */
        public Map build() {
            return new Map(polyLines, polygons);
        }
    }
}
