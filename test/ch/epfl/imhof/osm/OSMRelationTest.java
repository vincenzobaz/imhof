package ch.epfl.imhof.osm;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Attributes;

public class OSMRelationTest {   
    @Test
    public void builderTest() {
        OSMRelation.Builder relationInConstruction = new OSMRelation.Builder(
                1234);
        OSMNode node = new OSMNode(01, new PointGeo(1, -1), new Attributes(new HashMap<>()));
        relationInConstruction.addMember(OSMRelation.Member.Type.NODE, "node", node);
        OSMWay way = new OSMWay(02, , );
        relationInConstruction.addMember(OSMRelation.Member.Type.WAY, "way", way);
        OSMRelation relation = null;
        relationInConstruction.addMember(OSMRelation.Member.Type.RELATION, "relation", relation);
    }

    @Test (expected = IllegalStateException.class)
    public void builderFailsWhenSetIncompleteIsCalled() {
        OSMRelation.Builder relationInConstruction = new OSMRelation.Builder(
                1234);
        relationInConstruction.addMember(OSMRelation.Member.Type.NODE, "role",
                new OSMNode(01, new PointGeo(0, 0), new Attributes(
                        new HashMap<>())));
        
       relationInConstruction.setIncomplete();
       assertTrue(relationInConstruction.isIncomplete());
       relationInConstruction.build();
    }
}
