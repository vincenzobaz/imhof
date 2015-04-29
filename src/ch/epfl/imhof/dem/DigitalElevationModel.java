package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3D;

public interface DigitalElevationModel extends AutoCloseable {
    @Override
    void close();

    Vector3D normalAt(PointGeo point) throws IllegalArgumentException;
}
