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
    private Map.Builder mapToBe;

    /**
     * Construit un convertisseur OSM en géométrie qui utilise la projection
     * donnée
     * 
     * @param projection
     *            la projection à utiliser, une Projection
     */
    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
        mapToBe = new Map.Builder();
    }

    /**
     * Convertit une carte OSM en une carte géométrique projetée
     * 
     * @param map
     *            la carte qu'on veut convertir, une OSMMap
     * @return la carte projetée, une Map
     */
    public Map transform(OSMMap map) {
        waysConversion(map.ways());
        relationsConversion(map.relations());

        return mapToBe.build();
    }

    private void relationsConversion(List<OSMRelation> relations) {
        for (OSMRelation relationToConvert : relations) {
            Attributes newAttributes = relationToConvert.attributes()
                    .keepOnlyKeys(POLYGON_ATTRIBUTES);
            if ("multipolygon".equals(relationToConvert.attributeValue("type"))
                    && !newAttributes.isEmpty()) {
                for (Attributed<Polygon> polygon : assemblePolygon(
                        relationToConvert, newAttributes)) {
                    mapToBe.addPolygon(polygon);
                }
            }
        }
    }

    private void waysConversion(List<OSMWay> ways) {
        for (OSMWay wayToConvert : ways) {
            Attributes newAttributes = filteredAttributes(wayToConvert);
            if (!(OSMWayIsASurface(wayToConvert) || newAttributes.isEmpty())) {
                mapToBe.addPolyLine(new Attributed<>(
                        OSMWayToPolyLine(wayToConvert), newAttributes));
            } else if (wayToConvert.isClosed()
                    && OSMWayIsASurface(wayToConvert)
                    && !newAttributes.isEmpty()) {
                mapToBe.addPolygon(new Attributed<>(new Polygon(
                        OSMWayToClosedPolyLine(wayToConvert)), newAttributes));
            }
        }
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
            if (role.equals(m.role())
                    && m.type() == OSMRelation.Member.Type.WAY) {
                roleWays.add(OSMWayToPolyLine((OSMWay) m.member()));
            }
        }

        if (roleWays.isEmpty()) {
            return Collections.emptyList();
        }

        Graph<Point> nonOrientedGraph = graphCreator(roleWays);

        if (!everyNodeHasTwoNeighbors(nonOrientedGraph)) {
            return Collections.emptyList();
        }

        List<ClosedPolyLine> ringsList = new ArrayList<>();
        Set<Point> nonVisitedNodes = new HashSet<>(nonOrientedGraph.nodes());

        while (!nonVisitedNodes.isEmpty()) {
            PolyLine.Builder polylineInConstruction = new PolyLine.Builder();
            theRingMaker(nonOrientedGraph, polylineInConstruction,
                    nonVisitedNodes, nonVisitedNodes.iterator().next());
            ringsList.add(polylineInConstruction.buildClosed());
        }

        return ringsList;
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

        if (outerRings.isEmpty()) {
            return Collections.emptyList();
        }
        // tester si outerRings est vide?
        outerRings.sort((ring1, ring2) -> (int) Math.signum(ring1.area()
                - ring2.area()));

        List<Attributed<Polygon>> relationPolygons = new ArrayList<>();

        for (ClosedPolyLine outerRing : outerRings) {
            List<ClosedPolyLine> attachedInnerRings = new ArrayList<>();
            for (Iterator<ClosedPolyLine> iterator = innerRings.iterator(); iterator
                    .hasNext();) {
                ClosedPolyLine nextPolyLine = iterator.next();
                if (outerRing.containsPoint(nextPolyLine.firstPoint())) {
                    attachedInnerRings.add(nextPolyLine);
                    iterator.remove();
                }
            }
            if (attachedInnerRings.isEmpty()) {
                relationPolygons.add(new Attributed<>(new Polygon(outerRing),
                        attributes));
            } else {
                relationPolygons.add(new Attributed<>(new Polygon(outerRing,
                        attachedInnerRings), attributes));
            }
        }

        return relationPolygons;
    }

    /**
     * Méthode récursive ajoutant tous les noeuds d'un anneau à la polyligne en
     * construction
     * 
     * @param nonOrientedGraph
     * @param polylineInConstruction
     * @param nonVisitedNodes
     * @param currentPoint
     */
    private void theRingMaker(Graph<Point> nonOrientedGraph,
            PolyLine.Builder polylineInConstruction,
            Set<Point> nonVisitedNodes, Point currentPoint) {
        Set<Point> neighbors = new HashSet<>(
                nonOrientedGraph.neighborsOf(currentPoint));
        neighbors.retainAll(nonVisitedNodes);

        if (neighbors.isEmpty()) {
            return;
        } else {
            polylineInConstruction.addPoint(currentPoint);
            nonVisitedNodes.remove(currentPoint);
            theRingMaker(nonOrientedGraph, polylineInConstruction,
                    nonVisitedNodes, neighbors.iterator().next());
        }
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
    // erreurs
    private Graph<Point> graphCreator(List<PolyLine> roleWays) {
        Graph.Builder<Point> graphInConstruction = new Graph.Builder<>();

        for (PolyLine polyline : roleWays) {
            List<Point> pointList = polyline.points();

            for (int i = 0; i < pointList.size(); ++i) {
                graphInConstruction.addNode(pointList.get(i));
                if (i > 0) {
                    graphInConstruction.addEdge(pointList.get(i), pointList.get(i - 1));
                }
            }
            /*for (ListIterator<Point> iterator = pointList.listIterator(); iterator
                    .hasNext();) {
                Point nextPoint = iterator.next();
                graphInConstruction.addNode(nextPoint);
                if (iterator.hasPrevious()) {
                    graphInConstruction.addEdge(nextPoint, iterator.previous());
                }
            }*/
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
