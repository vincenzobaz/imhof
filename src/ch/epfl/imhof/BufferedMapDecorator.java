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
        int imageWidth = this.image().getWidth();
        int imageHeight = this.image().getHeight();
        int frameSize = imageWidth / 20;
        int decoratedMapWidth = imageWidth + 2 * frameSize;
        int decoratedMapHeight = imageHeight + 2 * frameSize;
        BufferedImage decoratedMap = new BufferedImage(decoratedMapWidth,
                decoratedMapHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D canvas = decoratedMap.createGraphics();
        canvas.setColor(Color.WHITE);
        canvas.fillRect(0, 0, decoratedMapWidth, decoratedMapHeight);
        canvas.drawImage(this.image(), frameSize, frameSize, this.image()
                .getWidth(), this.image().getHeight(), null);
        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        canvas.setStroke(new BasicStroke(0.05f * imageWidth / 100f));

        double radiansPerPixel = (TR.longitude() - BL.longitude())
                / ((double) imageWidth);

        int squareSizePixel = (int) Math.round(imageWidth
                / ((double) howManySquares));

        double squareSizeRadian = radiansPerPixel * squareSizePixel;

        canvas.setFont(new Font("inconsolata", Font.PLAIN, 30));
        int indexForStrings = 0;
        for (int x = frameSize + squareSizePixel; x < decoratedMapWidth
                - frameSize; x += squareSizePixel) {
            canvas.setColor(Color.GRAY);
            canvas.draw(new Line2D.Double(x, frameSize, x, decoratedMapHeight
                    - frameSize));
            canvas.setColor(Color.BLACK);
            double longitude = Math.toDegrees(BL.longitude() + indexForStrings
                    * squareSizeRadian);
            double minute = (longitude % 1) * 60;
            double second = (minute % 1) * 60;
            canvas.drawString("" + (int) longitude + "°" + (int) minute + "'"
                    + (int) second + "\"", x, frameSize);
            canvas.setColor(Color.GRAY);
            indexForStrings++;
        }

        indexForStrings = 0;
        for (int y = frameSize + squareSizePixel; y < decoratedMapHeight
                - frameSize; y += squareSizePixel) {
            canvas.setColor(Color.GRAY);
            canvas.draw(new Line2D.Double(frameSize, y, decoratedMapHeight
                    - frameSize, y));
            canvas.setColor(Color.BLACK);
            double latitude = Math.toDegrees(BL.latitude() + indexForStrings
                    * squareSizeRadian);
            double minute = (latitude % 1) * 60;
            double second = (minute % 1) * 60;
            canvas.drawString("" + (int) latitude + "°" + (int) minute + "'"
                    + (int) second + "\"", 0, y);
            canvas.setColor(Color.GRAY);
            indexForStrings++;
        }
        setImage(decoratedMap);
    }
}
