package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3D;

public class HGTDigitalElevationModelTest {
    @Test
    public void correctlyConstructsNewInstance() {
        try {
            HGTDigitalElevationModel hgtDEM = new HGTDigitalElevationModel(
                    new File("j46E007.hgt"));
        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            HGTDigitalElevationModel hgtDEm = new HGTDigitalElevationModel(
                    new File("N4hE007.hgt"));
        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            HGTDigitalElevationModel hgtDEm = new HGTDigitalElevationModel(
                    new File("N46F007.hgt"));
        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            HGTDigitalElevationModel hgtDEm = new HGTDigitalElevationModel(
                    new File("N46E0u7.hgt"));
        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            HGTDigitalElevationModel hgtDEm = new HGTDigitalElevationModel(
                    new File("N46E007.png"));
        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            HGTDigitalElevationModel hgtDEm = new HGTDigitalElevationModel(
                    new File("N046E007.hgt"));
        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void correctlyDrawsSimmental() throws IllegalArgumentException,
            IOException {
        File file = new File("data/N46E007.hgt");
        HGTDigitalElevationModel simmental = new HGTDigitalElevationModel(file);
        BufferedImage image = new BufferedImage(800, 800,
                BufferedImage.TYPE_INT_RGB);
        final double POINT_DISTANCE = Math.toRadians(0.6) / 800d;
        for (int x = 0; x < 800; ++x) {
            for (int y = 0; y < 800; ++y) {
                Vector3D normal = simmental.normalAt(new PointGeo(Math
                        .toRadians(7.2) + x * POINT_DISTANCE, Math
                        .toRadians(46.8) - y * POINT_DISTANCE));
                double greyLevel = (normal.normalized().y() + 1) / 2d;
                int newInt = (int) (greyLevel * 255.9999);
                int grey = newInt | (newInt << 8) | (newInt << 16);
                image.setRGB(x, y, grey);
            }
        }
        ImageIO.write(image, "png", new File("relief_simmental.png"));
    }

    // @Test
    public void correctlyDrawsRelief() throws IllegalArgumentException,
            IOException {
        File file = new File("data/N46E007.hgt");
        HGTDigitalElevationModel interlaken = new HGTDigitalElevationModel(file);
        BufferedImage image = new BufferedImage(3316, 2188,
                BufferedImage.TYPE_INT_RGB);
        final double VERTICAL_POINT_DISTANCE = Math.toRadians(0.0416) / 2188d;
        final double HORIZONTAL_POINT_DISTANCE = Math.toRadians(0.0927) / 3316d;
        for (int j = 0; j < 2188; ++j) {
            for (int i = 0; i < 3316; ++i) {
                Vector3D normal = interlaken.normalAt(new PointGeo(Math
                        .toRadians(7.8122) + i * HORIZONTAL_POINT_DISTANCE,
                        Math.toRadians(46.7061) - j * VERTICAL_POINT_DISTANCE));
                double greyLevel = (normal.normalized().y() + 1) / 2d;
                int newInt = (int) (greyLevel * 255.9999);
                int grey = newInt + (newInt << 8) + (newInt << 16);
                image.setRGB(i, j, grey);
            }
        }
        ImageIO.write(image, "png", new File("relief_interlaken.png"));
    }
}
