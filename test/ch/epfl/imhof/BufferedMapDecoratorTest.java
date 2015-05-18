package ch.epfl.imhof;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.xml.sax.SAXException;

public class BufferedMapDecoratorTest {
    @Test
    public void testDecoratedImage() throws IOException {
        File image = new File("interlaken_Main.png");
        BufferedImage read = ImageIO.read(image);
        BufferedMapDecorator.howManySquares = 7;
        BufferedMapDecorator toDecorate = new BufferedMapDecorator(read);
        toDecorate.addGrid(
                new PointGeo(Math.toRadians(7.8122), Math.toRadians(46.6645)),
                new PointGeo(Math.toRadians(7.9049), Math.toRadians(46.7061)),
                300);
        toDecorate.printOnFile("png", "testGrid.png");
    }

}
