package ch.epfl.imhof.osm;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Comparator;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.geometry.*;
import ch.epfl.imhof.osm.OSMRelation.Member;
import ch.epfl.imhof.projection.Projection;

/**
 * Classe représentant un convertisseur de données OSM en carte. Elle est
 * immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
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

    /**
     * Construit un convertisseur OSM en géométrie qui utilise la projection
     * donnée
     * 
     * @param projection
     *            la projection à utiliser, une Projection
     */
    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
    }

    /**
     * Convertit une carte OSM en une carte géométrique projetée
     * 
     * @param map
     *            la carte qu'on veut convertir, une OSMMap
     * @return la carte projetée, une Map
     */
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

        for (OSMRelation relationToConvert : map.relations()) {
            Attributes newAttributes = relationToConvert.attributes()
                    .keepOnlyKeys(POLYGON_ATTRIBUTES);
            if ("multipolygon".equals(relationToConvert.attributeValue("type"))
                    && !newAttributes.isEmpty()) {

                // performance: copie ou mise en mémoire
                for (Attributed<Polygon> polygon : assemblePolygon(
                        relationToConvert, newAttributes)) {
                    mapInConstruction.addPolygon(polygon);
                }
            }
        }

        return mapInConstruction.build();
    }

    /**
     * Retourne l'ensemble des anneaux de la relation donnée ayant le rôle
     * spécifié
     * 
     * @param relation
     * @param role
     * @return
     */
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {
        List<PolyLine> roleWays = new ArrayList<>();

        for (Member m : relation.members()) {
            if (m.role().equals(role)
                    && m.type() == OSMRelation.Member.Type.WAY) {
                roleWays.add(OSMWayToPolyLine((OSMWay) m.member()));
            }
        }

        Graph<Point> nonOrientedGraph = graphCreator(roleWays);

        if (!everyNodeHasTwoNeighbors(nonOrientedGraph)) {
            return Collections.emptyList();
        }

        java.util.Map<Point, Boolean> visitedNodes = new HashMap<>();
    }

    /**
     * Retourne la liste des polygones attribués de la relation donnée, en leur
     * attachant les attributs donnés
     * 
     * @param relation
     * @param attributes
     * @return
     */
    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation,
            Attributes attributes) {
        List<ClosedPolyLine> innerRings = ringsForRole(relation, "inner");
        List<ClosedPolyLine> outerRings = ringsForRole(relation, "outer");

        outerRings.sort(new Comparator<ClosedPolyLine>() {
            @Override
            public int compare(ClosedPolyLine line1, ClosedPolyLine line2) {
                return (int) Math.signum(line1.area() - line2.area());
            }
        });
        
        
    }

    /**
     * Retourne vrai si tous les noeuds du graphe donné possèdent exactement
     * deux voisins
     * 
     * @param nonOrientedGraph
     * @return
     */
    private boolean everyNodeHasTwoNeighbors(Graph<Point> nonOrientedGraph) {
        boolean everyNodeHasTwoNeighbors = true;
        Iterator<Point> iterator = nonOrientedGraph.nodes().iterator();
        while (everyNodeHasTwoNeighbors && iterator.hasNext()) {
            everyNodeHasTwoNeighbors = (nonOrientedGraph.neighborsOf(
                    iterator.next()).size() == 2);
        }
        return everyNodeHasTwoNeighbors;
    }

    /**
     * Construit un graphe à partir de la liste de polylignes donnée
     * 
     * @param roleWays
     * @return
     */
    private Graph<Point> graphCreator(List<PolyLine> roleWays) {
        Graph.Builder<Point> graphInConstruction = new Graph.Builder<>();
        for (PolyLine polyline : roleWays) {
            List<Point> pointList = polyline.points();
            for (int i = 0; i < pointList.size(); ++i) {
                graphInConstruction.addNode(pointList.get(i));
                if (i != 0) {
                    graphInConstruction.addEdge(pointList.get(i),
                            pointList.get(i - 1));
                }
            }
        }
        return graphInConstruction.build();
    }

    /**
     * Retourne une version filtrée des attributs du chemin donné
     * 
     * @param way
     * @return
     */
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

    /**
     * Convertit le chemin donné en polyligne
     * 
     * @param way
     * @return
     */
    private PolyLine OSMWayToPolyLine(OSMWay way) {
        if (way.isClosed()) {
            return OSMWayToClosedPolyLine(way);
        } else {
            return OSMWayToOpenPolyLine(way);
        }
    }

    /**
     * Convertit le chemin donné en polyligne ouverte
     * 
     * @param way
     * @return
     */
    private OpenPolyLine OSMWayToOpenPolyLine(OSMWay way) {
        PolyLine.Builder openPolylineInConstruction = new PolyLine.Builder();
        for (OSMNode nodeToConvert : way.nodes()) {
            openPolylineInConstruction.addPoint(projection
                    .project(nodeToConvert.position()));
        }
        return openPolylineInConstruction.buildOpen();
    }

    /**
     * Convertit le chemin donné en polyligne fermée
     * 
     * @param way
     * @return
     */
    private ClosedPolyLine OSMWayToClosedPolyLine(OSMWay way) {
        PolyLine.Builder closedPolyLineInConstruction = new PolyLine.Builder();
        for (OSMNode nodeToConvert : way.nonRepeatingNodes()) {
            closedPolyLineInConstruction.addPoint(projection
                    .project(nodeToConvert.position()));
        }
        return closedPolyLineInConstruction.buildClosed();
    }

    /**
     * Retourne vrai si le chemin donné est une surface
     * 
     * @param way
     * @return
     */
    private boolean OSMWayIsASurface(OSMWay way) {
        String area = way.attributeValue("area");
        if ("yes".equals(area) || "1".equals(area) || "true".equals(area)) {
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
