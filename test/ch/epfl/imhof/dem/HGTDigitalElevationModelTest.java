package ch.epfl.imhof.dem;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import ch.epfl.imhof.PointGeo;

public class HGTDigitalElevationModelTest {
    @Test
    public void correctlyConstructsNewInstance() {
        try {
            HGTDigitalElevationModel dem = new HGTDigitalElevationModel(
                    new File("data/N46E007.hgt"));
        } catch (IllegalArgumentException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void correctlyDrawsRelief() throws IllegalArgumentException,
            IOException {
        HGTDigitalElevationModel rhoneValley = new HGTDigitalElevationModel(
                new File("data/N46E007.hgt"));
        BufferedImage image = new BufferedImage(800, 800,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D context = image.createGraphics();
        context.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        context.setColor(java.awt.Color.WHITE);
        context.fillRect(0, 0, 800, 800);
        final double POINT_DISTANCE = Math.toRadians(0.6) / 800d;
        for (int i = 0; i <= 800; ++i) {
            for (int j = 0; i <= 800; ++j) {
                /*int greyLevel = (int) (0.5 * (rhoneValley
                        .normalAt(new PointGeo(Math.toRadians(7.2) + i
                                * POINT_DISTANCE, Math.toRadians(46.8) - j
                                * POINT_DISTANCE)).y()) * 255.99);*/
                rhoneValley.normalAt(new PointGeo(Math.toRadians(7.2), Math.toRadians(46.2)));
                //int grey = greyLevel & greyLevel << 8 & greyLevel << 16;
                //image.setRGB(i, j, grey);
            }
        }
        ImageIO.write(image, "png", new File("vallee_du_rhone.png"));
    }
}
