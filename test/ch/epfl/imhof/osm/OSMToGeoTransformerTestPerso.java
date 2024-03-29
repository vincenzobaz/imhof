package ch.epfl.imhof.osm;

import org.junit.Test;

import ch.epfl.imhof.Map;
import ch.epfl.imhof.projection.*;

public class OSMToGeoTransformerTestPerso {

    public OSMMap readFile(String nomfichier) {
        String fileALire = "data/" + nomfichier;
        System.out.println("Debut lecture du fichier " + fileALire);
        OSMMap newMap = null;
        try {
            newMap = OSMMapReader.readOSMFile(fileALire, false);
        } catch (Exception e) {
            System.out.println("Exception!");
        }
        return newMap;
    }

    /**
     * @Test public void conversionDesCheminsMarche() { OSMMap fileMap =
     *       readFile(fichier); OSMToGeoTransformer convertisseur = new
     *       OSMToGeoTransformer( new CH1903Projection());
     *       convertisseur.waysConversion(fileMap.ways());
     *       System.out.println(fileMap.ways().size()); Map provina =
     *       convertisseur.mapToBe.build();
     *       System.out.println("Nombre de polyline " +
     *       provina.polyLines().size());
     *       System.out.println("Nombre de polygons " +
     *       provina.polygons().size()); }
     */

    /**
     * @Test public void conversionDesRelationsMarche() { OSMMap fileMap =
     *       readFile(fichier); OSMToGeoTransformer convertisseur = new
     *       OSMToGeoTransformer( new CH1903Projection());
     *       convertisseur.waysConversion(fileMap.ways());
     *       System.out.println(fileMap.relations().size()); Map provina =
     *       convertisseur.mapToBe.build();
     *       System.out.println("Nombre de polyline " +
     *       provina.polyLines().size());
     *       System.out.println("Nombre de polygons " +
     *       provina.polygons().size()); }
     */

    public void transformMarche(String fichier) {
        OSMMap fileMap = readFile(fichier);
        OSMToGeoTransformer convertisseur = new OSMToGeoTransformer(
                new CH1903Projection());
        Map mappa = convertisseur.transform(fileMap);
        System.out.println("conversion");
        System.out.println("Chemins dans le OSMMap " + fileMap.ways().size());
        System.out.println("Relations dans le OSMMap "
                + fileMap.relations().size());
        System.out.println("L'attribut de polygon est present? "
                + fileMap.relations().get(0).hasAttribute("type"));

        System.out.println("Nombre de polylines dans Map "
                + mappa.polyLines().size());
        System.out.println("Nombre de polygons dans Map "
                + mappa.polygons().size());
        System.out.println();
        /*
         * OSMToGeoTransformer convertisseur = new OSMToGeoTransformer( new
         * CH1903Projection()); Map prova = convertisseur.transform(fileMap);
         * System.out.println("Nombre de polyline " + prova.polyLines().size());
         * System.out.println("Nombre de polygons " + prova.polygons().size());
         */
    }

    @Test
    public void testAll() {
        transformMarche("lausanne.osm");
        // transformMarche("berne.osm");
        // transformMarche("interlaken.osm");
        // transformMarche("lausanne.osm");
        transformMarche("berne.osm");
        transformMarche("interlaken.osm");
        transformMarche("lc.osm");
    }

    /*
     * @Test public void conversionDesCheminsMarche() { String fileALire =
     * "data/lausanne.osm"; OSMMap fileMap = null;
     * System.out.println("Debut lecture du fichier " + fileALire); try {
     * fileMap = OSMMapReader.readOSMFile(fileALire, false); } catch
     * (IOException | SAXException e) { e.printStackTrace(); }
     * OSMToGeoTransformer convertisseur = new OSMToGeoTransformer( new
     * CH1903Projection()); convertisseur.waysConversion(fileMap.ways());
     * System.out.println(fileMap.ways().size()); Map provina =
     * convertisseur.mapToBe.build() ; System.out.println("Nombre de polyline "
     * + provina.polyLines().size()); System.out.println("Nombre de polygons " +
     * provina.polygons().size()); }
     */
}
