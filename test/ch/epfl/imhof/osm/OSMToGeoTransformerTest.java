package ch.epfl.imhof.osm;

import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

import ch.epfl.imhof.Map;
import ch.epfl.imhof.projection.*;

public class OSMToGeoTransformerTest {

    private String fichier = "lausanne.osm";

    public OSMMap readFile(String nomfichier) {
        String fileALire = "data/" + nomfichier;
        System.out.println("Debut lecture du fichier " + fileALire);
        try {
            return OSMMapReader.readOSMFile(fileALire, false);
        } catch (IOException | SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Test public void conversionDesCheminsMarche() { OSMMap fileMap =
     *       readFile(fichier); OSMToGeoTransformer convertisseur = new
     *       OSMToGeoTransformer( new CH1903Projection());
     *       convertisseur.conversionChemins(fileMap.ways());
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
     *       convertisseur.conversionChemins(fileMap.ways());
     *       System.out.println(fileMap.relations().size()); Map provina =
     *       convertisseur.mapToBe.build();
     *       System.out.println("Nombre de polyline " +
     *       provina.polyLines().size());
     *       System.out.println("Nombre de polygons " +
     *       provina.polygons().size()); }
     */

    @Test
    public void transformMarche() {
        OSMMap fileMap = readFile(fichier);
        OSMToGeoTransformer convertisseur = new OSMToGeoTransformer(
                new CH1903Projection());
        Map mappa = convertisseur.transform(fileMap);
        System.out.println("conversion");
        System.out.println("Nombre de polyline " + mappa.polyLines().size());
        System.out.println("Nombre de polygons " + mappa.polygons().size());
    }
}
