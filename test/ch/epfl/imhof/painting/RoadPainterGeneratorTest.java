package ch.epfl.imhof.painting;

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
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;
import ch.epfl.imhof.SwissPainter;

public final class RoadPainterGeneratorTest {
    private final Projection projection = new CH1903Projection();
    private final OSMToGeoTransformer transformer = new OSMToGeoTransformer(
            projection);

    @Test
    public void correctlyDrawsInterlakenWithRoads() throws IOException {
        Painter<?> swissStands = SwissPainter.painter();
        OSMMap osmMap = null;
        try {
            osmMap = OSMMapReader.readOSMFile("data/interlaken.osm", false);
        } catch (IOException | SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map map = transformer.transform(osmMap);
        Point bl = new Point(628764, 167585);
        Point tr = new Point(634991, 172331);
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, 800, 530, 72,
                Color.WHITE);

        // Dessin de la carte et stockage dans un fichier
        swissStands.drawMap(map, canvas);
        ImageIO.write(canvas.image(), "png", new File("interlakenWithRoads.png"));
    }

}
