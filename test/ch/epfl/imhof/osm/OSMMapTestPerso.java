package ch.epfl.imhof.osm;

import org.junit.Test;

import ch.epfl.imhof.BuildAll;
import static org.junit.Assert.*;

import ch.epfl.imhof.PointGeo;

public class OSMMapTestPerso {
    @Test
    public void builderTest() {
        OSMMap.Builder mapInConstruction = new OSMMap.Builder();
        mapInConstruction.addNode(new OSMNode(00, new PointGeo(-1, 0), BuildAll
                .newAttributes()));
        mapInConstruction.addWay(BuildAll.newWay(01));
        mapInConstruction.addRelation(BuildAll.newRelation(02));

        assertTrue(mapInConstruction.nodeForId(00).position().longitude() == -1);
        assertTrue(mapInConstruction.wayForId(01).firstNode()
                .attributeValue("key00").equals("value00"));
    }
}
