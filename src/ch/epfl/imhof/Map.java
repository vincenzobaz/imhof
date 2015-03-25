package ch.epfl.imhof;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Cette classe, immuable, représente une carte projetée contenant des entité
 * géométriques attribuées (Attributed)
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class Map {
    private final List<Attributed<PolyLine>> polyLines;
    private final List<Attributed<Polygon>> polygons;

    /**
     * Constructeur de la classe
     * 
     * @param polyLines
     *            liste des Polyligne Attributed
     * @param polygons
     *            liste des Polygons Attributed
     */
    public Map(List<Attributed<PolyLine>> polyLines,
            List<Attributed<Polygon>> polygons) {
        this.polyLines = Collections
                .unmodifiableList(new ArrayList<>(polyLines));
        this.polygons = Collections.unmodifiableList(new ArrayList<>(polygons));
    }

    /**
     * Accesseur de la liste de polylignes attribuées
     * 
     * @return la liste des polylignes attribuées
     */
    public List<Attributed<PolyLine>> polyLines() {
        return polyLines;
    }

    /**
     * Accesseur de la liste de polygones attribués
     * 
     * @return la liste des polygones attribués
     */
    public List<Attributed<Polygon>> polygons() {
        return polygons;
    }

    /**
     * Bâtisseur de la classe Map
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static final class Builder {
        List<Attributed<PolyLine>> polyLines;
        List<Attributed<Polygon>> polygons;

        /**
         * Constructeur par défaut du bâtisseur
         */
        public Builder() {
            polyLines = new ArrayList<>();
            polygons = new ArrayList<>();
        }

        /**
         * Méthode permettant d'ajouter une polyligne attribuée
         * 
         * @param newPolyLine
         */
        public void addPolyLine(Attributed<PolyLine> newPolyLine) {
            polyLines.add(newPolyLine);
        }

        /**
         * Méthode permettant d'ajouter un polygone attribué
         */
        public void addPolygon(Attributed<Polygon> newPolygon) {
            polygons.add(newPolygon);
        }

        /**
         * Méthode de construction d'une Map à partir de son bâtisseur
         * 
         * @return la map
         */
        public Map build() {
            return new Map(polyLines, polygons);
        }
    }
}
