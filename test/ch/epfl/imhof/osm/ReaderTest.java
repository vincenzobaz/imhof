package ch.epfl.imhof.osm;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReaderTest {
    @Test
    public void readOSMFileWorks() {
        try {
            // String testFile = getClass().getResource("/bc.osm").getFile();
            OSMMap testMap = OSMMapReader.readOSMFile("/data/bc.osm", false);
        } catch (Exception e) {
            System.out.println("Exception catchée!!!");
        }
    }
}
