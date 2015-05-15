package ch.epfl.imhof;

import java.awt.BasicStroke;    
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class BufferedMapDecorator extends BufferedMap {
    static int howManySquares = 5;
    
    public BufferedMapDecorator(BufferedImage map){
        super(map);
    }
    
    public void addGrid(PointGeo BL, PointGeo TR, int resolution){
        int width = this.image().getWidth();
        int height = this.image().getHeight();

        Graphics2D canvas = this.image().createGraphics();
        canvas.setColor(Color.BLACK);
        float lineWidth = (float) resolution / 254f;
        BasicStroke line = new BasicStroke(lineWidth);
        canvas.setStroke(line);
        double radiansPerPixel =  (TR.longitude() - BL.longitude()) / ((double) width);

        int squareSizePixel = (int) Math.round(width /  ((double) howManySquares));
        double squareSizeRadian = radiansPerPixel * squareSizePixel;
        double longFirstVerticalLine = (Math.abs(BL.longitude()) % squareSizeRadian) + BL.longitude();
        int x = (int) Math.round(longFirstVerticalLine / radiansPerPixel);
        
        while (x <= TR.longitude()){
            canvas.draw(new Line2D.Double(x, 0, x, height - 1));
            x += squareSizePixel;
        }
        
        
        // canvas.draw(newPath(line))
        
    }
}
