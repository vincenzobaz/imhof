package ch.epfl.imhof.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3D;

/**
 * Classe représentant un modèle numérique de terrain stocké dans un fichier au
 * format HGT. Elle implémente l'interface <code>DigitalElevationModel</code>.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class HGTDigitalElevationModel implements DigitalElevationModel {
    // Le buffer n'est pas en final, on a besoin de le réaffecter à null dans la
    // redéfinition de la méthode close.
    private ShortBuffer buffer;
    private final double latitudeSW;
    private final double longitudeSW;
    private final InputStream stream;
    private final int pointsPerLine;

    /**
     * Construit un modèle numérique de terrain à partir du fichier HGT passé en
     * argument.
     * 
     * @param model
     *            le fichier HGT contenant le modèle numérique du terrain
     * @throws IllegalArgumentException
     *             lève une exception si le nom du fichier HGT n'obéit pas aux
     *             conventions de nommage, ou si sa taille en octets divisée par
     *             deux n'a pas une racine carrée entière
     * @throws IOException
     *             lève une exception en cas d'erreur d'entrée/sortie (fichier
     *             introuvable, etc...)
     */
    public HGTDigitalElevationModel(File model)
            throws IllegalArgumentException, IOException {
        String filename = model.getName();

        // Teste si le nom du fichier a la bonne longueur
        if (filename.length() != 11) {
            throw new IllegalArgumentException(
                    "La taille du nom de fichier n'est pas valide.");
        }

        // Teste si la première lettre du nom est bien N ou S, et stockage du
        // signe de la latitude du coin sud-ouest
        int latitude = 0;
        if (filename.charAt(0) != 'N' && filename.charAt(0) != 'S') {
            throw new IllegalArgumentException(
                    "La première lettre du nom du fichier n'est pas valide.");
        } else {
            latitude = filename.charAt(0) == 'N' ? 1 : -1;
        }

        // On essaie de parser le deuxième et le troisième caractère du nom de
        // fichier. Si parseInt échoue, le fichier n'est pas valide; sinon on
        // stocke la valeur de la latitude
        try {
            latitude = latitude * Integer.parseInt(filename.substring(1, 3));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La deuxième ou la troisième lettre n'est pas un entier.");
        }

        // Teste si la quatrième lettre du nom du fichier est bien E ou W, et
        // stockage du signe de la longitude
        int longitude = 0;
        if (filename.charAt(3) != 'E' && filename.charAt(3) != 'W') {
            throw new IllegalArgumentException(
                    "La quatrième lettre du nom du fichier n'est pas valide.");
        } else {
            longitude = filename.charAt(3) == 'E' ? 1 : -1;
        }

        // On essaie de parser les caractères 5,6,7. Si parseInt lance une
        // exception, ceux-ci ne sont pas des entiers et le nom du fichier est
        // invalide; sinon on stocke la valeur de la longitude.
        try {
            longitude = longitude * Integer.parseInt(filename.substring(4, 7));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La cinquième, sixième ou septième lettre du nom du fichier n'est pas un entier.");
        }

        // On vérifie si l'extension du fichier est bien .hgt
        if (!filename.substring(7).equals(".hgt")) {
            throw new IllegalArgumentException("Extension du fichier invalide.");
        }
        // On vérifie si la taille en octets du fichier est valide
        double points = Math.sqrt(model.length() / 2L);
        if (points % 1d != 0d) {
            throw new IllegalArgumentException(
                    "Taille en octets du fichier invalide.");
        }
        pointsPerLine = (int) points;
        // Ouverture et assignation du flot et mappage de la valeur des octets
        // en mémoire
        try (FileInputStream stream = new FileInputStream(model)) {
            this.stream = stream;
            buffer = stream.getChannel()
                    .map(MapMode.READ_ONLY, 0, model.length()).asShortBuffer();
        }
        // Assignation de la longitude et de la latitude du coin sud-ouest
        latitudeSW = Math.toRadians(latitude);
        longitudeSW = Math.toRadians(longitude);
    }

    @Override
    public void close() throws IOException {
        buffer = null;
        stream.close();
    }

    @Override
    public Vector3D normalAt(PointGeo point) throws IllegalArgumentException {
        final double oneDegree = Math.toRadians(1);
        // Vérification de l'appartenance du point à la zone du fichier HGT
        if (point.latitude() < latitudeSW
                || point.latitude() > latitudeSW + oneDegree
                || point.longitude() < longitudeSW
                || point.longitude() > longitudeSW + oneDegree) {
            throw new IllegalArgumentException(
                    "Le point fourni ne fait pas partie de la zone couverte par le MNT.");
        }

        // Calcul des coordonnées du coin bas-gauche du carré dans lequel se
        // situe le point, dans le repère ayant pour origine le coin sud-ouest
        // du fichier HGT
        double angularResolution = oneDegree / ((double) pointsPerLine - 1);
        int i = (int) Math.floor((point.longitude() - longitudeSW)
                / angularResolution);
        int j = (int) Math.floor((point.latitude() - latitudeSW)
                / angularResolution);

        double s = Earth.RADIUS * angularResolution;

        double zA = altitudeAt(i + 1, j) - altitudeAt(i, j);
        double zB = altitudeAt(i, j + 1) - altitudeAt(i, j);
        double zC = altitudeAt(i, j + 1) - altitudeAt(i + 1, j + 1);
        double zD = altitudeAt(i + 1, j) - altitudeAt(i + 1, j + 1);

        return new Vector3D(0.5 * s * (zC - zA), 0.5 * s * (zD - zB), s * s);
    }

    /**
     * Retourne l'altitude du point situé aux coordonnées passées en argument
     * dans le fichier HGT.
     * 
     * @param i
     *            la coordonnée corrrespondant à la longitude du point
     * @param j
     *            la coordonnée correspondant à la latitude du point
     * @return l'altitude du point
     */
    private short altitudeAt(int i, int j) {
        return buffer.get(pointsPerLine * (pointsPerLine - j - 1) + i);
    }
}
