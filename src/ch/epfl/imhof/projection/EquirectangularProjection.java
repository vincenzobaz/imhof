package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * coordonnées sphériques d'un point à la surface de la Terre en cordonnées
 * cartésiennes et viceversa dans le cadre de la projection équirectangulaire
 * Cette classe ne contient que des méthodes. La classe est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733), Nicolas Phan Van (239293) Conversion des
 * 
 */
public final class EquirectangularProjection implements Projection {


    public Point project(PointGeo point) {
        return new Point(point.longitude(), point.latitude());
    }
    public PointGeo inverse(Point point) {
        return new PointGeo(point.x(), point.y());
    }
}
