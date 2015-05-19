package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3D;

public class MultiDigitalElevationModel implements DigitalElevationModel {
    private static final double ONE_DEGREE = Math.toRadians(1);
    HGTDigitalElevationModel firstDEM;
    HGTDigitalElevationModel secondDEM;
    HGTDigitalElevationModel thirdDEM;
    HGTDigitalElevationModel fourthDEM;

    public MultiDigitalElevationModel(HGTDigitalElevationModel firstDEM,
            HGTDigitalElevationModel secondDEM) {
        this.firstDEM = firstDEM;
        this.secondDEM = secondDEM;
        thirdDEM = null;
        fourthDEM = null;
    }

    public MultiDigitalElevationModel(HGTDigitalElevationModel firstDEM,
            HGTDigitalElevationModel secondDEM,
            HGTDigitalElevationModel thirdDEM,
            HGTDigitalElevationModel fourthDEM) {
        this.firstDEM = firstDEM;
        this.secondDEM = secondDEM;
        this.thirdDEM = thirdDEM;
        this.fourthDEM = fourthDEM;
    }

    @Override
    public void close() throws Exception {
        firstDEM.close();
        secondDEM.close();
        if (thirdDEM != null) {
            thirdDEM.close();
            fourthDEM.close();
        }
    }

    @Override
    public Vector3D normalAt(PointGeo point) throws IllegalArgumentException {
        if (thirdDEM == null) {
            return DEMContainsPoint(point, firstDEM) ? firstDEM.normalAt(point)
                    : secondDEM.normalAt(point);
        } else {
            switch (demNumber(point)) {
            case 1:
                return firstDEM.normalAt(point);
            case 2:
                return secondDEM.normalAt(point);
            case 3:
                return thirdDEM.normalAt(point);
            case 4:
                return fourthDEM.normalAt(point);
            default:
                return null;
            }
        }
    }

    private int demNumber(PointGeo point) {
        if (point.latitude() - firstDEM.latitudeSW() >= ONE_DEGREE) {
            return DEMContainsPoint(point, thirdDEM) ? 3 : 4;
        } else {
            return DEMContainsPoint(point, firstDEM) ? 1 : 2;
        }
    }

    private boolean DEMContainsPoint(PointGeo point,
            HGTDigitalElevationModel dem) {
        return (point.latitude() >= dem.latitudeSW()
                && point.latitude() < dem.latitudeSW() + ONE_DEGREE
                && point.longitude() >= dem.longitudeSW() && point.longitude() < dem
                .longitudeSW() + ONE_DEGREE);
    }
}
