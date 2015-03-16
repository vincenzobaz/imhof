package ch.epfl.imhof.osm;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.projection.Projection;

public final class OSMToGeoTransformer {
    private final Projection projection;
    private final static Set<String> closedWayAttributes = new HashSet<>(String[]);

    public OSMToGeoTransformer(Projection projection) {
        String[] temp = {"aeroway", "amenity", "building", "harbour", "historic",
                "landuse", "leisure", "man_made", "military", "natural",
                "office", "place", "power", "public_transport", "shop",
                "sport", "tourism", "water", "waterway", "wetland"};
        this.projection = projection;
        
    }

    public Map transform(OSMMap map) {

    }

    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {

    }

    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation,
            Attributes attributes) {

    }
}
