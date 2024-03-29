package ch.epfl.imhof;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import ch.epfl.imhof.dem.DigitalElevationModel;
import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.MultiHGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.EquirectangularProjection;
import ch.epfl.imhof.projection.LambertConformalConicProjection;
import ch.epfl.imhof.projection.MercatorProjection;
import ch.epfl.imhof.projection.Projection;
import ch.epfl.imhof.projection.StereographicProjection;

/**
 * Classe contenant la méthode principale du projet.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class Main {
    // Vecteur représentant la source lumineuse du relief ombré
    private static final Vector3D LIGHT_SOURCE = new Vector3D(-1d, 1d, 1d);

    /**
     * Méthode principale, elle utilise toutes les autres classes pour produire
     * une carte à partir des paramètres fournis.
     * 
     * @param args
     *            tableau de chaînes de charactères; on n'effectue pas de
     *            validation des arguments, mais il devrait contenir:
     *            <ul>
     *            <li>args[0]: le nom (chemin) d'un fichier OSM compressé avec
     *            gzip contenant les données de la carte à dessiner, utiliser
     *            download si on souhaite le télécharger
     *            <li>args[1]: le nom (chemin) d'un fichier HGT couvrant la
     *            totalité de la zone de la carte à dessiner, zone tampon
     *            incluse,
     *            <li>args[2]: la longitude du point bas-gauche de la carte, en
     *            degrés,
     *            <li>args[3]: la latitude du point bas-gauche de la carte, en
     *            degrés,
     *            <li>args[4]: la longitude du point haut-droite de la carte, en
     *            degrés,
     *            <li>args[5]: la latitude du point haut-droite de la carte, en
     *            degrés,
     *            <li>args[6]: la résolution de l'image à dessiner, en points
     *            par pouce (dpi),
     *            <li>args[7]: le nom (chemin) du fichier PNG à générer.
     *            <li>args[8]: la projection à utiliser:
     *            <ul>
     *            <li>Stereographic
     *            <li>LambertConformalConic
     *            <li>Equirectangular
     *            <li>CH1903
     *            <li>Mercator
     *            </ul>
     *            Si aucun n'est fourni, alors CH1903 est utilisée.
     *            <li>Deuxième fichier hgt couvrant la zone au nord ou à l'est
     *            du premier (args[1])
     *            </ul>
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // On construit les deux points de type WGS 84 correspondant aux coins
        // bas-gauche et haut-droite de la zone à dessiner
        PointGeo topRight = new PointGeo(Math.toRadians(Double
                .parseDouble(args[4])), Math.toRadians(Double
                .parseDouble(args[5])));
        PointGeo bottomLeft = new PointGeo(Math.toRadians(Double
                .parseDouble(args[2])), Math.toRadians(Double
                .parseDouble(args[3])));

        // Calcul de la résolution de l'image en pixel par mètres
        int dpi = Integer.parseInt(args[6]);
        int pixelPerMeterResolution = (int) Math.round(dpi * (5000d / 127d));

        // Calcul de la hauteur de l'image
        int height = (int) Math.round(pixelPerMeterResolution
                * (topRight.latitude() - bottomLeft.latitude()) * Earth.RADIUS
                / 25000d);

        // Projection utilisée pour projeter les points dans le repère cartésien
        Projection projection = null;

        // Choix de la projection à utiliser
        if (args.length > 8) {
            switch (args[8]) {
            case "CH1903":
                projection = new CH1903Projection();
                break;
            case "LambertConformalConic":
                projection = new LambertConformalConicProjection(20, 50);
                break;
            case "Mercator":
                projection = new MercatorProjection();
                break;
            case "Stereographic":
                projection = new StereographicProjection();
                break;
            case "Equirectangular":
                projection = new EquirectangularProjection();
                break;
            default:
                throw new IllegalArgumentException(
                        "Nom de projection invalide.");
            }
        } else {
            projection = new CH1903Projection();
        }

        // Projection des points du système WGS84 dans un repère cartésien
        Point projectedTopRight = projection.project(topRight);
        Point projectedBottomLeft = projection.project(bottomLeft);

        // Calcul de la largeur de l'image
        int width = (int) Math
                .round((projectedTopRight.x() - projectedBottomLeft.x())
                        / (projectedTopRight.y() - projectedBottomLeft.y())
                        * height);

        // Lecture du fichier OSM et création d'un objet OSMMap qui
        // est ensuite converti en map et dessiné sur une toile
        OSMMap osmMap;
        if (args[0].equals("download")) {
            osmMap = OSMMapReader.readOSMFile(
                    download(args[2], args[3], args[4], args[5]), false);
        } else {
            osmMap = OSMMapReader.readOSMFile(args[0], true);
        }
        OSMToGeoTransformer osmToGeoTransformer = new OSMToGeoTransformer(
                projection);
        Map map = osmToGeoTransformer.transform(osmMap);
        Java2DCanvas canvas = new Java2DCanvas(projectedBottomLeft,
                projectedTopRight, width, height, dpi, Color.WHITE);
        SwissPainter.painter().drawMap(map, canvas);

        // Création d'un modèle de relief
        DigitalElevationModel dem = null;
        switch (args.length) {
        case 8:
        case 9:
            dem = new HGTDigitalElevationModel(new File(args[1]));
            break;
        case 10:
            dem = new MultiHGTDigitalElevationModel(
                    new HGTDigitalElevationModel(new File(args[1])),
                    new HGTDigitalElevationModel(new File(args[9])));
            break;
        }

        // Création d'un "dessinateur de reliefs" ayant une source lumineuse au
        // nord-ouest
        ReliefShader reliefShader = new ReliefShader(projection, dem,
                LIGHT_SOURCE);

        // Dessin du relief flouté
        BufferedImage relief = reliefShader.shadedRelief(projectedBottomLeft,
                projectedTopRight, width, height,
                0.0017f * pixelPerMeterResolution);

        // HGTDigitalElevation model implemente AutoCloseable, mais comme on ne
        // l'utilise pas dans un bloc try-catch, on doit le fermer manuellement
        dem.close();

        // Composition de l'image du relief et de celle de la carte
        BufferedImage finalImage = combine(relief, canvas.image());

        BufferedMapDecorator imageToDecorate = new BufferedMapDecorator(
                finalImage, dpi, args[7]);
        imageToDecorate.addGrid(bottomLeft, topRight, dpi, 7);
        imageToDecorate.addLegend();

        // Sauvegarde de l'image obtenue sur disque
        imageToDecorate.printOnFile("png", args[7]);
    }

    /**
     * Retourne une image obtenue par composition des deux images fournies en
     * multipliant les couleurs de chacun de leurs pixels entre elles.
     * 
     * @param shadedRelief
     *            l'image du relief
     * @param plainMap
     *            l'image de la carte
     * @return la carte finale
     */
    private static BufferedImage combine(BufferedImage shadedRelief,
            BufferedImage plainMap) {
        int width = plainMap.getWidth();
        int height = plainMap.getHeight();
        BufferedImage result = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result.setRGB(x, y, (Color.rgb(shadedRelief.getRGB(x, y))
                        .multiplyWith(Color.rgb(plainMap.getRGB(x, y))))
                        .convert().getRGB());
            }
        }
        return result;
    }

    /**
     * Télécharge un fichier osm à travers la Overpass API de OpenStreetMap et
     * retourne le nom du fichier.
     */
    private static String download(String longitudeWest, String latitudeSud,
            String longitudeEast, String latitudeNorth) throws IOException {
        URL url = new URL(
                "http://overpass.osm.rambler.ru/cgi/xapi_meta?*[bbox="
                        + longitudeWest + "," + latitudeSud + ","
                        + longitudeEast + "," + latitudeNorth + "]");
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        String fileName = longitudeWest + "," + latitudeSud + ","
                + longitudeEast + "," + latitudeNorth + ".osm";
        FileOutputStream osmFile = new FileOutputStream(fileName);
        osmFile.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        rbc.close();
        osmFile.close();
        return fileName;
    }
}
