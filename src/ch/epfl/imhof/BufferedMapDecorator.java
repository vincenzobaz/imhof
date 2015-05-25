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

public class BufferedMapDecorator {
    private final BufferedImage map;
    private final Graphics2D graphicContext;
    private final float scale;
    private final int frameSize;
    private Font font;

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
        scale.moveTo(w * 4 / 5, h - 1.5 * frameSize);
        scale.lineTo(w * 4 / 5 + 1000 * dpm, h - 1.5 * frameSize);
        graphicContext.draw(scale);
        scale.reset();
        scale.moveTo(w * 4 / 5 + 250f * dpm, h - 1.5 * frameSize);
        scale.lineTo(w * 4 / 5 + 1000f * dpm, h - 1.5 * frameSize);
        graphicContext.setColor(Color.WHITE);
        graphicContext.draw(scale);
        graphicContext.setFont(font);
        graphicContext.setColor(Color.BLACK);
        graphicContext.drawString("0", w * 4 / 5, h - 13 * frameSize / 10);
        graphicContext.drawString("250m", w * 4 / 5 + 250 * dpm, h - 13
                * frameSize / 10);
        graphicContext.drawString("500m", w * 4 / 5 + 500 * dpm, h - 13
                * frameSize / 10);
        graphicContext.drawString("750m", w * 4 / 5 + 750 * dpm, h - 13
                * frameSize / 10);
        graphicContext.drawString("1km", w * 4 / 5 + 1000 * dpm, h - 13
                * frameSize / 10);
    }

    public BufferedMapDecorator(BufferedImage map, int dpi, String name)
            throws IOException {
        this(map, dpi, name, map.getWidth() / 20, Color.WHITE);
    }

    public void addGrid(PointGeo bottomLeft, PointGeo topRight, int resolution,
            int howManyCells) {
        int decoratedMapWidth = map.getWidth();
        int decoratedMapHeight = map.getHeight();
        int imageWidth = decoratedMapWidth - 2 * frameSize;

        graphicContext.setColor(Color.WHITE);
        graphicContext.setStroke(new BasicStroke(0.05f * imageWidth / 100f));

        double radiansPerPixel = (topRight.longitude() - bottomLeft.longitude())
                / imageWidth;

        int squareSizePixel = (int) Math.round(imageWidth
                / ((double) howManyCells));

        double squareSizeRadian = radiansPerPixel * squareSizePixel;

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

    public void printOnFile(String extension, String path) throws IOException {
        ImageIO.write(map, extension, new File(path));
    }

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
        path.moveTo(w * 33 / 40, frameSize * 3 / 2 + h * 11 / 30);
        path.lineTo(w * 541 / 640, frameSize * 3 / 2 + h * 11 / 30);
        graphicContext.setColor(Color.BLACK);
        graphicContext.draw(path);
        graphicContext.drawString("chemin", w * 137 / 160, frameSize * 3 / 2
                + h * 111 / 300);
    }

    // Méthode dessinant l'élément de légende et sa description suivant les
    // paramètres donnés
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
