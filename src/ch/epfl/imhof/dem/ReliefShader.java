package ch.epfl.imhof.dem;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
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
            int height, float radius) {
        BufferedImage rawRelief = raw(width, height,
                Point.alignedCoordinateChange(new Point(0d, height), BL,
                        new Point(width, 0d), TR));
        return rawRelief;
        //return radius == 0d ? rawRelief : blurringImage(rawRelief,
                //shadingkernel(radius));
    }

    private BufferedImage raw(int width, int height,
            Function<Point, Point> imageToPlan) {
        BufferedImage rawRelief = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < height; ++x) {
            for (int y = 0; y < width; ++y) {
                Vector3D normal = model.normalAt(projection.inverse(imageToPlan
                        .apply(new Point(x, y))));
                double cosTheta = lightSource.scalarProduct(normal)
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

    private Kernel shadingKernel(float radius) {
        float sigma = radius / 3f;
        int n = 2 * ((int) Math.ceil(radius)) + 1;

        float[] line = new float[n];
        int indexBase = (n - 1) / 2;
        float totalWeight = 0f;
        for (int i = 0; i < indexBase; i++) {
            float weight = (float) Math.exp(-i * i / (2 * sigma * sigma));
            line[indexBase + i] = line[indexBase - i] = weight;
            totalWeight += weight;
        }
        for (int i = 0; i < line.length; ++i) {
            line[i] /= totalWeight;
        }

        float[] data = new float[n * n];

        return new Kernel(n, n, data);
    }

    private BufferedImage blurringImage(BufferedImage image, Kernel kernel) {
        int edgeCondition = 0;
        ConvolveOp convolveop = new ConvolveOp(kernel, edgeCondition,
                RenderingHints.VALUE_ANTIALIAS_ON);

        return convolveop.filter(image, null);
    }
}
