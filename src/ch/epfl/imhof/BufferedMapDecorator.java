package ch.epfl.imhof;

import java.awt.BasicStroke;    
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class BufferedMapDecorator extends BufferedMap {
    static int howManySquares = 5;
    
    public BufferedMapDecorator(BufferedImage map){
        super(map);
    }
    
    public void addGrid(PointGeo BL, PointGeo TR, int resolution, int sizeSquarePercent){
        int width = this.image().getWidth();
        int height = this.image().getHeight();

        Graphics2D canvas = this.image().createGraphics();
        canvas.setColor(Color.BLACK);
        float lineWidth = 0;
        BasicStroke line = new BasicStroke(lineWidth);
        canvas.setStroke(line);
        double squareSizePixel = width / ((double) howManySquares);
        
        
        
        // canvas.draw(newPath(line))
        
    }
}
