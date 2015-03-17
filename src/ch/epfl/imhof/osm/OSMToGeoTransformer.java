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

        surfaceAttributes = new ArrayList<>(Arrays.asList(tab1));
        polyLineAttributes = new ArrayList<>(Arrays.asList(tab2));
        polygonAttributes = new ArrayList<>(Arrays.asList(tab3));
        this.projection = projection;
    }

    public Map transform(OSMMap map) {
        List<Polygon> surfaces = new ArrayList<>();
        List<OpenPolyLine> polylines = new ArrayList<>();
        
        for (OSMWay wayToConvert : map.ways()) {
            if (wayToConvert.isClosed() && OSMWayIsASurface(wayToConvert)) {
                surfaces.add(new Polygon(OSMWayToClosedPolyLine(wayToConvert)));
            } else {
                polylines.add(OSMWayToOpenPolyLine(wayToConvert));
            }
        }
        
        List<ClosedPolyLine> innerRings = new ArrayList<>();
        List<ClosedPolyLine> outerRings = new ArrayList<>();
        for (OSMRelation relation : map.relations()) {
            innerRings.addAll(ringsForRole(relation, "inner"));
        }
        
        
        for (OSMRelation relation : map.relations()) {
            outerRings.addAll(ringsForRole(relation, "outer"));
        }
        
        
        +
        
        
    }

    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {
        List<ClosedPolyLine> ringsForRole = new ArrayList<>();
        for (Member m : relation.members()) {
            if (m.role().equals(role)
                    && m.type() == OSMRelation.Member.Type.WAY) {
                ringsForRole.add(OSMWayToClosedPolyLine((OSMWay) m.member()));
            }
        }
        return ringsForRole;
    }

    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation,
            Attributes attributes) {

    }

    private OpenPolyLine OSMWayToOpenPolyLine(OSMWay way) {
        PolyLine.Builder openPolylineInConstruction = new PolyLine.Builder();
        for (OSMNode nodeToConvert : way.nodes()) {
            openPolylineInConstruction.addPoint(projection
                    .project(nodeToConvert.position()));
        }
        return openPolylineInConstruction.buildOpen();
    }

    private ClosedPolyLine OSMWayToClosedPolyLine(OSMWay way) {
        PolyLine.Builder closedPolyLineInConstruction = new PolyLine.Builder();
        for (OSMNode nodeToConvert : way.nonRepeatingNodes()) {
            closedPolyLineInConstruction.addPoint(projection
                    .project(nodeToConvert.position()));
        }
        return closedPolyLineInConstruction.buildClosed();
    }

    private boolean OSMWayIsASurface(OSMWay way) {
        String area = way.attributeValue("area");
        if (area.equals("yes") || area.equals("1") || area.equals("true")) {
            return true;
        } else {
            int index = 0;
            boolean hasSurfaceAttribute = false;
            while (!hasSurfaceAttribute && index < surfaceAttributes.size()) {
                hasSurfaceAttribute = way.hasAttribute(surfaceAttributes
                        .get(index));
                ++index;
            }
            return hasSurfaceAttribute;
        }
    }
}
