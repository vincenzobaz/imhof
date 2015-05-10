package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
import java.util.function.Function;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3D;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.Projection;

public final class ReliefShader {
    private final Projection projection;
    private final DigitalElevationModel model;
    private final Vector3D lightSource;

    public ReliefShader(Projection projection, DigitalElevationModel model,
            Vector3D lightSource) {
        this.projection = projection;
        this.model = model;
        this.lightSource = lightSource;
    }

    public BufferedImage shadedRelief(Point BL, Point TR, int width,
            int height, double radius) {
        BufferedImage rawRelief = raw(width, height,
                Point.alignedCoordinateChange(new Point(0d, height), BL,
                        new Point(width, 0d), TR));
        return radius == 0d ? rawRelief : blurringImage(rawRelief,
                shadingkernel(radius));
    }

    private BufferedImage raw(int width, int height,
            Function<Point, Point> imageToPlan) {
        BufferedImage rawRelief = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        double cosTheta;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Vector3D normal = model.normalAt(projection.inverse(imageToPlan
                        .apply(new Point(x, y))));
                cosTheta = lightSource.scalarProduct(normal)
                        / (lightSource.norm() * normal.norm());
                int redAndGreenLevel = (int) (0.5 * (cosTheta + 1) * 255);
                int blueLevel = (int) (0.5 * (0.7 * cosTheta + 1) * 255);
                int rgb = blueLevel | redAndGreenLevel << 8
                        | redAndGreenLevel << 16;
                rawRelief.setRGB(x, y, rgb);
            }
        }
        return rawRelief;
    }

    private Kernel shadingKernel(double radius) {
        double sigma = radius / 3d;
        int n = 2 * ((int) Math.ceil(radius)) + 1;
        float[] semiHVector = new float[(n + 1) / 2];
        float sum = 1f;
        semiHVector[0] = 1f;
        for (int i = 1; i < semiHVector.length; ++i) {
            semiHVector[i] = (float) Math.exp(- (i * i) / (2 * sigma * sigma));
            sum += semiHVector[i] * 2f;
        }
        for (int i = 0; i < semiHVector.length; ++i) {
            semiHVector[i] /= sum;
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
            }
        }
        
        Kernel kernel = new Kernel(n, n, );
    }

    private BufferedImage blurringImage(BufferedImage image, double[][] kernel) {

    }
}
