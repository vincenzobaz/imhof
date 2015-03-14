package ch.epfl.imhof.osm;

import java.util.List;
import java.io.PrintWriter;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReaderTest {
    @Test
    public void readOSMFileWorks() {
        try {
            String file = getClass().getResource("/bc.osm").getFile();
            OSMMap fileMap = OSMMapReader.readOSMFile(file, false);
            List<OSMRelation> relations = fileMap.relations();
            List<OSMWay> ways = fileMap.ways();

            // temps de outputter sur fichier de texte tous les ways.:
            PrintWriter debug = new PrintWriter("Debugging.txt");
            for (OSMWay way : ways){
                debug.println("Way, ID: "+way.id());
                for (OSMNode node : way.nodes()){
                    debug.println("    Node, ID: "+node.id()+ " lat: "+Math.toDegrees(node.position().latitude())+" lon: "+Math.toDegrees(node.position().longitude()));
                }
            }
            debug.println();
            //temps de printer sur fichier de texte les relations
            for (OSMRelation relation : relations){
                debug.println("Relation, ID:"+relation.id());
                for (OSMRelation.Member member : relation.members()){
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
                    debug.println("Type: "+type+ " role: "+member.role());
                }
            }
            
            
            
            
            
            
            debug.close();
        } catch (Exception e) {
            System.out.println("Exception catchée!!!");
            System.out.println(e.getMessage());
        }
    }
}
