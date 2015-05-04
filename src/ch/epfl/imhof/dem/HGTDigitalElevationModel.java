package ch.epfl.imhof.dem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import static java.lang.Math.toRadians;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3D;

public final class HGTDigitalElevationModel implements DigitalElevationModel {
    private final ShortBuffer buffer;
    private final double latitudeNW;
    private final double longitudeNW;
    private final double HGTResolution;
    private final InputStream stream;

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
            latitude = latitude * Integer.parseInt(filename.substring(1, 2));
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
            longitude = longitude * Integer.parseInt(filename.substring(4, 6));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La cinquième, sixième ou septième lettre du nom du fichier n'est pas un entier.");
        }

        if (!filename.substring(7).equals(".hgt")) {
            throw new IllegalArgumentException("Extension du fichier invalide.");
        }
        // pas sûr de ça, voir piazza checker si resolution est bien valide, si
        // oui l'affecter à hgtresolution, si non erreur
        if (Math.floorMod(model.length(), 2) == 0) {
            throw new IllegalArgumentException(
                    "dimensions du fichier invalides");
        }

        double bufferSize = model.length() / 2L;
        try (FileInputStream stream = new FileInputStream(model)) {
            this.stream = stream;
            buffer = stream.getChannel()
                    .map(MapMode.READ_ONLY, 0, model.length()).asShortBuffer();
        }
        latitudeNW = toRadians(latitude+1);
        longitudeNW = toRadians(longitude);
        // FAUX pour la résolution: elle se calcule à partir de length. length
        // nous donne le nombre de poitns décrit dans le fichier mais après je
        // pense qu'il faut faire des calcules pour trouver la vrai resolution
        HGTResolution = toRadians(1d / Math.sqrt(bufferSize));
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }

    @Override
    public Vector3D normalAt(PointGeo point) throws IllegalArgumentException {
        double oneDegree =toRadians(1);
        if (point.latitude() < latitudeNW
                || point.latitude() > latitudeNW + oneDegree
                || point.longitude() < longitudeNW
                || point.longitude() > longitudeNW + oneDegree) {
            throw new IllegalArgumentException(
                    "Le point fourni ne fait pas partie de la zone couverte par le MNT.");
        }
        
        // on doit convertir (i,j) en un k car notre buffer est un tableau/list
        // à une dimension
        // il y a un total de HGTResolution points
        // comment trouver le i,j du point reçu en paramètre?
        long zIJ = buffer.get();
        double s = Earth.RADIUS * HGTResolution;
        
        
        /*
         * Vector3D a = new Vector3D(s, 0d, ); Vector3D b = new Vector3D(0d, s,
         * z); Vector3D c = new Vector3D(-s, 0d, ); Vector3D d = new
         * Vector3D(0d, -s );
         */
        return new Vector3D(0, 0, 0);
    }
}
