package ch.epfl.imhof.osm;

import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

public class OSMMapReaderTest {
    @Test
    public void readOSMFileWorks() {
        try {
            String fileALire = "data/lc.osm";
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

            // temps de outputter sur fichier de texte tous les ways.:
            PrintWriter debug = new PrintWriter("Debugging.txt");
            for (OSMWay way : ways) {
                debug.println("Way, ID: " + way.id());
                for (OSMNode node : way.nodes()) {
                    debug.println("    Node, ID: " + node.id() + " lat: "
                            + Math.toDegrees(node.position().latitude())
                            + " lon: "
                            + Math.toDegrees(node.position().longitude()));
                }
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
                    debug.println("    Member, Type: " + type + " role: " + member.role());
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
