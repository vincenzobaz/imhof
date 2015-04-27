package ch.epfl.imhof.painting;

import static ch.epfl.imhof.painting.Filters.tagged;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.RoadPainterGenerator.RoadSpec;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;
import ch.epfl.imhof.SwissPainter;

public final class RoadPainterGeneratorTest {
    private final Projection projection = new CH1903Projection();
    private final OSMToGeoTransformer transformer = new OSMToGeoTransformer(
            projection);
    private final Java2DCanvas canvas = new Java2DCanvas(new Point(628764,
            167585), new Point(634991, 172331), 800, 530, 72, Color.WHITE);

    private Map newMap() {
        OSMMap osmMap = null;
        try {
            osmMap = OSMMapReader.readOSMFile("data/interlaken.osm", false);
        } catch (IOException | SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map map = transformer.transform(osmMap);
        return map;
    }

    @Test
    public void correctlyDrawsMapForOneRoadSpecification() throws IOException {
        Painter<?> roadPainter = RoadPainterGenerator
                .painterForRoads(new RoadSpec(tagged("highway", "primary"),
                        1.7f, Color.rgb(0.95, 0.7, 0.6), 0.35f, Color.BLACK));
        roadPainter.drawMap(newMap(), canvas);
        ImageIO.write(canvas.image(), "png",
                new File("interlakenRoadsOnly.png"));
    }

    @Test
    public void correctlyDrawsInterlakenWithRoads() throws IOException {
        Painter<?> swissStands = SwissPainter.painter();

        // Dessin de la carte et stockage dans un fichier
        swissStands.drawMap(newMap(), canvas);
        ImageIO.write(canvas.image(), "png",
                new File("interlakenWithRoads.png"));
    }
}
