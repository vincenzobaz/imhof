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
            /*if (!wayToConvert.isClosed()) {
                mapInConstruction.addPolyLine(new Attributed<>(
                        OSMWayToOpenPolyLine(wayToConvert), filteredAttributes(
                                wayToConvert, "polyline")));
            } else if (OSMWayIsASurface(wayToConvert)) {
                mapInConstruction.addPolygon(new Attributed<>(new Polygon(
                        OSMWayToClosedPolyLine(wayToConvert)),
                        filteredAttributes(wayToConvert, "polygon")));
            } else {
                mapInConstruction.addPolyLine(new Attributed<>(
                        OSMWayToClosedPolyLine(wayToConvert),
                        filteredAttributes(wayToConvert, "polyline")));
            }*/

            if (!OSMWayIsASurface(wayToConvert)) {
                Attributes polyLineAttributes = filteredAttributes(
                        wayToConvert, "polyline");
                if (!(wayToConvert.isClosed() || polyLineAttributes.isEmpty())) {
                    mapInConstruction.addPolyLine(new Attributed<>(
                            OSMWayToOpenPolyLine(wayToConvert),
                            polyLineAttributes));
                } else if (wayToConvert.isClosed()
                        && !polyLineAttributes.isEmpty()) {
                    mapInConstruction.addPolyLine(new Attributed<>(
                            OSMWayToClosedPolyLine(wayToConvert),
                            polyLineAttributes));
                }
            } else {
                Attributes polygonAttributes = filteredAttributes(wayToConvert,
                        "polygon");
                if (wayToConvert.isClosed() && !polygonAttributes.isEmpty()) {
                    mapInConstruction.addPolygon(new Attributed<>(new Polygon(
                            OSMWayToClosedPolyLine(wayToConvert)),
                            polygonAttributes));
                }
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
    }

    private Attributes filteredAttributes(OSMEntity entity, String type) {
        Attributes filteredAttributes = null;
        switch (type) {
        case "polyline":
            filteredAttributes = entity.attributes().keepOnlyKeys(
                    POLYLINE_ATTRIBUTES);
        case "polygon":
            filteredAttributes = entity.attributes().keepOnlyKeys(
                    POLYGON_ATTRIBUTES);
        }
        return filteredAttributes;
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
            boolean hasSurfaceAttribute = false;
            Iterator<String> iterator = SURFACE_ATTRIBUTES.iterator();
            while (!hasSurfaceAttribute && iterator.hasNext()) {
                hasSurfaceAttribute = way.hasAttribute(iterator.next());
            }
            return hasSurfaceAttribute;
        }
    }
}
