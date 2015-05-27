package ch.epfl.imhof;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Classe fournissant des méthodes permettant d'ajouter des éléments à une
 * carte, notamment une légende et un quadrillage.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class BufferedMapDecorator {
    private final BufferedImage map;
    private final Graphics2D graphicContext;
    private final float scale;
    private final int frameSize;
    private final Font font;

    /**
     * Construit un décorateur de carte, avec les paramètres fournis.
     * 
     * @param map
     *            la carte à décorer
     * @param dpi
     *            la résolution de la carte à décorer
     * @param name
     *            le titre de la carte
     * @param frameSize
     *            la taille du cadre
     * @param frameColor
     *            la couleur du cadre
     * @throws IOException
     *             lève une exception en cas d'erreur d'entrée-sortie
     */
    public BufferedMapDecorator(BufferedImage map, int dpi, String name,
            int frameSize, Color frameColor) throws IOException {
        this.map = new BufferedImage(map.getWidth() + 2 * frameSize,
                map.getHeight() + 2 * frameSize, BufferedImage.TYPE_INT_RGB);
        scale = dpi / 72f;
        this.frameSize = frameSize;
        graphicContext = this.map.createGraphics();
        graphicContext.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        font = new Font("inconsolata", Font.PLAIN, (int) (7.5 * scale));

        int w = this.map.getWidth();
        int h = this.map.getHeight();

        // Dessin du cadre
        graphicContext.setBackground(frameColor);
        graphicContext.fillRect(0, 0, w, h);
        graphicContext.drawImage(map, frameSize, frameSize, map.getWidth(),
                map.getHeight(), null);

        // Dessin du logo EPFL
        BufferedImage epflLogo = ImageIO.read(new File("epflLogo.jpg"));
        graphicContext.drawImage(epflLogo, 0, map.getHeight() + frameSize,
                map.getWidth() / 11,
                map.getHeight() + frameSize + map.getWidth() * 417 / 8921, 0,
                0, epflLogo.getWidth() - 1, epflLogo.getHeight() - 1, null);

        // Dessin du titre
        String s = name.substring(0, 1).toUpperCase()
                + name.substring(1, name.indexOf('.'));
        graphicContext.setColor(Color.BLACK);
        graphicContext.setFont(new Font("inconsolata", Font.BOLD,
                (int) (scale * 12)));
        graphicContext.drawString(s, frameSize, frameSize / 2);

        // Dessin de l'échelle
        float dpm = Math.round(dpi * (5000d / 127d)) / 25000f;
        graphicContext.setStroke(new BasicStroke(h / 300f, 0, 0, 10f,
                new float[] { dpm * 250f, dpm * 250f }, 0f));
        Path2D scale = new Path2D.Float();
        double scaleHeight = h - 1.5 * frameSize;
        double baseScaleX = w * 4 / 5;
        scale.moveTo(baseScaleX, scaleHeight);
        scale.lineTo(baseScaleX + 750 * dpm, scaleHeight);
        graphicContext.draw(scale);
        scale.reset();
        scale.moveTo(baseScaleX + 250f * dpm, scaleHeight);
        scale.lineTo(baseScaleX + 1000f * dpm, scaleHeight);
        graphicContext.setColor(Color.WHITE);
        graphicContext.draw(scale);

        // Dessin des étiquettes de l'échelle
        graphicContext.setFont(font);
        graphicContext.setColor(Color.BLACK);
        int scaleTextHeight = h - 13 * frameSize / 10;
        graphicContext.drawString("0", (int) baseScaleX, scaleTextHeight);
        graphicContext.drawString("250m", (int) (baseScaleX + 250 * dpm),
                scaleTextHeight);
        graphicContext.drawString("500m", (int) (baseScaleX + 500 * dpm),
                scaleTextHeight);
        graphicContext.drawString("750m", (int) (baseScaleX + 750 * dpm),
                scaleTextHeight);
        graphicContext.drawString("1 km", (int) (baseScaleX + 1000 * dpm),
                scaleTextHeight);
    }

    /**
     * Construit un décorateur de carte, avec les paramètres fournis, et un
     * cadre ayant une taille par défaut de 1/20 de la largeur de la carte, et
     * une couleur blanche.
     * 
     * @param map
     *            la carte à décorer
     * @param dpi
     *            la résolution de la carte à décorer
     * @param name
     *            le titre de la carte
     * @throws IOException
     *             lève une exception en cas d'erreur d'entrée-sortie
     */
    public BufferedMapDecorator(BufferedImage map, int dpi, String name)
            throws IOException {
        this(map, dpi, name, map.getWidth() / 20, Color.WHITE);
    }

    /**
     * Ajoute un quadrillage à la carte.
     * 
     * @param bottomLeft
     *            le coin bas-gauche de la carte
     * @param topRight
     *            le coin haut-droite de la carte
     * @param resolution
     *            la résolution de la carte
     * @param howManyCells
     *            le nombre de divisions horizontales du quadrillage
     */
    public void addGrid(PointGeo bottomLeft, PointGeo topRight, int resolution,
            int howManyCells) {
        int decoratedMapWidth = map.getWidth();
        int decoratedMapHeight = map.getHeight();
        int imageWidth = decoratedMapWidth - 2 * frameSize;

        double radiansPerPixel = (topRight.longitude() - bottomLeft.longitude())
                / imageWidth;

        int squareSizePixel = (int) Math.round(imageWidth
                / ((double) howManyCells));

        double squareSizeRadian = radiansPerPixel * squareSizePixel;

        graphicContext.setStroke(new BasicStroke(0.0005f * imageWidth));
        graphicContext.setFont(font);

        int indexForStrings = 0;
        for (int x = frameSize + squareSizePixel; x < decoratedMapWidth
                - frameSize; x += squareSizePixel) {
            graphicContext.setColor(Color.GRAY);
            graphicContext.draw(new Line2D.Double(x, frameSize, x,
                    decoratedMapHeight - frameSize));
            graphicContext.setColor(Color.BLACK);
            double longitude = Math.toDegrees(bottomLeft.longitude()
                    + (indexForStrings + 1) * squareSizeRadian);
            double minute = (longitude % 1) * 60;
            double second = (minute % 1) * 60;
            graphicContext.drawString("" + (int) longitude + "°" + (int) minute
                    + "'" + (int) second + "\"", x, frameSize);
            indexForStrings++;
        }

        indexForStrings = 0;
        for (int y = frameSize + squareSizePixel; y < decoratedMapHeight
                - frameSize; y += squareSizePixel) {
            graphicContext.setColor(Color.GRAY);
            graphicContext.draw(new Line2D.Double(frameSize, y,
                    decoratedMapWidth - frameSize, y));
            graphicContext.setColor(Color.BLACK);
            double latitude = Math.toDegrees(topRight.latitude()
                    - (indexForStrings + 1) * squareSizeRadian);
            double minute = (latitude % 1) * 60;
            double second = (minute % 1) * 60;
            graphicContext.drawString("" + (int) latitude + "°" + (int) minute
                    + "'" + (int) second + "\"", 0, y);
            indexForStrings++;
        }
    }

    /**
     * Ajoute une légende à la carte.
     */
    public void addLegend() {
        int w = map.getWidth();
        int h = map.getHeight();

        // Dessin du cadre et du titre de la légende
        graphicContext.setColor(Color.WHITE);
        graphicContext.fillRect(w * 13 / 16, frameSize * 3 / 2, w / 8,
                h * 2 / 5);
        graphicContext.setColor(Color.BLACK);
        graphicContext.setFont(new Font("inconsolata", Font.BOLD,
                (int) (7.5 * scale)));
        graphicContext.drawString("Légende", w * 13 / 16 + w / 80, frameSize
                * 3 / 2 + h / 50);
        graphicContext.setFont(font);

        // Dessin des éléments de la légende
        drawLegend(new Color(0.2f, 0.2f, 0.2f), "bâtiments", w, h, 1, 1);
        drawLegend(new Color(0.75f, 0.85f, 0.7f), "forêts", w, h, 2, 1);
        drawLegend(new Color(0.85f, 0.9f, 0.85f), "parcs", w, h, 3, 1);
        drawLegend(new Color(0.8f, 0.9f, 0.95f), "plan d'eau, rivière", w, h,
                4, 1);
        drawLegend(new Color(0.45f, 0.7f, 0.8f), "cours d'eau", w, h, 5, 4);
        drawLegend(new Color(0.7f, 0.15f, 0.15f), "chemin de fer", w, h, 6, 4);
        drawLegend(new Color(1f, 0.75f, 0.2f), "autoroute", w, h, 7, 4);
        drawLegend(new Color(0.95f, 0.7f, 0.6f), "route nationale", w, h, 8, 4);
        drawLegend(new Color(1f, 1f, 0.5f), "route secondaire", w, h, 9, 4);

        // Dessin de deux lignes pour repésenter les routes tertiaires blanches
        graphicContext.drawLine(w * 33 / 40, frameSize * 3 / 2 + h / 3,
                w * 541 / 640, frameSize * 3 / 2 + h / 3);
        graphicContext.drawLine(w * 33 / 40, frameSize * 3 / 2 + h * 203 / 600,
                w * 541 / 640, frameSize * 3 / 2 + h * 203 / 600);
        graphicContext.drawString("route tertiaire", w * 137 / 160, frameSize
                * 3 / 2 + h * 101 / 300);

        // Dessin de la ligne pointillée représentant les chemins
        graphicContext.setStroke(new BasicStroke(h / 400f, 0, 0, 10f,
                new float[] { h / 200f, h / 200f }, 0f));
        Path2D path = new Path2D.Float();
        path.moveTo((int) w * 33 / 40, frameSize * 3 / 2 + h * 11 / 30);
        path.lineTo((int) w * 541 / 640, frameSize * 3 / 2 + h * 11 / 30);
        graphicContext.setColor(Color.BLACK);
        graphicContext.draw(path);
        graphicContext.drawString("chemin", w * 137 / 160, frameSize * 3 / 2
                + h * 111 / 300);
    }

    /**
     * Sauvegarde la carte sous forme de fichier ayant l'extension et le nom
     * fournis.
     * 
     * @param extension
     *            l'extension du fichier
     * @param fileName
     *            le nom complet du fichier (avec extension)
     * @throws IOException
     *             lève une exception en cas d'erreur d'entrée-sortie
     */
    public void printOnFile(String extension, String fileName)
            throws IOException {
        ImageIO.write(map, extension, new File(fileName));
    }

    /**
     * Dessine l'élément de légende et sa description suivant les paramètres
     * donnés.
     */
    private void drawLegend(Color color, String s, int w, int h, int n,
            int roadRatio) {
        graphicContext.setColor(color);
        graphicContext.fillRect(w * 33 / 40, frameSize * 3 / 2 + h * n / 30,
                w * 13 / 640, h / (50 * roadRatio));
        graphicContext.setColor(Color.BLACK);
        graphicContext.drawString(s, w * 137 / 160, frameSize * 3 / 2 + h
                * (10 * n + 5 - roadRatio) / 300);
    }
}
