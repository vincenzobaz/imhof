package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3D;

public class MultiDigitalElevationModel  {
    private final HGTDigitalElevationModel firstDEM;
    private final HGTDigitalElevationModel secondDEM;

    public MultiDigitalElevationModel(HGTDigitalElevationModel firstDEM,
            HGTDigitalElevationModel secondDEM) {
        this.firstDEM = firstDEM;
        this.secondDEM = secondDEM;
    }

    @Override
    public void close() throws Exception {
        firstDEM.close();
        secondDEM.close();
    }

    @Override
    public Vector3D normalAt(PointGeo point) throws IllegalArgumentException {
        return firstDEMContainsPoint(point) ? firstDEM.normalAt(point)
                : secondDEM.normalAt(point);
    }

    @Override
    public int latitudeSW() {
        return firstDEM.latitudeSW();
    }

    @Override
    public int longitudeSW() {
        return firstDEM.longitudeSW();
    }

    private boolean firstDEMContainsPoint(PointGeo point) {
        return (Math.toDegrees(point.latitude()) >= firstDEM.latitudeSW()
                && Math.toDegrees(point.latitude()) < firstDEM.latitudeSW() + 1
                && Math.toDegrees(point.longitude()) >= firstDEM.longitudeSW() && Math
                .toDegrees(point.longitude()) < firstDEM.longitudeSW() + 1);
    }
