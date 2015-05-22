package ch.epfl.imhof;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedMapDecorator {
    private final int frameSize;
    BufferedImage map;
    Graphics2D graphicContext;

    public BufferedMapDecorator(BufferedImage map) {
        frameSize = map.getWidth() / 20;
        this.map = new BufferedImage(map.getWidth() + 2 * frameSize,
                map.getHeight() + 2 * frameSize, BufferedImage.TYPE_INT_RGB);
        graphicContext = this.map.createGraphics();
        graphicContext.setBackground(Color.WHITE);
        graphicContext.fillRect(0, 0, this.map.getWidth(), this.map.getWidth());
        graphicContext.drawImage(map, frameSize, frameSize, map.getWidth(),
                map.getHeight(), null);
    }

    public BufferedMapDecorator(BufferedImage map, int frameSize,
            Color frameColor) {
        this.map = map;
        this.frameSize = frameSize;
        this.map = new BufferedImage(map.getWidth() + 2 * frameSize,
                map.getHeight() + 2 * frameSize, BufferedImage.TYPE_INT_RGB);
        graphicContext = this.map.createGraphics();
        graphicContext.setBackground(frameColor);
        graphicContext.fillRect(0, 0, this.map.getWidth(), this.map.getWidth());
        graphicContext.drawImage(map, frameSize, frameSize, map.getWidth(),
                map.getHeight(), null);
    }

    public void addGrid(PointGeo BL, PointGeo TR, int resolution,
            int howManyCells) {
        int decoratedMapWidth = map.getWidth();
        int decoratedMapHeight = map.getHeight();
        int imageWidth = decoratedMapWidth - 2 * frameSize;

        graphicContext.setColor(Color.WHITE);
        graphicContext.setStroke(new BasicStroke(0.05f * imageWidth / 100f));

        double radiansPerPixel = (TR.longitude() - BL.longitude())
                / ((double) imageWidth);

        int squareSizePixel = (int) Math.round(imageWidth
                / ((double) howManyCells));

        double squareSizeRadian = radiansPerPixel * squareSizePixel;

        graphicContext.setFont(new Font("inconsolata", Font.PLAIN, 30));
        int indexForStrings = 0;
        for (int x = frameSize + squareSizePixel; x < decoratedMapWidth
                - frameSize; x += squareSizePixel) {
            graphicContext.setColor(Color.GRAY);
            graphicContext.draw(new Line2D.Double(x, frameSize, x,
                    decoratedMapHeight - frameSize));
            graphicContext.setColor(Color.BLACK);
            double longitude = Math.toDegrees(BL.longitude() + indexForStrings
                    * squareSizeRadian);
            double minute = (longitude % 1) * 60;
            double second = (minute % 1) * 60;
            graphicContext.drawString("" + (int) longitude + "°" + (int) minute
                    + "'" + (int) second + "\"", x, frameSize);
            graphicContext.setColor(Color.GRAY);
            indexForStrings++;
        }

        indexForStrings = 0;
        for (int y = frameSize + squareSizePixel; y < decoratedMapHeight
                - frameSize; y += squareSizePixel) {
            graphicContext.setColor(Color.GRAY);
            graphicContext.draw(new Line2D.Double(frameSize, y,
                    decoratedMapHeight - frameSize, y));
            graphicContext.setColor(Color.BLACK);
            double latitude = Math.toDegrees(BL.latitude() + indexForStrings
                    * squareSizeRadian);
            double minute = (latitude % 1) * 60;
            double second = (minute % 1) * 60;
            graphicContext.drawString("" + (int) latitude + "°" + (int) minute
                    + "'" + (int) second + "\"", 0, y);
            graphicContext.setColor(Color.GRAY);
            indexForStrings++;
        }
    }

    public void printOnFile(String extension, String path) throws IOException {
        ImageIO.write(map, extension, new File(path));
    }

    public void addLegend() {
        int w = map.getWidth();
        int h = map.getHeight();
        graphicContext.setColor(Color.WHITE);
        graphicContext.fillRect(w * 13 / 16, 2 * frameSize, w / 8, h * 2 / 5);
        graphicContext.setColor(Color.BLACK);
        graphicContext.setFont(new Font("inconsolata", Font.PLAIN, 30));
        graphicContext.drawString("Légende", w * 13 / 16 + w / 80, 2
                * frameSize + h / 50);
        drawLegendItem(new Color(0.2f, 0.2f, 0.2f), "bâtiments", w, h, 1);
        drawLegendItem(new Color(0.75f, 0.85f, 0.7f), "forêts", w, h, 2);
        drawLegendItem(new Color(0.85f, 0.9f, 0.85f), "parcs", w, h, 3);
        drawLegendItem(new Color(0.45f, 0.7f, 0.8f), "cours d'eau", w, h, 4);
        drawLegendItem(new Color(0.8f, 0.9f, 0.95f),
                "plan d'eau, rivière, canal", w, h, 5);
    }

    private void drawLegendItem(Color color, String s, int w, int h, int n) {
        graphicContext.setColor(color);
        graphicContext.fillRect(w * 13 / 16 + w / 80, 2 * frameSize + h * n
                / 30, w * 13 / 640, h / 50);
        graphicContext.setColor(Color.BLACK);
        graphicContext.drawString(s, w * 13 / 16 + w / 80 + w * 20 / 640, 2
                * frameSize + h * (10 * n + 5) / 300);
    }
}
