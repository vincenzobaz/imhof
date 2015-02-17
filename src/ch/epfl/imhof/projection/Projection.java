package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * Cette interface est utilisé pour la conversion des coordonnées d'un système
 * de coordonnées sphériques à un système de coordonnées cartésiennes.
 * 
 * @author Vincenzo Bazzucchi (249733), Nicolas Phan Van (239293)
 * 
 */
public interface Projection {
    /**
     * Méthode returnant un Point (en coordonnées cartésiennes) à partir d'un
     * PointGeo (en coordonnées sphériques)
     * 
     * @param point
     *            le point en coordonnées sphériques
     */
    Point project(PointGeo point);
    /**
     * Méthode returnant un PointGeo (en coordonnées sphériques) à partir d'un
     * Point (en coordonnées cartésiennes)
     * 
     * @param point
     *            le point en coordonnées cartésiennes
     */
    PointGeo inverse(Point point);
}
