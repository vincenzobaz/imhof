package ch.epfl.imhof.dem;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3D;

public class HGTDigitalElevationModelTest {
    // @Test
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
        File file = new File("data/N46E007.hgt");
        HGTDigitalElevationModel rhoneValley = new HGTDigitalElevationModel(
                file);
        BufferedImage image = new BufferedImage(800, 800,
                BufferedImage.TYPE_INT_RGB);
        final double POINT_DISTANCE = Math.toRadians(0.6) / 800d;
        for (int j = 0; j < 800; ++j) {
            for (int i = 0; i < 800; ++i) {
                Vector3D normal = rhoneValley.normalAt(new PointGeo(Math
                        .toRadians(7.2) + i * POINT_DISTANCE, Math
                        .toRadians(46.8) - j * POINT_DISTANCE));
                double greyLevel = (normal.normalized().y() + 1) / 2d;
                int newInt = (int) (greyLevel * 255.9999);
                int grey = newInt & (newInt << 8)
                        & (newInt << 16);
                image.setRGB(i, j, grey);
            }
        }
        ImageIO.write(image, "png", new File("vallee_du_rhone.png"));
    }
}
