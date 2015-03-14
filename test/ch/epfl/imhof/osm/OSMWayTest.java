package ch.epfl.imhof.osm;

import org.junit.Test;
import static org.junit.Assert.*;

import ch.epfl.imhof.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public final class OSMWayTest {
    public Attributes newAttributes(String... args) {
        Map<String, String> newHashMap = new HashMap<>();
        for (int i = 0; i < args.length - 1; ++i) {
            newHashMap.put(args[i], args[i + 1]);
        }
        return new Attributes(newHashMap);
    }

    @Test
    public void builderAndNonClosedWayTest() {
        OSMWay.Builder wayInConstruction = new OSMWay.Builder(1234);
        wayInConstruction.addNode(new OSMNode(01, new PointGeo(0, 0),
                newAttributes("clé1", "valeur1", "chemin1", "way1")));
        wayInConstruction.addNode(new OSMNode(02, new PointGeo(1, 1),
                newAttributes("clé2", "valeur2", "chemin2", "way2")));
        wayInConstruction.addNode(new OSMNode(03, new PointGeo(-1, -1),
                newAttributes("clé3", "valeur3", "chemin3", "way3")));
        wayInConstruction.setAttribute("test1", "testA");

        OSMWay newWay = wayInConstruction.build();

        assertTrue(newWay.nodesCount() == 3);

        List<OSMNode> nodes = newWay.nodes();
        assertTrue(nodes.get(0).id() == 01);
        assertTrue(nodes.get(2).id() == 03);

        List<OSMNode> nonRepeatingNodes = newWay.nonRepeatingNodes();
        assertTrue(nodes.get(0) == nonRepeatingNodes.get(0));
        assertTrue(nodes.get(2) == nonRepeatingNodes.get(2));

        assertTrue(newWay.firstNode().position().longitude() == 0);
        assertTrue(newWay.lastNode().position().latitude() == -1);

        assertFalse(newWay.isClosed());
        assertTrue(newWay.id() == 1234);
        assertTrue(newWay.hasAttribute("test1"));
        assertFalse(newWay.hasAttribute("nope"));
        assertTrue(newWay.attributeValue("test1") == "testA");
    }

    @Test
    public void builderAndClosedWayTest() {
        OSMWay.Builder wayInConstruction = new OSMWay.Builder(4567);
        OSMNode node1 = new OSMNode(01, new PointGeo(0, 0), newAttributes(
                "clé1", "valeur1", "chemin1", "way1"));
        wayInConstruction.addNode(node1);
        wayInConstruction.addNode(new OSMNode(02, new PointGeo(1, 1),
                newAttributes("clé2", "valeur2", "chemin2", "way2")));
        wayInConstruction.addNode(new OSMNode(03, new PointGeo(-1, -1),
                newAttributes("clé3", "valeur3", "chemin3", "way3")));
        wayInConstruction.addNode(node1);
        OSMWay newWay = wayInConstruction.build();

        assertTrue(newWay.nodesCount() == 4);
        assertTrue(newWay.isClosed());
        assertTrue(newWay.id() == 4567);

        List<OSMNode> nodes = newWay.nonRepeatingNodes();
        assertTrue(nodes.size() == 3);
        assertTrue(nodes.get(0).id() == 01);
        assertTrue(nodes.get(0).attributeValue("chemin1") == "way1");
        assertTrue(nodes.get(1).id() == 02);
        assertTrue(nodes.get(1).hasAttribute("clé2"));
        assertTrue(nodes.get(2).id() == 03);
        assertTrue(nodes.get(2).hasAttribute("chemin3"));
    }

    @Test(expected = IllegalStateException.class)
    public void builderFailsWhenNodesCountIsLessThanTwo() {
        OSMWay.Builder wayInConstruction = new OSMWay.Builder(891011);
        wayInConstruction.addNode(new OSMNode(01, new PointGeo(0, 0),
                newAttributes("clé1", "valeur1", "chemin1", "way1")));
        assertTrue(wayInConstruction.isIncomplete());
        wayInConstruction.build();
    }

    @Test(expected = IllegalStateException.class)
    public void builderFailsWhenSetIncompleteIsCalled() {
        OSMWay.Builder wayInConstruction = new OSMWay.Builder(1234);
        wayInConstruction.addNode(new OSMNode(01, new PointGeo(0, 0),
                newAttributes("clé1", "valeur1", "chemin1", "way1")));
        wayInConstruction.addNode(new OSMNode(02, new PointGeo(1, 1),
                newAttributes("clé2", "valeur2", "chemin2", "way2")));
        wayInConstruction.addNode(new OSMNode(03, new PointGeo(-1, -1),
                newAttributes("clé3", "valeur3", "chemin3", "way3")));

        wayInConstruction.setIncomplete();
        assertTrue(wayInConstruction.isIncomplete());
        wayInConstruction.build();
    }
}
