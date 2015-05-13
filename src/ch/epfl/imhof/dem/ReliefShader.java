package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

import javax.imageio.ImageIO;

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
        if (radius == 0) {
            return raw(width, height, Point.alignedCoordinateChange(new Point(
                    0d, height - 1), BL, new Point(width - 1, 0d), TR));
        } else {
            float[] gaussValues = shadingKernel(radius);
            int bufferZoneSize = (gaussValues.length - 1) / 2;
            BufferedImage rawImage = raw(width + 2 * bufferZoneSize, height + 2
                    * bufferZoneSize, Point.alignedCoordinateChange(new Point(
                    bufferZoneSize - 1, height + bufferZoneSize - 2), BL,
                    new Point(width + bufferZoneSize - 2, bufferZoneSize - 1),
                    TR));
            BufferedImage blurredImage = blurredImage(rawImage, gaussValues);
            return blurredImage.getSubimage(bufferZoneSize, bufferZoneSize,
                    width, height);
        }
    }

    private BufferedImage raw(int width, int height,
            Function<Point, Point> imageToPlan) {
        BufferedImage rawRelief = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
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

    private float[] shadingKernel(float radius) {
        float sigma = radius / 3f;
        int n = 2 * ((int) Math.ceil(radius)) + 1;
        float[] line = new float[n];
        int indexBase = (n - 1) / 2;
        float totalWeight = line[indexBase] = 1f;
        for (int i = 1; i < indexBase; i++) {
            float weight = (float) Math.exp(-i * i / (2 * sigma * sigma));
            line[indexBase + i] = line[indexBase - i] = weight;
            totalWeight += 2 * weight;
        }
        for (int i = 0; i < line.length; ++i) {
            line[i] /= totalWeight;
        }
        return line;
    }

    private BufferedImage blurredImage(BufferedImage image, float[] kernel) {
        ConvolveOp horizontalConvolution = new ConvolveOp(new Kernel(
                kernel.length, 1, kernel), ConvolveOp.EDGE_NO_OP, null);
        ConvolveOp verticalConvolution = new ConvolveOp(new Kernel(1,
                kernel.length, kernel), ConvolveOp.EDGE_NO_OP, null);
        return verticalConvolution.filter(
                horizontalConvolution.filter(image, null), null);
    }
}
