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

    /**
     * Convertit un <code>PointGeo</code> en <code>Point</code>.
     * 
     * @param point
     *            le point en coordonnées sphériques à projeter
     * @return le point projeté en coordonnées cartésiennes
     */
    @Override
    public Point project(PointGeo point) {
        double longitude = (Math.toDegrees(point.longitude()) * 3600 - 26782.5) / 10000;
        double latitude = (Math.toDegrees(point.latitude()) * 3600 - 169028.66) / 10000;
        double x = 600072.37 + 211455.93 * longitude - 10938.51 * longitude * latitude - 0.36
                * longitude * Math.pow(latitude, 2) - 44.54 * Math.pow(longitude, 3);
        double y = 200147.07 + 308807.95 * latitude + 3745.25 * Math.pow(longitude, 2)
                + 76.63 * Math.pow(latitude, 2) - 194.56 * Math.pow(longitude, 2) * latitude
                + 119.79 * Math.pow(latitude, 3);
        return new Point(x, y);
    }

    /**
     * Convertit un <code>Point</code> en <code>PointGeo</code>.
     * 
     * @param point
     *            le point en coordonnées cartésiennes à dé-projeter
     * @return le point en coordonnées sphériques, déprojeté du plan
     */
    @Override
    public PointGeo inverse(Point point) {
        double x1 = (point.x() - 600000) / 1000000;
        double y1 = (point.y() - 200000) / 1000000;
        double lambda0 = 2.6779094 + 4.728982 * x1 + 0.791484 * x1 * y1
                + 0.1306 * x1 * Math.pow(y1, 2) - 0.0436 * Math.pow(x1, 3);
        double phi0 = 16.9023892 + 3.238272 * y1 - 0.270978 * Math.pow(x1, 2)
                - 0.002528 * Math.pow(y1, 2) - 0.0447 * Math.pow(x1, 2) * y1
                - 0.0140 * Math.pow(y1, 3);
        return new PointGeo(Math.toRadians(lambda0 * 100 / 36),
                Math.toRadians(phi0 * 100 / 36));
    }
}
