package ch.epfl.imhof.dem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3D;

public class HGTDigitalElevationModel implements DigitalElevationModel {
    private final MappedByteBuffer model;

    public HGTDigitalElevationModel(File model) throws FileNotFoundException {
        FileChannel channel = new FileInputStream(model).getChannel();

    }

    public Vector3D normalAt(PointGeo point) throws IllegalArgumentException {
        if (point.latitude() < latitude || point.latitude() > latitude
                || point.longitude() < longitude
                || point.longitude() > longitude) {
            throw new IllegalArgumentException(
                    "Le point fourni ne fait pas partie de la zone couverte par le MNT.");
        }
        
        double s = Earth.RADIUS * HGTResolution;
        Vector3D a = new Vector3D(s, 0d, );
        Vector3D b = new Vector3D(0d, s, z);
        Vector3D c = new Vector3D(-s, 0d, );
        Vector3D d = new Vector3D(0, -s );
        
    }
}
