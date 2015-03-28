package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * Cette interface est utilisée pour la conversion des coordonnées d'un système
 * de coordonnées sphériques à un système de coordonnées cartésiennes.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 * 
 */
public interface Projection {
    
    /**
     * Projette sur le plan le point PointGeo reçu en argument.
     * 
     * @param point
     *            le point en coordonnées sphériques
     */
    Point project(PointGeo point);

    /**
     *  Dé-projette le point (Point) du plan reçu en argument.
     * 
     * @param point
     *            le point en coordonnées cartésiennes
     */
    PointGeo inverse(Point point);
}
