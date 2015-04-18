package ch.epfl.imhof.painting;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.awt.image.BufferedImage;
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
import ch.epfl.imhof.projection.EquirectangularProjection;

public final class Java2DCanvasTest {
    private Java2DCanvas newCanvas(Color color) {
        return new Java2DCanvas(new Point(533100, 152160), new Point(533400,
                152240), 1280, 720, 800, color);
    }

    private LineStyle newStyle() {
        return new LineStyle(4f, Color.BLACK, LineStyle.LineCap.BUTT,
                LineStyle.LineJoin.ROUND, new float[] {});
    }

    private PolyLine simpleLine() {
        return new OpenPolyLine(Arrays.asList(new Point(-1.0, -1.0), new Point(
                2.0, 2.0)));
    }

    private Polygon square() {
        return new Polygon(new ClosedPolyLine(Arrays.asList(
                new Point(0.0, -2.0), new Point(2.0, 0.0), new Point(0.0, 2.0),
                new Point(-2.0, 0.0))));
    }

    @Test
    public void correctlyFillsBackground() {
        try {
            ImageIO.write(newCanvas(Color.RED).image(), "png", new File(
                    "background.png"));
        } catch (IOException e) {
            System.out.println("Exception!");
        }
    }

    @Test
    public void correctlyDrawsLine() {
        Java2DCanvas canvas = newCanvas(Color.WHITE);
        canvas.drawPolyLine(simpleLine(), newStyle());
        try {
            ImageIO.write(canvas.image(), "png", new File("droite.png"));
        } catch (IOException e) {
            System.out.println("Exception!");
        }
    }

    @Test
    public void correctlyDrawsArea() {
        Java2DCanvas canvas = newCanvas(Color.WHITE);
        canvas.drawPolygon(square(), newStyle(), Color.RED);
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

        Java2DCanvas canvas = newCanvas(Color.WHITE);
        canvas.drawPolygon(rolexPlan, newStyle(), Color.gray(0.7));
        try {
            ImageIO.write(canvas.image(), "png", new File("rolex.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
