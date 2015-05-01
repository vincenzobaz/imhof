package ch.epfl.imhof.dem;

import java.io.IOException;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3D;

public interface DigitalElevationModel extends AutoCloseable {
    @Override
    void close() throws IOException;

    Vector3D normalAt(PointGeo point) throws IllegalArgumentException;
}
