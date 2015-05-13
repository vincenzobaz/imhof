package ch.epfl.imhof;

import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

public final class MainTest {

    @Test
    public void interlakenTest() throws IOException, SAXException {
        String[] arguments = { "data/interlaken.osm.gz", "data/N46E007.hgt",
                "7.8122", "46.6645", "7.9049", "46.7061", "300",
                "data/linterlaken_Main.png" };
        Main.main(arguments);
    }
}
