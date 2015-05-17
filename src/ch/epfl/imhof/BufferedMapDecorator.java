package ch.epfl.imhof;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
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
        canvas.setColor(Color.BLACK);
        // float lineWidth = (float) resolution / 254f;
        float lineWidth = (float) 0.05d * width / 100;
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
        for (int x = squareSizePixel; x < width; x += squareSizePixel) {
            canvas.draw(new Line2D.Double(x, 0, x, height - 1));
            canvas.drawString("" + (Math.toDegrees(BL.longitude() + squareSizeRadian)), x, 100);
        }

        for (int y = squareSizePixel; y < height; y += squareSizePixel) {
            canvas.draw(new Line2D.Double(0, y, width - 1, y));
            canvas.drawString("" + Math.toDegrees(BL.latitude() + squareSizeRadian), 0, y);
        }
    }
}
