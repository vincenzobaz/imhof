package ch.epfl.imhof.osm;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.geometry.*;
import ch.epfl.imhof.osm.OSMRelation.Member;
import ch.epfl.imhof.projection.Projection;

public final class OSMToGeoTransformer {
    private final Projection projection;
    private final List<String> surfaceAttributes;
    private final List<String> polyLineAttributes;
    private final List<String> polygonAttributes;

    public OSMToGeoTransformer(Projection projection) {
        String[] tab1 = { "aeroway", "amenity", "building", "harbour",
                "historic", "landuse", "leisure", "man_made", "military",
                "natural", "office", "place", "power", "public_transport",
                "shop", "sport", "tourism", "water", "waterway", "wetland" };
        String[] tab2 = { "bridge", "highway", "layer", "man_made", "railway",
                "tunnel", "waterway" };
        String[] tab3 = { "building", "landuse", "layer", "leisure", "natural",
                "waterway" };

        closedWayAttributes = new ArrayList(Arrays.asList(tab1));
        surfaceAttributes = new ArrayList(Arrays.asList(tab1));
        polyLineAttributes = new ArrayList<>(Arrays.asList(tab2));
        polygonAttributes = new ArrayList<>(Arrays.asList(tab3));
        this.projection = projection;
    }

    public Map transform(OSMMap map) {
    }
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {
        List<ClosedPolyLine> ringsforRole = new ArrayList<>();
        for (Member m : relation.members()) {
            if (m.role().equals(role)
                    && m.type() == OSMRelation.Member.Type.WAY) {
                ringsforRole.add((ClosedPolyLine)OSMWayToPolyLine((OSMWay) m.member()));
            }
        }
        return ringsforRole;
    }

    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation,
            Attributes attributes) {

    }

    private PolyLine OSMWayToPolyLine(OSMWay way) {
        PolyLine.Builder polylineInConstruction = new PolyLine.Builder();
        List<OSMNode> nodesList = way.nonRepeatingNodes();
        for (OSMNode nodeToAdd : nodesList) {
            polylineInConstruction.addPoint(projection.project(nodeToAdd
                    .position()));
        }
        if (way.isClosed()) {
            return polylineInConstruction.buildClosed();
        } else {
            return polylineInConstruction.buildOpen();
        }
    }

    private boolean OSMWayIsASurface(OSMWay way) {
        String area = way.attributeValue("area");
        if (area.equals("yes") || area.equals("1") || area.equals("true")) {
            return true;
        } else {
            int index = 0;
            boolean hasSurfaceAttribute = false;
            while (!hasSurfaceAttribute && index < surfaceAttributes.size()) {
                hasSurfaceAttribute = way.hasAttribute(surfaceAttributes.get(index));
                ++index;
            }
            return hasSurfaceAttribute;
        }        
    }
}
