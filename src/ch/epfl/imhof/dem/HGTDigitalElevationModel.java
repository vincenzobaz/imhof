package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3D;

public final class HGTDigitalElevationModel implements DigitalElevationModel {
    private ShortBuffer buffer;
    private final double latitudeNW;
    private final double longitudeNW;
    private final InputStream stream;
    private final int pointsPerLine;

    public HGTDigitalElevationModel(File model)
            throws IllegalArgumentException, IOException {
        String filename = model.getName();
        if (filename.length() != 11) {
            throw new IllegalArgumentException(
                    "La taille du nom de fichier n'est pas valide.");
        }

        int latitude = 0;
        if (filename.charAt(0) != 'N' && filename.charAt(0) != 'S') {
            throw new IllegalArgumentException(
                    "La première lettre du nom du fichier n'est pas valide.");
        } else {
            latitude = filename.charAt(0) == 'N' ? 1 : -1;
        }

        try {
            latitude = latitude * Integer.parseInt(filename.substring(1, 3));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La deuxième ou la troisième lettre n'est pas un entier.");
        }

        int longitude = 0;
        if (filename.charAt(3) != 'E' && filename.charAt(3) != 'W') {
            throw new IllegalArgumentException(
                    "La quatrième lettre du nom du fichier n'est pas valide.");
        } else {
            longitude = filename.charAt(3) == 'E' ? 1 : -1;
        }

        try {
            longitude = longitude * Integer.parseInt(filename.substring(4, 7));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La cinquième, sixième ou septième lettre du nom du fichier n'est pas un entier.");
        }

        if (!filename.substring(7).equals(".hgt")) {
            throw new IllegalArgumentException("Extension du fichier invalide.");
        }
        double points = Math.sqrt(model.length() / 2L);
        if (points % 1d != 0) {
            throw new IllegalArgumentException(
                    "Dimensions du fichier invalides.");
        }
        pointsPerLine = (int) points;
        try (FileInputStream stream = new FileInputStream(model)) {
            this.stream = stream;
            buffer = stream.getChannel()
                    .map(MapMode.READ_ONLY, 0, model.length()).asShortBuffer();
        }
        latitudeNW = Math.toRadians(latitude + 1);
        longitudeNW = Math.toRadians(longitude);
    }

    @Override
    public void close() throws IOException {
        buffer = null;
        stream.close();
    }

    @Override
    public Vector3D normalAt(PointGeo point) throws IllegalArgumentException {
        double oneDegree = Math.toRadians(1);
        if (point.latitude() > latitudeNW
                || point.latitude() < latitudeNW - oneDegree
                || point.longitude() < longitudeNW
                || point.longitude() > longitudeNW + oneDegree) {
            throw new IllegalArgumentException(
                    "Le point fourni ne fait pas partie de la zone couverte par le MNT.");
        }

        double angularResolution = oneDegree / ((double) pointsPerLine);
        int i = (int) Math.floor((point.longitude() - longitudeNW)
                / angularResolution);
        int j = (int) Math.ceil((latitudeNW - point.latitude())
                / angularResolution);

        double s = Earth.RADIUS * angularResolution;

        double zA = altitudeAt(i + 1, j + 1) - altitudeAt(i, j + 1);
        double zB = altitudeAt(i, j) - altitudeAt(i, j + 1);
        double zC = altitudeAt(i, j) - altitudeAt(i + 1, j);
        double zD = altitudeAt(i + 1, j + 1) - altitudeAt(i + 1, j);

        return new Vector3D(0.5 * s * (zC - zA), 0.5 * s * (zD - zB), s * s);
    }

    private short altitudeAt(int i, int j) {
        return buffer.get(j * pointsPerLine + i);
    }
}
