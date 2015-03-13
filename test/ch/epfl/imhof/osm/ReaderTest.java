package ch.epfl.imhof.osm;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReaderTest {
    @Test
    public void readOSMFileWorks() {
        try {
            String bc = getClass().getResource("/bc.osm").getFile();
            OSMMap bcMap = OSMMapReader.readOSMFile(bc, false);
            String rolex = getClass().getResource("/lc.osm").getFile();
            OSMMap rolexMap = OSMMapReader.readOSMFile(rolex, false);
        } catch (Exception e) {
            System.out.println("Exception catchée!!!");
            System.out.println(e.getMessage());
        }
    }
}
