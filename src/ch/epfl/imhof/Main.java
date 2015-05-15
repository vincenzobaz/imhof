package ch.epfl.imhof;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.xml.sax.SAXException;

import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

/**
 * Classe contenant la méthode principale du projet
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class Main {
    /**
     * Méthode principale, elle utilise toutes les autres classes pour produire
     * une carte à partir des paramètres fournis
     * 
     * @param args
     *            tableau de chaînes de charactères. On ne vérifie pas de
     *            validation des arguments, mais il devrait contenir:
     *            <ul>
     *            <li>args[0]: le nom (chemin) d'un fichier OSM compressé avec
     *            gzip contenant les données de la carte à dessiner,
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
     *            </ul>
     * 
     * @throws IOException
     *             si l'un des fichiers n'est pas accessible par les programme
     * @throws SAXException
     *             s'il y des erreurs de parsing du fichiers osm
     */
    public static void main(String[] args) throws IOException, SAXException {

        // On construit les deux points de type WGS 84
        PointGeo topRight = new PointGeo(Math.toRadians(Double
                .parseDouble(args[4])), Math.toRadians(Double
                .parseDouble(args[5])));
        PointGeo bottomLeft = new PointGeo(Math.toRadians(Double
                .parseDouble(args[2])), Math.toRadians(Double
                .parseDouble(args[3])));

        // On calcule la resolution de l'image en pixel par mètres
        int pixelPerMeterResolution = (int) Math.round(Integer
                .parseInt(args[6]) * (5000d / 127d));

        // On calcule hauteur selon laformule fournie
        int height = (int) Math.round(pixelPerMeterResolution
                * (1 / 25000d)
                * Math.toRadians(Double.parseDouble(args[5])
                        - Double.parseDouble(args[3])) * Earth.RADIUS);

        // En ayant besoin de cette projection à plusieurs moments, on la stocke
        // dans une variable en évitant d'en instanciant une autre à chaque fois
        Projection ch1903 = new CH1903Projection();

        // On projète nos points du système WGS84 dans un repère cartésien
        Point projectedTopRight = ch1903.project(topRight);
        Point projectedBottomLeft = ch1903.project(bottomLeft);

        // On calcule la largeur selon la formule fournie
        int width = (int) Math
                .round((projectedTopRight.x() - projectedBottomLeft.x())
                        / (projectedTopRight.y() - projectedBottomLeft.y())
                        * height);

        // Lecture du fichier OSM et conséquente création d'un objet OSMMap qui
        // est ensuite converti en Map et dessiné sur une toile
        OSMMap osmMap = OSMMapReader.readOSMFile(args[0], true);
        OSMToGeoTransformer osmToGeoTransformer = new OSMToGeoTransformer(
                ch1903);
        Map map = osmToGeoTransformer.transform(osmMap);
        Java2DCanvas canvas = new Java2DCanvas(projectedBottomLeft,
                projectedTopRight, width, height, Integer.parseInt(args[6]),
                Color.WHITE);
        SwissPainter.painter().drawMap(map, canvas);

        // Création d'un modèle de relief
        HGTDigitalElevationModel dem = new HGTDigitalElevationModel(new File(
                args[1]));

        // Création d'un "dessinnateur de reliefs"
        ReliefShader reliefShader = new ReliefShader(ch1903, dem, new Vector3D(
                -1, 1, 1));

        // Le relief flouté est dessiné
        BufferedImage relief = reliefShader.shadedRelief(projectedBottomLeft,
                projectedTopRight, width, height,
                0.0017f * pixelPerMeterResolution);
        
        ImageIO.write(relief, "png", new File("leMondeEstJoli.png"));

        // Finalement on compose l'image du relief et celle de la carte
        BufferedImage finalImage = combine(relief, canvas.image());
        
        BufferedMapDecorator.howManySquares = 7;
        BufferedMapDecorator imageToDecorate = new BufferedMapDecorator(finalImage);

        imageToDecorate.addGrid(bottomLeft, topRight, Integer.parseInt(args[6]));

        // On sauvegarde l'image ainsi obtenue sur disque.
        ImageIO.write(imageToDecorate.image(), "png", new File(args[7]));
    }

    /**
     * Retourne une image obtenue en composant deux images par multiplication
     * des couleurs de chaque pixel
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
}
