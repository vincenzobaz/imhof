package ch.epfl.imhof.osm;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.*;
import ch.epfl.imhof.osm.OSMRelation.Member;
import ch.epfl.imhof.projection.Projection;

public final class OSMToGeoTransformer {
    public static final String[] SURFACE_VALUES = new String[] { "aeroway",
            "amenity", "building", "harbour", "historic", "landuse", "leisure",
            "man_made", "military", "natural", "office", "place", "power",
            "public_transport", "shop", "sport", "tourism", "water",
            "waterway", "wetland" };
    public static final String[] POLYLINE_VALUES = new String[] { "bridge",
            "highway", "layer", "man_made", "railway", "tunnel", "waterway" };
    public static final String[] POLYGON_VALUES = new String[] { "building",
            "landuse", "layer", "leisure", "natural", "waterway" };

    public static final Set<String> SURFACE_ATTRIBUTES = new HashSet<>(
            Arrays.asList(SURFACE_VALUES));
    public static final Set<String> POLYLINE_ATTRIBUTES = new HashSet<>(
            Arrays.asList(POLYLINE_VALUES));
    public static final Set<String> POLYGON_ATTRIBUTES = new HashSet<>(
            Arrays.asList(POLYGON_VALUES));

    private final Projection projection;

    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
    }

    public Map transform(OSMMap map) {
        Map.Builder mapInConstruction = new Map.Builder();

        for (OSMWay wayToConvert : map.ways()) {
            Attributes newAttributes = filteredAttributes(wayToConvert);

            if (!(OSMWayIsASurface(wayToConvert) || newAttributes.isEmpty())) {
                mapInConstruction.addPolyLine(new Attributed<>(
                        OSMWayToPolyLine(wayToConvert), newAttributes));
            } else if (OSMWayIsASurface(wayToConvert)
                    && wayToConvert.isClosed() && !newAttributes.isEmpty()) {
                mapInConstruction.addPolygon(new Attributed<>(new Polygon(
                        OSMWayToClosedPolyLine(wayToConvert)), newAttributes));
            }
        }
    }

    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {
        List<ClosedPolyLine> ringsForRole = new ArrayList<>();
        List<PolyLine> roleWays = new ArrayList<>();

        for (Member m : relation.members()) {
            if (m.role().equals(role)) {
                if (m.type() == OSMRelation.Member.Type.WAY) {
                    roleWays.add(OSMWayToPolyLine((OSMWay) m.member()));
                } else if (m.type() == OSMRelation.Member.Type.RELATION) {

                }
            }
        }
        return ringsForRole;
    }

    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation,
            Attributes attributes) {

    }

    private Attributes filteredAttributes(OSMWay way) {
        Attributes filteredAttributes;
        if (!OSMWayIsASurface(way)) {
            filteredAttributes = way.attributes().keepOnlyKeys(
                    POLYLINE_ATTRIBUTES);
        } else {
            filteredAttributes = way.attributes().keepOnlyKeys(
                    POLYGON_ATTRIBUTES);
        }
        return filteredAttributes;
    }

    private PolyLine OSMWayToPolyLine(OSMWay way) {
        if (way.isClosed()) {
            return OSMWayToClosedPolyLine(way);
        } else {
            return OSMWayToOpenPolyLine(way);
        }
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
            boolean hasSurfaceAttribute = false;
            Iterator<String> iterator = SURFACE_ATTRIBUTES.iterator();
            while (!hasSurfaceAttribute && iterator.hasNext()) {
                hasSurfaceAttribute = way.hasAttribute(iterator.next());
            }
            return hasSurfaceAttribute;
        }
    }
}
