package ch.epfl.imhof;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.epfl.imhof.dem.DigitalElevationModel;
import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.MultiDigitalElevationModel;
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
 * Classe contenant la méthode principale du projet.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class MainWithGUI {
    // Projection utilisée pour projeter les points dans le repère cartésien
    private static final Projection CH1903 = new CH1903Projection();

    // Vecteur représentant la source lumineuse du relief ombré
    private static final Vector3D LIGHT_SOURCE = new Vector3D(-1d, 1d, 1d);

    private static final int TEXT_WIDTH = 200;
    private static final int NUMBERS_WIDTH = 40;
    public static void main(String[] args) throws Exception{
        createUI();
    }

    private static void createUI() {
        JFrame w = new JFrame("Map creator");

        String[] arguments = new String[8];

        // Panneau osm
        JLabel osmFilePlease = new JLabel("Fichier osm.gz");
        JTextField osmPath = new JTextField();
        osmPath.setPreferredSize(new Dimension(TEXT_WIDTH, osmPath
                .getPreferredSize().height));
        osmPath.setEditable(false);
        JButton chooseOsm = new JButton("Choisir...");
        chooseOsm.addActionListener(e -> {
            JFileChooser c = new JFileChooser();
            if (c.showDialog(w, "Choisir") == JFileChooser.APPROVE_OPTION) {
                osmPath.setText(c.getSelectedFile().getPath());
                arguments[0] = c.getSelectedFile().getPath();
            }
        });
        JPanel osmPanel = new JPanel(new FlowLayout());
        osmPanel.add(osmFilePlease);
        osmPanel.add(osmPath);
        osmPanel.add(chooseOsm);

        // Panneau hgt
        JLabel hgtFilePlease = new JLabel("Fichier hgt");
        JTextField hgtPath = new JTextField();
        hgtPath.setEditable(false);
        hgtPath.setPreferredSize(new Dimension(TEXT_WIDTH, hgtPath
                .getPreferredSize().height));
        JButton chooseHgt = new JButton("Choisir");
        chooseHgt.addActionListener(e -> {
            JFileChooser c = new JFileChooser();
            if (c.showDialog(w, "Choisir") == JFileChooser.APPROVE_OPTION) {
                hgtPath.setText(c.getSelectedFile().getPath());
                arguments[1] = c.getSelectedFile().getPath();
            }
        });
        JPanel hgtPanel = new JPanel(new FlowLayout());
        hgtPanel.add(hgtFilePlease);
        hgtPanel.add(hgtPath);
        hgtPanel.add(chooseHgt);

        // Lat et long Panel
        JLabel latitudeBottomLeft = new JLabel("Latitude du point bas gauche: ");
        JTextField latBL = new JTextField();
        latBL.setEditable(true);
        latBL.setPreferredSize(new Dimension(NUMBERS_WIDTH, latBL
                .getPreferredSize().height));
        JPanel latBlP = new JPanel(new FlowLayout());
        latBlP.add(latitudeBottomLeft);
        latBlP.add(latBL);

        JLabel longitudeBottomLeft = new JLabel(
                "Longitude du point bas gauche: ");
        JTextField lonBL = new JTextField();
        lonBL.setEditable(true);
        lonBL.setPreferredSize(new Dimension(NUMBERS_WIDTH, latBL
                .getPreferredSize().height));
        JPanel lonBlP = new JPanel(new FlowLayout());
        lonBlP.add(longitudeBottomLeft);
        lonBlP.add(lonBL);

        JLabel latitudeTopRight = new JLabel("Latitude du point haut droite: ");
        JTextField latTR = new JTextField();
        latTR.setEditable(true);
        latTR.setPreferredSize(new Dimension(NUMBERS_WIDTH, latBL
                .getPreferredSize().height));
        JPanel latTrP = new JPanel(new FlowLayout());
        latTrP.add(latitudeTopRight);
        latTrP.add(latTR);

        JLabel longitudeTopRight = new JLabel(
                "Longitude du point haut droite: ");
        JTextField lonTR = new JTextField();
        lonTR.setEditable(true);
        lonTR.setPreferredSize(new Dimension(NUMBERS_WIDTH, latBL
                .getPreferredSize().height));
        JPanel lonTrP = new JPanel(new FlowLayout());
        lonTrP.add(longitudeTopRight);
        lonTrP.add(lonTR);

        JPanel LatLonPanel = new JPanel();
        LatLonPanel.setLayout(new BoxLayout(LatLonPanel, BoxLayout.Y_AXIS));
        LatLonPanel.add(latBlP);
        LatLonPanel.add(lonBlP);
        LatLonPanel.add(latTrP);
        LatLonPanel.add(lonTrP);

        // panneau résolution
        JLabel resolution = new JLabel("Resolution de l'image (en dpi): ");
        JTextField res = new JTextField();
        res.setEditable(true);
        res.setPreferredSize(new Dimension(NUMBERS_WIDTH, latBL
                .getPreferredSize().height));
        JPanel resolutionP = new JPanel(new FlowLayout());
        resolutionP.add(resolution);
        resolutionP.add(res);

        // panneau image finale
        JLabel chooseDestLabel = new JLabel("FIchier de l'image :");
        JTextField destPath = new JTextField();
        destPath.setEditable(false);
        destPath.setPreferredSize(new Dimension(TEXT_WIDTH, hgtPath
                .getPreferredSize().height));
        JButton chooseDest = new JButton("Choisir");
        chooseDest.addActionListener(e -> {
            JFileChooser c = new JFileChooser("Choisir");
            if (c.showDialog(w, "Choisir") == JFileChooser.APPROVE_OPTION) {
                destPath.setText(c.getSelectedFile().getPath());
                arguments[7] = c.getSelectedFile().getPath();
            }
        });
        JPanel destP = new JPanel(new FlowLayout());
        destP.add(chooseDestLabel);
        destP.add(destPath);
        destP.add(chooseDest);
        
        // panneau final
        JButton dessin = new JButton("Dessine la carte! ");
        dessin.addActionListener(e->{
            arguments[2] = lonBL.getText();
            arguments[3] = latBL.getText();
            arguments[4] = lonTR.getText();
            arguments[5] = lonTR.getText();
            arguments[6] = res.getText();
            try {
                draw(arguments);
            } catch (Exception f){
                System.out.println(f.getMessage());
            }
        });
        JPanel drawP = new JPanel(new FlowLayout());
        drawP.add(dessin);


        // panneau principal
        JPanel mainP = new JPanel();
        mainP.setLayout(new BoxLayout(mainP, BoxLayout.Y_AXIS));
        mainP.add(osmPanel);
        mainP.add(hgtPanel);
        mainP.add(LatLonPanel);
        mainP.add(resolutionP);
        mainP.add(destP);
        mainP.add(drawP);

        w.setContentPane(mainP);
        w.pack();
        w.setVisible(true);
    }

    /**
     * Méthode principale, elle utilise toutes les autres classes pour produire
     * une carte à partir des paramètres fournis.
     * 
     * @param args
     *            tableau de chaînes de charactères; on n'effectue pas de
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
     * @throws Exception
     */
    public static void draw(String[] args) throws Exception {

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

        // Projection des points du système WGS84 dans un repère cartésien
        Point projectedTopRight = CH1903.project(topRight);
        Point projectedBottomLeft = CH1903.project(bottomLeft);

        // Calcul de la largeur de l'image
        int width = (int) Math
                .round((projectedTopRight.x() - projectedBottomLeft.x())
                        / (projectedTopRight.y() - projectedBottomLeft.y())
                        * height);

        // Lecture du fichier OSM et création d'un objet OSMMap qui
        // est ensuite converti en map et dessiné sur une toile
        OSMMap osmMap = OSMMapReader.readOSMFile(args[0], true);
        OSMToGeoTransformer osmToGeoTransformer = new OSMToGeoTransformer(
                CH1903);
        Map map = osmToGeoTransformer.transform(osmMap);
        Java2DCanvas canvas = new Java2DCanvas(projectedBottomLeft,
                projectedTopRight, width, height, dpi, Color.WHITE);
        SwissPainter.painter().drawMap(map, canvas);

        // Création d'un modèle de relief
        DigitalElevationModel dem;
        BufferedImage relief = null;

        switch (args.length) {
        case 8:
            dem = new HGTDigitalElevationModel(new File(args[1]));
            relief = relief(dem, projectedBottomLeft, projectedTopRight, width,
                    height, pixelPerMeterResolution);
            dem.close();
            break;
        case 9:
            dem = new MultiDigitalElevationModel(new HGTDigitalElevationModel(
                    new File(args[1])), new HGTDigitalElevationModel(new File(
                    args[8])));
            relief = relief(dem, projectedBottomLeft, projectedTopRight, width,
                    height, pixelPerMeterResolution);
            dem.close();
            break;
        case 11:
            dem = new MultiDigitalElevationModel(new HGTDigitalElevationModel(
                    new File(args[1])), new HGTDigitalElevationModel(new File(
                    args[8])));
            BufferedImage firstPart = relief(dem, projectedBottomLeft,
                    CH1903.project(new PointGeo(topRight.longitude(), Math
                            .toRadians(dem.latitudeSW() + 0.97))), width / 2,
                    height / 2, pixelPerMeterResolution);
            dem.close();

            dem = new MultiDigitalElevationModel(new HGTDigitalElevationModel(
                    new File(args[9])), new HGTDigitalElevationModel(new File(
                    args[10])));
            BufferedImage secondPart = relief(dem, CH1903.project(new PointGeo(
                    bottomLeft.longitude(), Math.toRadians(dem.latitudeSW()))),
                    projectedTopRight, (width % 2 == 0) ? width / 2
                            : width / 2 + 1, (height % 2) == 0 ? height / 2
                            : height / 2 + 1, pixelPerMeterResolution);
            dem.close();

            relief = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D context = relief.createGraphics();
            context.drawImage(firstPart, new AffineTransformOp(
                    new AffineTransform(), 2), 0, height / 2);
            context.drawImage(secondPart, new AffineTransformOp(
                    new AffineTransform(), 2), 0, height);
            break;
        }

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

    private static BufferedImage relief(DigitalElevationModel dem,
            Point bottomLeft, Point topRight, int width, int height,
            int resolution) {
        ReliefShader reliefShader = new ReliefShader(CH1903, dem, LIGHT_SOURCE);
        return reliefShader.shadedRelief(bottomLeft, topRight, width, height,
                0.0017f * resolution);
    }
}
