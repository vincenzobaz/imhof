package ch.epfl.imhof.painting;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.OpenPolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.projection.CH1903Projection;

public final class Java2DCanvasTest {
    private Java2DCanvas newCanvas(double x1, double y1, double x2, double y2,
            int width, Color color) {
        return new Java2DCanvas(new Point(x1, y1), new Point(x2, y2), 1280,
                width, 2000, color);
    }

    private LineStyle newStyle() {
        return new LineStyle(4f, Color.BLACK, LineStyle.LineCap.BUTT,
                LineStyle.LineJoin.ROUND, new float[0]);
    }

    private PolyLine simpleLine() {
        return new OpenPolyLine(Arrays.asList(new Point(-3d, 2d), new Point(4d,
                3d)));
    }

    private Polygon square() {
        return new Polygon(new ClosedPolyLine(Arrays.asList(
                new Point(0.0, -3.0), new Point(3.0, 0.0), new Point(0.0, 3.0),
                new Point(-3.0, 0.0))));
    }

    @Test
    public void correctlyFillsBackground() {
        try {
            ImageIO.write(newCanvas(-5d, -5d, 5d, 5d, 1280, Color.RED).image(),
                    "png", new File("background.png"));
        } catch (IOException e) {
            System.out.println("Exception!");
        }
    }

    @Test
    public void correctlyDrawsLine() {
        Java2DCanvas canvas = newCanvas(-5d, -5d, 5d, 5d, 1280, Color.WHITE);
        canvas.drawPolyLine(simpleLine(), newStyle());
        try {
            ImageIO.write(canvas.image(), "png", new File("droite.png"));
        } catch (IOException e) {
            System.out.println("Exception!");
        }
    }

    @Test
    public void correctlyDrawsArea() {
        Java2DCanvas canvas = newCanvas(-5d, -5d, 5d, 5d, 1280, Color.WHITE);
        canvas.drawPolygon(square(), Color.RED);
        try {
            ImageIO.write(canvas.image(), "png", new File("carre.png"));
        } catch (IOException e) {
            System.out.println("Exception!");
        }
    }

    @Test
    public void correctlyDrawsRolex() {
        OSMMap rolex = null;
        try {
            rolex = OSMMapReader.readOSMFile("data/lc.osm", false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(
                new CH1903Projection());
        Map rolexMap = transformer.transform(rolex);
        Polygon rolexPlan = rolexMap.polygons().get(0).value();

        Java2DCanvas canvas = newCanvas(533100, 152150, 533350, 152350, 720,
                Color.WHITE);
        canvas.drawPolygon(rolexPlan, Color.gray(0.7));
        canvas.drawPolyLine(rolexPlan.shell(), newStyle());
        try {
            ImageIO.write(canvas.image(), "png", new File("rolex.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
