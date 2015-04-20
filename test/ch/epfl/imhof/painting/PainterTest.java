package ch.epfl.imhof.painting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import javax.imageio.ImageIO;

import org.junit.Test;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.Polygon;

public class PainterTest {
    private Map testMap() {
        Polygon bottom = new Polygon(new ClosedPolyLine(
                Arrays.asList(new Point[] { new Point(1d, 1.5),
                        new Point(5d, 1.5), new Point(5d, 5.5),
                        new Point(1d, 5.5) })));
        Polygon middle = new Polygon(new ClosedPolyLine(
                Arrays.asList(new Point[] { new Point(-3.5, 3.5),
                        new Point(3.5, 3.5), new Point(3.5, -3.5),
                        new Point(-3.5, -3.5) })));
        Polygon top = new Polygon(new ClosedPolyLine(Arrays.asList(new Point[] {
                new Point(-2.5, 2.5), new Point(2.5, 2.5), new Point(2.5, 6.5),
                new Point(-2.5, 6.5) })));

        Attributes.Builder bottomBuilder = new Attributes.Builder();
        bottomBuilder.put("layer", "-1");
        bottomBuilder.put("natural", "water");
        Attributed<Polygon> bottomAttributed = new Attributed<>(bottom,
                bottomBuilder.build());

        Attributes.Builder middleBuilder = new Attributes.Builder();
        middleBuilder.put("layer", "2");
        middleBuilder.put("natural", "forest");
        Attributed<Polygon> middleAttributed = new Attributed<Polygon>(middle,
                middleBuilder.build());

        Attributes.Builder topBuilder = new Attributes.Builder();
        middleBuilder.put("layer", "4");
        middleBuilder.put("building", "true");
        Attributed<Polygon> topAttributed = new Attributed<Polygon>(top,
                topBuilder.build());

        List<Attributed<Polygon>> polygons = new ArrayList<>();
        polygons.add(bottomAttributed);
        polygons.add(middleAttributed);
        polygons.add(topAttributed);

        return new Map(Collections.emptyList(), polygons);
    }

    @Test
    public void correctlyStacksLayers() {
        Predicate<Attributed<?>> isLake = Filters.tagged("natural", "water");
        Painter<?> lakesPainter = Painter.polygon(Color.BLUE).when(isLake);

        Predicate<Attributed<?>> isBuilding = Filters.tagged("building");
        Painter<?> buildingsPainter = Painter.polygon(Color.RED).when(
                isBuilding);

        Predicate<Attributed<?>> isForest = Filters.tagged("natural", "forest");
        Painter<?> forestPainter = Painter.polygon(Color.GREEN).when(isForest);

        Painter<?> mapPainter = lakesPainter.above(buildingsPainter);
        Java2DCanvas canvas = new Java2DCanvas(new Point(-7d, -7d), new Point(
                7d, 7d), 1280, 1280, 72, Color.WHITE);
        mapPainter.drawMap(testMap(), canvas);
        try {
            ImageIO.write(canvas.image(), "png", new File("layerTest.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
