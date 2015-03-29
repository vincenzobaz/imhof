package ch.epfl.imhof.osm;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import ch.epfl.imhof.BuildAll;
import ch.epfl.imhof.PointGeo;

public class OSMRelationTestPerso {
    private OSMRelation newRelation() {
        OSMRelation.Builder relationInConstruction = new OSMRelation.Builder(
                1234);
        OSMNode node = new OSMNode(00, new PointGeo(1, -1),
                BuildAll.newAttributes("nodekey1", "nodevalue1", "nodekey2",
                        "nodevalue2"));
        relationInConstruction.addMember(OSMRelation.Member.Type.NODE, "node",
                node);
        OSMWay way = BuildAll.newWay(01);
        relationInConstruction.addMember(OSMRelation.Member.Type.WAY, "way",
                way);
        OSMRelation relation = null;
        relationInConstruction.addMember(OSMRelation.Member.Type.RELATION,
                "relation", relation);
        relationInConstruction.setAttribute("uneclé", "unevaleur");
        relationInConstruction.setAttribute("uneautreclé", "uneautrevaleur");

        return relationInConstruction.build();
    }

    @Test
    public void builderTest() {
        OSMRelation newRelation = newRelation();

        assertTrue(newRelation.id() == 1234);
        assertTrue(newRelation.hasAttribute("uneclé"));
        assertFalse(newRelation.hasAttribute("pascetteclé"));
        assertTrue(newRelation.attributeValue("uneautreclé").equals(
                "uneautrevaleur"));
        assertFalse(newRelation.attributeValue("uneclé").equals(
                "paslabonnevaleur"));
    }

    @Test
    public void memberTest() {
        OSMRelation newRelation = newRelation();

        List<OSMRelation.Member> members = newRelation.members();
        assertTrue(members.get(0).type() == OSMRelation.Member.Type.NODE);
        assertFalse(members.get(2).type() == OSMRelation.Member.Type.WAY);

        assertTrue(members.get(1).role().equals("way"));
        assertFalse(members.get(0).role().equals("relation"));

        assertTrue(members.get(2).member() == null);
        assertFalse(members.get(0).member() == null);
    }

    @Test(expected = IllegalStateException.class)
    public void builderFailsWhenSetIncompleteIsCalled() {
        OSMRelation.Builder relationInConstruction = new OSMRelation.Builder(
                1234);
        relationInConstruction.addMember(
                OSMRelation.Member.Type.NODE,
                "role",
                new OSMNode(01, new PointGeo(0, 0), BuildAll.newAttributes(
                        "key", "value")));

        relationInConstruction.setIncomplete();
        assertTrue(relationInConstruction.isIncomplete());
        relationInConstruction.build();
    }
}
