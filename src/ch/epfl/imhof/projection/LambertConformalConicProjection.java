package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

public class LambertConformalConicProjection implements Projection {
    private final double referenceLongitude;
    private final double referenceLatitude;
    private final double n;
    private final double F;
    private final double rhoZero;

    public LambertConformalConicProjection(double referenceLongitude,
            double referenceLatitude, double standardPar1,
            double standardPar2) {
        this.referenceLatitude = Math.toRadians(referenceLatitude);
        this.referenceLongitude = Math.toRadians(referenceLongitude);
        double standardParallel1 = Math.toRadians(standardPar1);
        double standardParallel2 = Math.toRadians(standardPar2);

        n = Math.log(Math.cos(standardParallel1)
                * (1 / Math.cos(standardParallel2)))
                / (Math.log(Math.tan(Math.PI / 4d + standardParallel2 / 2d)
                        * (1 / Math.tan(Math.PI / 4d + standardParallel1 / 2d))));
        F = (Math.cos(standardParallel1) * Math.pow(
                Math.tan(Math.PI / 4d + 0.5 * standardParallel1), n))
                / n;
        rhoZero = F
                * Math.pow(
                        (1 / Math.tan(Math.PI / 4d + referenceLatitude / 2d)),
                        n);
    }
    public LambertConformalConicProjection(double standardPar1, double standardPar2){
        this(0,0,standardPar1, standardPar2);
    }

    @Override
    public Point project(PointGeo point) {
        double rho = F
                * Math.pow(
                        (1 / Math.tan(Math.PI / 4d + point.latitude() / 2d)), n);
        double x = rho * Math.sin(n * (point.longitude() - referenceLongitude));
        double y = rhoZero - rho
                * Math.cos(n * (point.longitude() - referenceLongitude));
        return new Point(x, y);
    }

    @Override
    public PointGeo inverse(Point point) {
        double rho = Math.signum(n)
                * Math.sqrt(Math.pow(point.x(), 2)
                        + Math.pow(rhoZero - point.y(), 2));
        double theta = Math.atan(point.x() / (rhoZero - point.y()));
        double longitude = referenceLatitude + theta / n;
        double latitude = 2 * Math.atan(Math.pow(F / rho, 1 / n)) - Math.PI
                / 2d;
        return new PointGeo(longitude, latitude);
    }

}
