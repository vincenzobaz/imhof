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
 * Classe représentant un convertisseur de données OSM en carte géométrique.
 * Elle est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class OSMToGeoTransformer {
    public static final Set<String> SURFACE_ATTRIBUTES = new HashSet<>(
            Arrays.asList(new String[] { "aeroway", "amenity", "building",
                    "harbour", "historic", "landuse", "leisure", "man_made",
                    "military", "natural", "office", "place", "power",
                    "public_transport", "shop", "sport", "tourism", "water",
                    "waterway", "wetland" }));
    public static final Set<String> POLYLINE_ATTRIBUTES = new HashSet<>(
            Arrays.asList(new String[] { "bridge", "highway", "layer",
                    "man_made", "railway", "tunnel", "waterway" }));
    public static final Set<String> POLYGON_ATTRIBUTES = new HashSet<>(
            Arrays.asList(new String[] { "building", "landuse", "layer",
                    "leisure", "natural", "waterway" }));

    private final Projection projection;
    private final Map.Builder mapToBe;

    /**
     * Construit un convertisseur OSM en géométrie qui utilise la projection
     * donnée.
     * 
     * @param projection
     *            la projection à utiliser pour les conversions d'entités OSM en
     *            entités géométriques
     */
    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
        mapToBe = new Map.Builder();
    }

    /**
     * Convertit une carte OSM en une carte géométrique projetée.
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

    /**
     * Convertit les relations de la carte OSM en polygones et les ajoute à la
     * carte en construction.
     * 
     * @param relations
     */
    private void relationsConversion(List<OSMRelation> relations) {
        for (OSMRelation relationToConvert : relations) {
            Attributes filteredAttributes = relationToConvert.attributes()
                    .keepOnlyKeys(POLYGON_ATTRIBUTES);

            if ("multipolygon".equals(relationToConvert.attributeValue("type"))
                    && !filteredAttributes.isEmpty()) {
                for (Attributed<Polygon> polygon : assemblePolygon(
                        relationToConvert, filteredAttributes)) {
                    mapToBe.addPolygon(polygon);
                }
            }
        }
    }

    /**
     * Convertit les chemins de la carte OSM en entités géométriques attribuées
     * et les ajoute à la carte en construction.
     * 
     * @param ways
     */
    private void waysConversion(List<OSMWay> ways) {
        for (OSMWay wayToConvert : ways) {
            boolean wayToConvertIsAPolygon = isAPolygon(wayToConvert);
            Attributes filteredAttributes = filteredAttributes(wayToConvert,
                    wayToConvertIsAPolygon);

            if (wayToConvertIsAPolygon && !filteredAttributes.isEmpty()) {
                mapToBe.addPolygon(new Attributed<>(new Polygon(
                        OSMWayToClosedPolyLine(wayToConvert)),
                        filteredAttributes));
            } else if (!wayToConvertIsAPolygon && !filteredAttributes.isEmpty()) {
                mapToBe.addPolyLine(new Attributed<>(
                        OSMWayToPolyLine(wayToConvert), filteredAttributes));
            }
        }
    }

    /**
     * Retourne l'ensemble des anneaux de la relation donnée ayant le rôle
     * spécifié.
     * 
     * @param relation
     * @param role
     * @return
     */
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {
        List<OSMWay> filteredWays = filterMembers(relation.members(), role);
        Graph<OSMNode> nonOrientedGraph = graphCreator(filteredWays);

        if (!everyNodeHasTwoNeighbors(nonOrientedGraph)) {
            return Collections.emptyList();
        }

        List<ClosedPolyLine> ringsList = new ArrayList<>();
        Set<OSMNode> nonVisitedNodes = new HashSet<>(nonOrientedGraph.nodes());

        while (!nonVisitedNodes.isEmpty()) {
            PolyLine.Builder polylineInConstruction = new PolyLine.Builder();
            Set<OSMNode> neighbors;
            OSMNode currentNode = nonVisitedNodes.iterator().next();

            do {
                polylineInConstruction.addPoint(projection.project(currentNode
                        .position()));
                nonVisitedNodes.remove(currentNode);
                neighbors = new HashSet<>(
                        nonOrientedGraph.neighborsOf(currentNode));
                neighbors.retainAll(nonVisitedNodes);

                if (!neighbors.isEmpty()) {
                    currentNode = neighbors.iterator().next();
                }
            } while (!neighbors.isEmpty());

            ringsList.add(polylineInConstruction.buildClosed());
        }

        return ringsList;
    }

    /**
     * Retourne une liste contenant uniquement les chemins de la relation donnée
     * ayant le role donné.
     * 
     * @param members
     * @param role
     * @return
     */
    private List<OSMWay> filterMembers(List<Member> members, String role) {
        List<OSMWay> filteredWays = new ArrayList<>();

        for (Member m : members) {
            if (role.equals(m.role())) {
                filteredWays.add((OSMWay) m.member());
            }
        }
        return filteredWays;
    }

    /**
     * Retourne la liste des polygones attribués de la relation donnée, en leur
     * attachant les attributs donnés.
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

        outerRings.sort((ring1, ring2) -> Double.compare(ring1.area(),
                ring2.area()));

        List<Attributed<Polygon>> relationPolygons = new ArrayList<>();

        for (ClosedPolyLine outerRing : outerRings) {
            List<ClosedPolyLine> attachedInnerRings = new ArrayList<>();

            for (Iterator<ClosedPolyLine> iterator = innerRings.iterator(); iterator
                    .hasNext();) {
                ClosedPolyLine potentialRing = iterator.next();

                if (outerRing.containsPoint(potentialRing.firstPoint())
                        && outerRing.area() > potentialRing.area()) {
                    attachedInnerRings.add(potentialRing);
                    iterator.remove();
                }
            }
            relationPolygons.add(new Attributed<>(new Polygon(outerRing,
                    attachedInnerRings), attributes));
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
    /*
     * private void theRingMaker(Graph<OSMNode> nonOrientedGraph,
     * PolyLine.Builder polylineInConstruction, Set<OSMNode> nonVisitedNodes,
     * OSMNode currentNode) {
     * 
     * polylineInConstruction.addPoint(projection.project(currentNode
     * .position()));
     * 
     * Set<OSMNode> neighbors = new HashSet<>(
     * nonOrientedGraph.neighborsOf(currentNode));
     * neighbors.retainAll(nonVisitedNodes);
     * 
     * nonVisitedNodes.remove(currentNode);
     * 
     * if (neighbors.isEmpty()) { return; } else {
     * theRingMaker(nonOrientedGraph, polylineInConstruction, nonVisitedNodes,
     * neighbors.iterator().next()); } }
     */

    /**
     * Retourne vrai si tous les noeuds du graphe donné possèdent exactement
     * deux voisins.
     * 
     * @param nonOrientedGraph
     * @return
     */
    private boolean everyNodeHasTwoNeighbors(Graph<OSMNode> nonOrientedGraph) {
        if (nonOrientedGraph.nodes().isEmpty()) {
            return false;
        } else {
            boolean everyNodeHasTwoNeighbors = true;

            Iterator<OSMNode> iterator = nonOrientedGraph.nodes().iterator();
            while (everyNodeHasTwoNeighbors && iterator.hasNext()) {
                everyNodeHasTwoNeighbors = (nonOrientedGraph.neighborsOf(
                        iterator.next()).size() == 2);
            }
            return everyNodeHasTwoNeighbors;
        }
    }

    /**
     * Construit un graphe non-orienté à partir de la liste de chemins donnée.
     * 
     * @param roleWays
     * @return
     */
    private Graph<OSMNode> graphCreator(List<OSMWay> roleWays) {
        Graph.Builder<OSMNode> graphInConstruction = new Graph.Builder<>();

        for (OSMWay way : roleWays) {
            List<OSMNode> nodes = way.nodes();
            graphInConstruction.addNode(nodes.get(0));

            for (int i = 1; i < nodes.size(); ++i) {
                graphInConstruction.addNode(nodes.get(i));
                graphInConstruction.addEdge(nodes.get(i), nodes.get(i - 1));
            }
        }
        return graphInConstruction.build();
    }

    /**
     * Retourne une version filtrée des attributs du chemin donné.
     * 
     * @param way
     * @return
     */
    private Attributes filteredAttributes(OSMWay way, boolean isAPolygon) {
        Attributes filteredAttributes;

        if (isAPolygon) {
            filteredAttributes = way.attributes().keepOnlyKeys(
                    POLYGON_ATTRIBUTES);
        } else {
            filteredAttributes = way.attributes().keepOnlyKeys(
                    POLYLINE_ATTRIBUTES);
        }
        return filteredAttributes;
    }

    /**
     * Convertit le chemin donné en polyligne, ouverte ou fermée selon que le
     * chemin est ouvert ou fermé.
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
     * Convertit le chemin donné en polyligne ouverte.
     * 
     * @param way
     * @return
     */
    private OpenPolyLine OSMWayToOpenPolyLine(OSMWay way) {
        PolyLine.Builder openPolylineInConstruction = new PolyLine.Builder();

        for (OSMNode nodeToProject : way.nodes()) {
            openPolylineInConstruction.addPoint(projection
                    .project(nodeToProject.position()));
        }
        return openPolylineInConstruction.buildOpen();
    }

    /**
     * Convertit le chemin donné en polyligne fermée.
     * 
     * @param way
     * @return
     */
    private ClosedPolyLine OSMWayToClosedPolyLine(OSMWay way) {
        PolyLine.Builder closedPolyLineInConstruction = new PolyLine.Builder();

        for (OSMNode nodeToProject : way.nonRepeatingNodes()) {
            closedPolyLineInConstruction.addPoint(projection
                    .project(nodeToProject.position()));
        }
        return closedPolyLineInConstruction.buildClosed();
    }

    /**
     * Retourne vrai si le chemin donné décrit un polygone.
     * 
     * @param way
     * @return
     */
    private boolean isAPolygon(OSMWay way) {
        String area = way.attributeValue("area");

        if ("yes".equals(area) || "1".equals(area) || "true".equals(area)) {
            return way.isClosed();
        } else {
            boolean hasSurfaceAttribute = false;

            Iterator<String> iterator = SURFACE_ATTRIBUTES.iterator();
            while (!hasSurfaceAttribute && iterator.hasNext()) {
                hasSurfaceAttribute = way.hasAttribute(iterator.next());
            }
            return (hasSurfaceAttribute && way.isClosed());
        }
    }
}
