package ch.epfl.imhof.painting;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;

import javax.imageio.ImageIO;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.PointGeo;
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
    private final CH1903Projection projection = new CH1903Projection();
    private final OSMToGeoTransformer transformer = new OSMToGeoTransformer(
            projection);

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
        try {
            ImageIO.write(canvas.image(), "png", new File("rolex.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void correctlyDrawsLausanne() throws IOException {
        // Le peintre et ses filtres
        Predicate<Attributed<?>> isLake = Filters.tagged("natural", "water");
        Painter<?> lakesPainter = Painter.polygon(Color.BLUE).when(isLake);

        Predicate<Attributed<?>> isBuilding = Filters.tagged("building");
        Painter<?> buildingsPainter = Painter.polygon(Color.BLACK).when(
                isBuilding);

        Predicate<Attributed<?>> isForest = Filters.tagged("natural", "wood");
        Painter<?> forestPainter = Painter.polygon(Color.GREEN).when(isForest);

        Painter<?> painter = buildingsPainter.above(lakesPainter).above(
                forestPainter);
        OSMMap osmMap = null;
        try {
            osmMap = OSMMapReader.readOSMFile("data/lausanne.osm", false);
        } catch (IOException | SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map map = transformer.transform(osmMap);

        // La toile
        Point blsc = projection.project(new PointGeo(Math.toRadians(5.8634),
                Math.toRadians(46.3638)));
        Point trsc = projection.project(new PointGeo(Math.toRadians(5.9009),
                Math.toRadians(46.4058)));
        Point bl = new Point(532510, 150590);
        Point tr = new Point(539570, 155260);
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, 800, 530, 72,
                Color.WHITE);

        // Dessin de la carte et stockage dans un fichier
        painter.drawMap(map, canvas);
        ImageIO.write(canvas.image(), "png", new File("lausanne.png"));
    }
}
