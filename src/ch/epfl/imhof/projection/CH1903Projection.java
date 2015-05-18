package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * Conversion des coordonnées sphériques d'un point à la surface de la Terre en
 * coordonnées cartésiennes et viceversa dans le cadre de la projection CH1903.
 * Cette classe ne contient que des méthodes. La classe est immuable. Elle
 * implémente <code>Projection</code>.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 * 
 */
public final class CH1903Projection implements Projection {

    @Override
    public Point project(PointGeo point) {
        double longitude = (Math.toDegrees(point.longitude()) * 3600 - 26782.5) / 10000d;
        double latitude = (Math.toDegrees(point.latitude()) * 3600 - 169028.66) / 10000d;
        double longitudeSquared = longitude * longitude;
        double latitudeSquared = latitude * latitude;
        double x = 600072.37 + 211455.93 * longitude - 10938.51 * longitude
                * latitude - 0.36 * longitude * latitudeSquared - 44.54
                * longitudeSquared * longitude;
        double y = 200147.07 + 308807.95 * latitude + 3745.25
                * longitudeSquared + 76.63 * latitudeSquared - 194.56
                * longitudeSquared * latitude + 119.79 * latitudeSquared
                * latitude;
        return new Point(x, y);
    }

    @Override
    public PointGeo inverse(Point point) {
        double x1 = (point.x() - 600000) / 1000000d;
        double y1 = (point.y() - 200000) / 1000000d;
        double x1Squared = x1 * x1;
        double y1Squared = y1 * y1;
        double lambda0 = 2.6779094 + 4.728982 * x1 + 0.791484 * x1 * y1
                + 0.1306 * x1 * y1Squared - 0.0436 * x1Squared * x1;
        double phi0 = 16.9023892 + 3.238272 * y1 - 0.270978 * x1Squared
                - 0.002528 * y1Squared - 0.0447 * x1Squared * y1 - 0.0140
                * y1Squared * y1;
        return new PointGeo(Math.toRadians(lambda0 * 100 / 36d),
                Math.toRadians(phi0 * 100 / 36d));
    }
}
