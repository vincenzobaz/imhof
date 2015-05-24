package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.geometry.Point;

public final class MercatorProjection implements Projection {

    @Override
    public Point project(PointGeo point) {
        double x = Earth.RADIUS * point.longitude();
        double y = Earth.RADIUS
                * Math.log(Math.tan(Math.PI / 4d + point.latitude() / 2d));
        return new Point(x, y);
    }

    @Override
    public PointGeo inverse(Point point) {
        double longitude = point.x() / Earth.RADIUS;
        double latitude = 2d * Math.atan(Math.exp(point.y() / Earth.RADIUS))
                - Math.PI / 2d;
        return new PointGeo(longitude, latitude);
    }

}
