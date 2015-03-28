package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * Conversion des coordonnées sphériques d'un point à la surface de la Terre en
 * coordonnées cartésiennes et viceversa dans le cadre de la projection
 * équirectangulaire. Cette classe ne contient que des méthodes. La classe est
 * immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 * 
 */
public final class EquirectangularProjection implements Projection {

    /**
     * Convertit un PointGeo en Point.
     * 
     * @param point
     *            le point en coordonnées sphériques à projeter
     * @return le point projeté en coordonnées cartésiennes
     */
    @Override
    public Point project(PointGeo point) {
        return new Point(point.longitude(), point.latitude());
    }

    /**
     * Convertit un Point en PointGeo.
     * 
     * @param point
     *            le point en coordonnées cartésiennes à dé-projeter
     * @return le point en coordonnées sphériques, déprojeté du plan
     */
    @Override
    public PointGeo inverse(Point point) {
        return new PointGeo(point.x(), point.y());
    }
}
