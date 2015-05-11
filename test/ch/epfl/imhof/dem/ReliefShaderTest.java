package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3D;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

public final class ReliefShaderTest {
    @Test
    public void correctlyDrawsRawRelief() throws IllegalArgumentException,
            IOException {
        Projection projection = new CH1903Projection();
        File file = new File("data/N46E007.hgt");
        HGTDigitalElevationModel simmental = new HGTDigitalElevationModel(file);
        ReliefShader reliefShader = new ReliefShader(projection, simmental,
                new Vector3D(-1, 1, 1));
        BufferedImage rawRelief = reliefShader.shadedRelief(
                projection.project(new PointGeo(Math.toRadians(7.2), Math
                        .toRadians(46.2))), projection.project(new PointGeo(
                        Math.toRadians(7.8), Math.toRadians(46.8))), 800, 800,
                0);
        ImageIO.write(rawRelief, "png", new File("test_raw_relief_1-11.png"));
    }
}
