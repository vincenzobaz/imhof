package ch.epfl.imhof.painting;

import java.awt.geom.Path2D;

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
     * @return le chemin <code>Path2D</code> correspondant à la polyligne
     */
    Path2D drawPolyLine(PolyLine polyline, LineStyle style);

    /**
     * Dessine sur la toile le polygone donné, en dessinant ses contours suivant
     * le style donné, et en le remplissant avec la couleur donnée.
     * 
     * @param polygon
     *            le polygone à dessiner
     * @param style
     *            le style des bords du polygone et de ses trous
     * @param color
     *            la couleur du polygone
     */
    void drawPolygon(Polygon polygon, LineStyle style, Color color);
}
