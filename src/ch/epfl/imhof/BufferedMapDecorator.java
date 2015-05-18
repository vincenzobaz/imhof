package ch.epfl.imhof;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class BufferedMapDecorator extends BufferedMap {
    static int howManySquares = 5;

    public BufferedMapDecorator(BufferedImage map) {
        super(map);
    }

    public void addGrid(PointGeo BL, PointGeo TR, int resolution) {
        int width = this.image().getWidth();
        int height = this.image().getHeight();

        Graphics2D canvas = this.image().createGraphics();
        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        canvas.setColor(Color.GRAY);
        // float lineWidth = (float) resolution / 254f;
        float lineWidth = 0.05f * width / 100f;
        BasicStroke line = new BasicStroke(lineWidth);
        canvas.setStroke(line);

        double radiansPerPixel = (TR.longitude() - BL.longitude())
                / ((double) width);
        System.out.println(radiansPerPixel);

        int squareSizePixel = (int) Math.round(width
                / ((double) howManySquares));
        System.out.println(squareSizePixel);
        double squareSizeRadian = radiansPerPixel * squareSizePixel;
        System.out.println(squareSizeRadian);

        canvas.setFont(new Font("inconsolata", Font.PLAIN, 30));
        int indexForStrings = 0;
        for (int x = squareSizePixel; x < width; x += squareSizePixel) {
            canvas.draw(new Line2D.Double(x, 0, x, height - 1));
            canvas.setColor(Color.BLACK);
            double longitude = Math.toDegrees(BL.longitude() + indexForStrings
                    * squareSizeRadian);
            double minute = (longitude % 1) * 60;
            double second = (longitude - (int) minute) * 60;
            canvas.drawString("" + (int) longitude + "°" + (int) minute + "'"
                    + (int) second, x, 50);
            canvas.setColor(Color.GRAY);
            indexForStrings++;
        }

        indexForStrings = 0;
        for (int y = squareSizePixel; y < height; y += squareSizePixel) {
            canvas.draw(new Line2D.Double(0, y, width - 1, y));
            canvas.setColor(Color.BLACK);
            double latitude = Math.toDegrees(BL.latitude() + indexForStrings
                    * squareSizeRadian);
            double minute = (latitude % 1) * 60;
            double second = (latitude - (int) minute) * 60;
            canvas.drawString("" + (int) latitude + "°" + (int) minute + "'"
                    + (int) second, 0, y);
            canvas.setColor(Color.GRAY);
            indexForStrings++;
        }
    }
}
