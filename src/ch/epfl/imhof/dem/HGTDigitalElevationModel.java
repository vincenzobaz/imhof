package ch.epfl.imhof.dem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3D;

public class HGTDigitalElevationModel implements DigitalElevationModel {
    private final ShortBuffer buffer;
    private final double latitudeSW;
    private final double longitudeSW;

    public HGTDigitalElevationModel(File model)
            throws IllegalArgumentException, IOException {
        String filename = model.getName();
        int latitude = 0;
        if (filename.charAt(0) != 'N' && filename.charAt(0) != 'S')
            throw new IllegalArgumentException(
                    "La première lettre du nom du fichier n'est pas valide");
        else
            latitude = filename.charAt(0) == 'N' ? 1 : -1;

        try {
            latitude = latitude * Integer.parseInt(filename.substring(1, 2));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "la deuxième ou la troisième lettre n'est pas un entier");
        }
        int longitude = 0;
        if (filename.charAt(3) != 'E' && filename.charAt(3) != 'W')
            throw new IllegalArgumentException(
                    "La quatrième lettre du nom du fichier n'est pas valide");
        else
            longitude = filename.charAt(3) == 'E' ? 1 : -1;
        try {
            longitude = longitude * Integer.parseInt(filename.substring(4, 6));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La cinquième, sixième ou septième lettre du nom du fichier ne sont pas un entier");
        }

        if (!filename.substring(7).equals(".hgt"))
            throw new IllegalArgumentException("Extension du fichier invalide");
        if (Math.floorMod(model.length(), 2) == 0)
            throw new IllegalArgumentException(
                    "dimensions du fichier invalides");
        latitudeSW = Math.toRadians(latitude);
        longitudeSW = Math.toRadians(longitude);
        buffer = mapToMemory(model);
    }

    private ShortBuffer mapToMemory(File file) throws IOException {
        try (FileInputStream stream = new FileInputStream(file)) {
            return stream.getChannel().map(MapMode.READ_ONLY, 0, file.length())
                    .asShortBuffer();
        }
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
