package ch.epfl.imhof.osm;

import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

import ch.epfl.imhof.Map;
import ch.epfl.imhof.projection.*;

public class OSMToGeoTransformerTest {
    /**@Test
    public void transformerMarche() {
        String fileALire = "data/lausanne.osm";
        OSMMap fileMap = null;
        System.out.println("Debut lecture du fichier " + fileALire);
        try {
            fileMap = OSMMapReader.readOSMFile(fileALire, false);
        } catch (IOException | SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Chemins dans le OSMMap " + fileMap.ways().size());
        System.out.println("Relations dans le Map "
                + fileMap.relations().size());
        System.out.println("L'attribut de polygon est present? "
                + fileMap.relations().get(0).hasAttribute("type"));
        OSMToGeoTransformer convertisseur = new OSMToGeoTransformer(
                new CH1903Projection());
        Map prova = convertisseur.transform(fileMap);
        System.out.println("Nombre de polyline " + prova.polyLines().size());
        System.out.println("Nombre de polygons " + prova.polygons().size());
    }**/

    public 
    @Test
    public void conversionDesCheminsMarche() {
        String fileALire = "data/lausanne.osm";
        OSMMap fileMap = null;
        System.out.println("Debut lecture du fichier " + fileALire);
        try {
            fileMap = OSMMapReader.readOSMFile(fileALire, false);
        } catch (IOException | SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        OSMToGeoTransformer convertisseur = new OSMToGeoTransformer(
                new CH1903Projection());
        convertisseur.conversionChemins(fileMap.ways());
        System.out.println(fileMap.ways().size());
        Map provina = convertisseur.mapToBe.build()   ;
        System.out.println("Nombre de polyline " + provina.polyLines().size());
        System.out.println("Nombre de polygons " + provina.polygons().size());
    }
}
