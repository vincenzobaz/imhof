package ch.epfl.imhof;

import org.junit.Test;

public final class MainTest {

    @Test
    public void interlakenTest() throws Exception {
        String[] arguments = { "data/interlaken.osm.gz", "data/N46E007.hgt",
                "7.8122", "46.6645", "7.9049", "46.7061", "300",
                "putainDeCadreInterlaken.png" };
        Main.main(arguments);
    }

    @Test
    public void lausanneTest() throws Exception {
        String[] arguments = { "data/lausanne.osm.gz", "data/N46E006.hgt",
                "6.5594", "46.5032", "6.6508", "46.5459", "300",
                "lausanne_Main.png" };
        Main.main(arguments);
    }

    @Test
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
                "besancon_Main.png", "data/N47E006.hgt" };
        Main.main(arguments);
    }

    // @Test
    public void fourZonesDEMTest() throws Exception {
        String[] arguments = { "data/chauxdefond.osm.gz", "data/N46E006.hgt",
                "6.7223", "46.9463", "7.1514", "47.1180", "300",
                "forest_Main.png", "data/N46E007.hgt", "data/N47E006.hgt",
                "data/N47E007.hgt" };
        Main.main(arguments);
    }
}
