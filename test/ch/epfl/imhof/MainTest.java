package ch.epfl.imhof;

import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

public final class MainTest {

    // @Test
    public void interlakenTest() throws IOException, SAXException {
        String[] arguments = { "data/interlaken.osm.gz", "data/N46E007.hgt",
                "7.8122", "46.6645", "7.9049", "46.7061", "300",
                "interlaken_Main.png" };
        Main.main(arguments);
    }

    // @Test
    public void lausanneTest() throws IOException, SAXException {
        String[] arguments = { "data/lausanne.osm.gz", "data/N46E006.hgt",
                "6.5594", "46.5032", "6.6508", "46.5459", "300",
                "lausanne_Main.png" };
        Main.main(arguments);
    }

    // @Test
    public void berneTest() throws IOException, SAXException {
        String[] arguments = { "data/berne.osm.gz", "data/N46E007.hgt",
                "7.3912", "46.9322", "7.4841", "46.9742", "300",
                "berne_Main.png" };
        Main.main(arguments);
    }

    @Test
    public void saintclaudeTest() throws IOException, SAXException {
        String[] arguments = { "data/saintclaude.osm.gz", "data/N46E005.hgt",
                "5.8136", "46.3662", "5.9209", "46.4097", "300",
                "saintclaude_Main.png" };
        Main.main(arguments);
    }
}
