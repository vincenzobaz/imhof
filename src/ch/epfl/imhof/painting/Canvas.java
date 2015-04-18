package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Interface représentant une toile.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public interface Canvas {
    /**
     * Dessine sur la toile la polyligne donnée, suivant le style de ligne donné
     * et retourne le chemin correspondant.
     * 
     * @param polyline
     *            la polyligne à dessiner
     * @param style
     *            le style de ligne à utiliser
     */
    void drawPolyLine(PolyLine polyline, LineStyle style);

    /**
     * Dessine sur la toile le polygone donné, en le remplissant avec la couleur
     * donnée.
     * 
     * @param polygon
     *            le polygone à dessiner
     * @param color
     *            la couleur du polygone
     */
    void drawPolygon(Polygon polygon, Color color);
}
