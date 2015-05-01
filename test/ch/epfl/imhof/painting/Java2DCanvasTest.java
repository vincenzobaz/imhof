package ch.epfl.imhof.painting;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import ch.epfl.imhof.Map;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.SwissPainter;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.OpenPolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

public final class Java2DCanvasTest {
    private final Projection projection = new CH1903Projection();
    private final OSMToGeoTransformer transformer = new OSMToGeoTransformer(
            projection);

    private Java2DCanvas newCanvas(double x1, double y1, double x2, double y2,
            int width, Color color) {
        return new Java2DCanvas(new Point(x1, y1), new Point(x2, y2), 1280,
                width, 2000, color);
    }

    private LineStyle newStyle() {
        return new LineStyle(4f, Color.RED, LineStyle.LineCap.BUTT,
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
        Java2DCanvas canvas = newCanvas(-5d, -5d, 5d, 5d, 1280, Color.BLUE);
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
    public void correctlyDrawsRolex() throws IOException {
        OSMMap rolex = null;
        try {
            rolex = OSMMapReader.readOSMFile("data/lc.osm", false);
        } catch (IOException | SAXException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Map rolexMap = transformer.transform(rolex);
        Polygon rolexPlan = rolexMap.polygons().get(0).value();

        Java2DCanvas canvas = newCanvas(533100, 152150, 533350, 152350, 720,
                Color.WHITE);
        canvas.drawPolygon(rolexPlan, Color.gray(0.7));
        canvas.drawPolyLine(rolexPlan.shell(), newStyle());

        ImageIO.write(canvas.image(), "png", new File("rolex.png"));
    }

    public void correctlyDrawsInterlaken() throws IOException {
        OSMMap osmMap = null;
        try {
            osmMap = OSMMapReader.readOSMFile("data/interlaken.osm.gz", true);
        } catch (IOException | SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map map = transformer.transform(osmMap);

        Point bl = new Point(628590, 168210);
        Point tr = new Point(635660, 172870);
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, 800, 530, 72,
                Color.WHITE);

        SwissPainter.painter().drawMap(map, canvas);
        ImageIO.write(canvas.image(), "png", new File("interlaken_swiss.png"));
    }

    public void correctlyDrawsSaintClaude() throws IOException {
        OSMMap osmMap = null;
        try {
            osmMap = OSMMapReader.readOSMFile("data/saintclaude.osm", false);
        } catch (IOException | SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map map = transformer.transform(osmMap);

        Point bl = projection.project(new PointGeo(Math.toRadians(5.8136), Math
                .toRadians(46.3662)));
        Point tr = projection.project(new PointGeo(Math.toRadians(5.9209), Math
                .toRadians(46.4097)));
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, 800 * 2, 530 * 2, 150,
                Color.WHITE);

        SwissPainter.painter().drawMap(map, canvas);
        ImageIO.write(canvas.image(), "png", new File("saintclaude_swiss.png"));
    }

    public void correctlyDrawsBesancon() throws IOException {
        OSMMap osmMap = null;
        try {
            osmMap = OSMMapReader.readOSMFile("data/besancon.osm", false);
        } catch (IOException | SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map map = transformer.transform(osmMap);

        Point bl = projection.project(new PointGeo(Math.toRadians(5.9647), Math
                .toRadians(47.2152)));
        Point tr = projection.project(new PointGeo(Math.toRadians(6.0720), Math
                .toRadians(47.2580)));
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, 800 * 2, 530 * 2, 150,
                Color.WHITE);

        SwissPainter.painter().drawMap(map, canvas);
        ImageIO.write(canvas.image(), "png", new File("besancon_swiss.png"));
    }

    @Test
    public void correctlyDrawsBerne() throws IOException {
        OSMMap osmMap = null;
        try {
            osmMap = OSMMapReader.readOSMFile("data/berne.osm.gz", true);
        } catch (IOException | SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map map = transformer.transform(osmMap);

        Point bl = new Point(597475, 197590);
        Point tr = new Point(605705, 203363);
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, 800 * 2, 530 * 2, 150,
                Color.WHITE);

        SwissPainter.painter().drawMap(map, canvas);
        ImageIO.write(canvas.image(), "png", new File("berne_swiss.png"));
    }

    public void correctlyDrawsLausanne() throws IOException {
        OSMMap osmMap = null;
        try {
            osmMap = OSMMapReader.readOSMFile("data/lausanne.osm.gz", true);
        } catch (IOException | SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map map = transformer.transform(osmMap);

        Point bl = new Point(532510, 150590);
        Point tr = new Point(539570, 155260);
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, 800 * 2, 530 * 2, 150,
                Color.WHITE);

        SwissPainter.painter().drawMap(map, canvas);
        ImageIO.write(canvas.image(), "png", new File("lausanne_swiss.png"));
    }
}
