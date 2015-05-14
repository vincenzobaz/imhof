package ch.epfl.imhof;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.xml.sax.SAXException;

import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

public final class Main {
    public static void main(String[] args) throws IOException, SAXException {

        PointGeo topRight = new PointGeo(Math.toRadians(Double
                .parseDouble(args[4])), Math.toRadians(Double
                .parseDouble(args[5])));
        PointGeo bottomLeft = new PointGeo(Math.toRadians(Double
                .parseDouble(args[2])), Math.toRadians(Double
                .parseDouble(args[3])));
        int pixelPerMeterResolution = (int) Math.round(Integer
                .parseInt(args[6]) * (5000d / 127d));
        int height = (int) Math.round(pixelPerMeterResolution
                * (1 / 25000d)
                * Math.toRadians(Double.parseDouble(args[5])
                        - Double.parseDouble(args[3])) * Earth.RADIUS);

        Projection ch1903 = new CH1903Projection();
        Point projectedTopRight = ch1903.project(topRight);
        Point projectedBottomLeft = ch1903.project(bottomLeft);

        int width = (int) Math
                .round((projectedTopRight.x() - projectedBottomLeft.x())
                        / (projectedTopRight.y() - projectedBottomLeft.y())
                        * height);

        OSMMap osmMap = OSMMapReader.readOSMFile(args[0], true);
        OSMToGeoTransformer osmToGeoTransformer = new OSMToGeoTransformer(
                ch1903);
        Map map = osmToGeoTransformer.transform(osmMap);
        Java2DCanvas canvas = new Java2DCanvas(projectedBottomLeft,
                projectedTopRight, width, height, Integer.parseInt(args[6]),
                Color.WHITE);

        SwissPainter.painter().drawMap(map, canvas);

        HGTDigitalElevationModel dem = new HGTDigitalElevationModel(new File(
                args[1]));

        ReliefShader reliefShader = new ReliefShader(ch1903, dem, new Vector3D(
                -1, 1, 1));

        BufferedImage relief = reliefShader.shadedRelief(projectedBottomLeft,
                projectedTopRight, width, height,
                0.0017f * pixelPerMeterResolution);

        BufferedImage finalImage = combine(relief, canvas.image());

        ImageIO.write(finalImage, "png", new File(args[7]));
    }

    private static BufferedImage combine(BufferedImage shadedRelief,
            BufferedImage plainMap) {
        int width = plainMap.getWidth();
        int height = plainMap.getHeight();
        BufferedImage result = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result.setRGB(x, y, (Color.rgb(shadedRelief.getRGB(x, y))
                        .multiplyWith(Color.rgb(plainMap.getRGB(x, y))))
                        .convert().getRGB());
            }
        }
        return result;
    }
}
