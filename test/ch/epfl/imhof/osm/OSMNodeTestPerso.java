package ch.epfl.imhof.osm;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.HashMap;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Attributes;

public final class OSMNodeTest {
    @Test
    public void positionReturnsRightValues() {
        OSMNode prova = new OSMNode(123456789, new PointGeo(Math.PI / 3,
                Math.PI / 3), genereAttribuz("il cane", "il gatto", "il topo",
                "l'elefante", "nicolas", "maxime"));
        assertTrue(Math.PI / 3 == prova.position().latitude());
        assertTrue(Math.PI / 3 == prova.position().longitude());
    }

    private Attributes genereAttribuz(String... args) {
        Map<String, String> newHashMap = new HashMap<>();
        for (int i = 0; i < args.length - 1; ++i) {
            newHashMap.put(args[i], args[i + 1]);
        }
        return new Attributes(newHashMap);
    }

    @Test
    public void builderAndNodeTest() {
        OSMNode.Builder nodeInConstruction = new OSMNode.Builder(1234,
                new PointGeo(Math.toRadians(50), Math.toRadians(40)));
        nodeInConstruction.setAttribute("clé", "valeur");
        nodeInConstruction.setAttribute("point", "test");
        OSMNode newNode = nodeInConstruction.build();
        assertTrue(Math.toRadians(50) == newNode.position().longitude());
        assertTrue(Math.toRadians(40) == newNode.position().latitude());
        assertFalse(newNode.hasAttribute("pas celui-là"));
        assertTrue(newNode.hasAttribute("clé"));
        assertTrue(newNode.hasAttribute("point"));
        assertTrue(newNode.id() == 1234);
        assertTrue(newNode.attributeValue("clé").equals("valeur"));
        assertTrue(newNode.attributeValue("point").equals("test"));
    }

    @Test(expected = IllegalStateException.class)
    public void builderFailsIfIncomplete() {
        OSMNode.Builder nodeInConstruction = new OSMNode.Builder(1234,
                new PointGeo(1, 1));
        nodeInConstruction.setIncomplete();
        assertTrue(nodeInConstruction.isIncomplete());
        nodeInConstruction.build();
    }
}
