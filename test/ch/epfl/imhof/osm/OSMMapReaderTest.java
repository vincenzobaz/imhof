package ch.epfl.imhof.osm;

import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

public class OSMMapReaderTest {
    @Test public void lectures(){
        readOSMFileWorks("lausanne.osm");
        readOSMFileWorks("berne.osm");
        readOSMFileWorks("interlaken.osm");
    }
    public void readOSMFileWorks(String zone) {
        try {
            String fileALire = "data/"+zone;
            long preTime = System.currentTimeMillis();
            System.out.println("Debut lecture du fichier "+fileALire);
            OSMMap fileMap = OSMMapReader.readOSMFile(fileALire,
                    false);
            long endTime = System.currentTimeMillis();
            System.out.println("La lecture du fichier osm a durée: "
                    + (endTime - preTime) + " ms, approximativement "
                    + ((endTime - preTime) / 1000) + " secondes , "+"\uD83D\uDC4D");
            List<OSMRelation> relations = fileMap.relations();
            List<OSMWay> ways = fileMap.ways();

        System.out.println("Chemins dans le OSMMap " + fileMap.ways().size());
        System.out.println("Relations dans le OSMMap "
                + fileMap.relations().size());
        System.out.println("L'attribut de polygon est present? "
                + fileMap.relations().get(0).hasAttribute("type"));
        System.out.println();
 
            // temps de outputter sur fichier de texte tous les ways.:
            PrintWriter debug = new PrintWriter("Debugging_"+zone+".txt");
            for (OSMWay way : ways) {
                debug.println("Way, ID: " + way.id());
                for (OSMNode node : way.nodes()) {
                    debug.println("    Node, ID: " + node.id() + " lat: "
                            + Math.toDegrees(node.position().latitude())
                            + " lon: "
                            + Math.toDegrees(node.position().longitude()));
                }
                debug.println("   hasAttribute(layer) "
                        + way.hasAttribute("layer"));
                debug.println("   hasAttribute(source) "
                        + way.hasAttribute("source"));
            }
            debug.println();
            // temps de printer sur fichier de texte les relations
            for (OSMRelation relation : relations) {
                debug.println("Relation, ID:" + relation.id());
                for (OSMRelation.Member member : relation.members()) {
                    String type = "";
                    switch (member.type()) {
                    case WAY:
                        type = "way";
                        break;
                    case NODE:
                        type = "node";
                        break;
                    case RELATION:
                        type = "relation";
                        break;
                    }
                    debug.println("    Member, Type: " + type + " role: "
                            + member.role());
                    debug.println("   hasAttribute(type) "
                            + relation.hasAttribute("type"));
                }
            }
            debug.close();
        } catch (IOException e) {
            System.out.println("Exception catchée!!!");
            System.out.println("Message de l'exception: " + e.getMessage());
        } catch (SAXException e) {
            System.out.println("Exception SAX");
        }
    }
}
