package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * Interface utilisée pour la conversion des coordonnées d'un système de
 * coordonnées sphériques à un système de coordonnées cartésiennes.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 * 
 */
public interface Projection {

    /**
     * Convertit et retourne le {@link ch.epfl.imhof.PointGeo} donné en
     * {@link ch.epfl.imhof.geometry.Point}, en le projetant sur le plan.
     * 
     * @param point
     *            le point en coordonnées sphériques à projeter
     * @return le point projeté en coordonnées cartésiennes
     */
    Point project(PointGeo point);

    /**
     * Convertit et retourne le {@link ch.epfl.imhof.geometry.Point} donné en
     * {@link ch.epfl.imhof.PointGeo}, en le dé-projetant du plan.
     * 
     * @param point
     *            le point en coordonnées cartésiennes à dé-projeter
     * @return le point en coordonnées sphériques, déprojeté du plan
     */
    PointGeo inverse(Point point);
}
