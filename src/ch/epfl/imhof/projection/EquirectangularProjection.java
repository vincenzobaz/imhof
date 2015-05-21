package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * Conversion des coordonnées sphériques d'un point à la surface de la Terre en
 * coordonnées cartésiennes et viceversa dans le cadre de la projection
 * équirectangulaire. Cette classe ne contient que des méthodes. La classe est
 * immuable. Elle implémente {@link ch.epfl.imhof.projection.Projection}.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 * 
 */
public final class EquirectangularProjection implements Projection {

    @Override
    public Point project(PointGeo point) {
        return new Point(point.longitude(), point.latitude());
    }

    @Override
    public PointGeo inverse(Point point) {
        return new PointGeo(point.x(), point.y());
    }
}
