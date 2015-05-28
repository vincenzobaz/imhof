package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.geometry.Point;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.atan;
import static java.lang.Math.asin;

/**
 * Classe permettant d'utiliser une projection stéréographique.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class StereographicProjection implements Projection {
    private final double centralLatitude;
    private final double centralLongitude;

    public StereographicProjection(double centralLongitude,
            double centralLatitude) {
        this.centralLatitude = Math.toRadians(centralLatitude);
        this.centralLongitude = Math.toRadians(centralLongitude);
    }

    public StereographicProjection() {
        this(0, 0);
    }

    @Override
    public Point project(PointGeo point) {
        double k = (2 * Earth.RADIUS)
                / (1 + sin(centralLatitude) * sin(point.latitude()) + cos(centralLatitude)
                        * cos(point.latitude())
                        * cos(point.longitude() - centralLongitude));
        double x = k * cos(point.latitude())
                * sin(point.longitude() - centralLongitude);
        double y = k
                * (cos(centralLatitude) * sin(point.latitude()) - sin(centralLatitude)
                        * cos(point.latitude())
                        * cos(point.longitude() - centralLongitude));
        return new Point(x, y);
    }

    @Override
    public PointGeo inverse(Point point) {
        double rho = Math.sqrt(Math.pow(point.x(), 2) + Math.pow(point.y(), 2));
        double c = 2 * atan(rho / (2 * Earth.RADIUS));
        double longitude = centralLongitude
                + atan((point.x() * sin(c))
                        / (rho * cos(centralLatitude) * cos(c) - point.y()
                                * sin(centralLatitude) * sin(c)));
        double latitude = asin(cos(c) * sin(centralLatitude)
                + (point.y() * sin(c) * cos(centralLatitude)) / rho);
        return new PointGeo(longitude, latitude);
    }
}
