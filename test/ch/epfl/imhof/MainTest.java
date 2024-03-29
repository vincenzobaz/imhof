package ch.epfl.imhof;

import org.junit.Test;

public final class MainTest {

    // @Test
    public void interlakenTest() throws Exception {
        String[] arguments = { "data/interlaken.osm.gz", "data/N46E007.hgt",
                "7.8122", "46.6645", "7.9049", "46.7061", "300",
                "interlaken_Main.png" };
        Main.main(arguments);
    }

    // @Test
    public void lausanneTest() throws Exception {
        String[] arguments = { "data/lausanne.osm.gz", "data/N46E006.hgt",
                "6.5594", "46.5032", "6.6508", "46.5459", "300",
                "lausanne_Main.png" };
        Main.main(arguments);
    }

    // @Test
    public void berneTest() throws Exception {
        String[] arguments = { "data/berne.osm.gz", "data/N46E007.hgt",
                "7.3912", "46.9322", "7.4841", "46.9742", "300",
                "berne_Main.png" };
        Main.main(arguments);
    }

    // @Test
    public void saintclaudeTest() throws Exception {
        String[] arguments = { "data/saintclaude.osm.gz", "data/N46E005.hgt",
                "5.8136", "46.3662", "5.9209", "46.4097", "300",
                "saintclaude_Main.png" };
        Main.main(arguments);
    }

    // @Test
    public void besanconAndMultiDEMTest() throws Exception {
        String[] arguments = { "data/besancon.osm.gz", "data/N47E005.hgt",
                "5.9647", "47.2152", "6.0720", "47.2580", "300",
                "besancon_Main.png", "CH1903", "data/N47E006.hgt" };
        Main.main(arguments);
    }

    // @Test
    public void fourZonesDEMTest() throws Exception {
        String[] arguments = { "data/croiseedeschemins.osm.gz",
                "data/N46E006.hgt", "6.95", "46.95", "7.05", "47.05", "200",
                "forest_Main.png", "data/N46E007.hgt", "data/N47E006.hgt",
                "data/N47E007.hgt" };
        Main.main(arguments);
    }

    @Test
    public void twoZonesVertical() throws Exception {
        String[] arguments = { "data/twofiles.osm.gz", "data/N46E006.hgt",
                "6.5594", "46.9", "6.6508", "47.1", "200", "twoFiles.png",
                "CH1903", "data/N47E006.hgt" };
        Main.main(arguments);
    }

    // @Test
    public void comoTest() throws Exception {
        String[] arguments = { "data/como.osm.gz", "data/N45E009.hgt",
                "9.0439", "45.7921", "9.1119", "45.8302", "300", "como.png" };
        Main.main(arguments);
    }
}
