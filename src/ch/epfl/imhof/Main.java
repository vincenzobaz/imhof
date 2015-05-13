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
        int resolutionPixelPerMeter = (int) Math.round(Integer
                .parseInt(args[6]) * (5000d / 127d));
        int height = (int) Math.round(resolutionPixelPerMeter
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
        BufferedImage paintedMap = canvas.image();
        ImageIO.write(paintedMap, "png", new File("painted_map.png"));

        HGTDigitalElevationModel dem = new HGTDigitalElevationModel(new File(
                args[1]));

        ReliefShader reliefShader = new ReliefShader(ch1903, dem, new Vector3D(
                -1, 1, 1));

        BufferedImage reliefs = reliefShader.shadedRelief(projectedBottomLeft,
                projectedTopRight, width, height, 0.0017f * resolutionPixelPerMeter);
        ImageIO.write(reliefs, "png", new File("relief.png"));

        // BufferedImage finalImage = combine(reliefs, paintedMap);

        // ImageIO.write(finalImage, "png", new File(args[7]));
    }

    private static BufferedImage combine(BufferedImage shaderRelief,
            BufferedImage flatMap) {
        BufferedImage result = new BufferedImage(flatMap.getWidth(),
                flatMap.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < result.getWidth(); x++) {
            for (int y = 0; y < result.getHeight(); y++) {
                result.setRGB(x, y, (Color.rgb(shaderRelief.getRGB(x, y))
                        .multiplyWith(Color.rgb(flatMap.getRGB(x, y))))
                        .convert().getRGB());
            }
        }
        return result;
    }
}