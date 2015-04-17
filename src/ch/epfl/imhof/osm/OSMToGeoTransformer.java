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
    // Ensemble contenant les attributs qualifiant une surface
    private final static Set<String> SURFACE_ATTRIBUTES = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList(new String[] {
                    "aeroway", "amenity", "building", "harbour", "historic",
                    "landuse", "leisure", "man_made", "military", "natural",
                    "office", "place", "power", "public_transport", "shop",
                    "sport", "tourism", "water", "waterway", "wetland" })));
    // Ensemble contenant les attributs à garder pour une polyligne
    private final static Set<String> POLYLINE_ATTRIBUTES = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList(new String[] {
                    "bridge", "highway", "layer", "man_made", "railway",
                    "tunnel", "waterway" })));
    // Ensemble contenant les attributs à garder pour un polygone
    private final static Set<String> POLYGON_ATTRIBUTES = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList(new String[] {
                    "building", "landuse", "layer", "leisure", "natural",
                    "waterway" })));

    private final Projection projection;
    private final Map.Builder mapBuilder;

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
        mapBuilder = new Map.Builder();
    }

    /**
     * Convertit une carte OSM en une carte géométrique projetée.
     * 
     * @param map
     *            la carte qu'on veut convertir, une <code>OSMMap</code>
     * @return la carte projetée, une <code>Map</code>
     */
    public Map transform(OSMMap map) {
        waysConversion(map.ways());
        relationsConversion(map.relations());

        return mapBuilder.build();
    }

    /**
     * Convertit les chemins de la carte OSM en entités géométriques attribuées
     * et les ajoute à la carte en construction.
     * 
     * @param ways
     *            la liste des chemins de la carte OSM à convertir
     */
    private void waysConversion(List<OSMWay> ways) {
        // Parcours des chemins de la carte OSM
        for (OSMWay wayToConvert : ways) {
            boolean wayIsAPolygon = isAPolygon(wayToConvert);
            Attributes filteredAttributes = filteredAttributes(wayToConvert,
                    wayIsAPolygon);

            // Teste si le chemin doit être converti en polygone, polyligne, ou
            // ignoré, et ajoute l'entité correspondante à la carte en
            // construction
            if (wayIsAPolygon && !filteredAttributes.isEmpty()) {
                mapBuilder.addPolygon(new Attributed<>(new Polygon(
                        OSMWayToClosedPolyLine(wayToConvert)),
                        filteredAttributes));
            } else if (!wayIsAPolygon && !filteredAttributes.isEmpty()) {
                mapBuilder.addPolyLine(new Attributed<>(
                        OSMWayToPolyLine(wayToConvert), filteredAttributes));
            }
        }
    }

    /**
     * Convertit les relations de la carte OSM en polygones attribués et les
     * ajoute à la carte en construction.
     * 
     * @param relations
     *            la liste des relations de la carte OSM à convertir
     */
    private void relationsConversion(List<OSMRelation> relations) {
        // Parcours des relations de la carte OSM
        for (OSMRelation relationToConvert : relations) {
            Attributes filteredAttributes = relationToConvert.attributes()
                    .keepOnlyKeys(POLYGON_ATTRIBUTES);

            // Teste si la relation doit être convertie en polygone, si oui
            // appelle la méthode assemblePolygon
            if ("multipolygon".equals(relationToConvert.attributeValue("type"))
                    && !filteredAttributes.isEmpty()) {
                // Parcours de la liste de polygones attribués retournée par
                // assemblePolygon, et ajout au constructeur de la carte
                for (Attributed<Polygon> polygon : assemblePolygon(
                        relationToConvert, filteredAttributes)) {
                    mapBuilder.addPolygon(polygon);
                }
            }
        }
    }

    /**
     * Retourne la liste des polygones attribués extraits de la relation donnée,
     * en leur attachant les attributs donnés.
     * 
     * @param relation
     *            la relation dont on extrait les polygones
     * @param attributes
     *            les attributs à attacher aux polygones
     * @return une liste de polygones attribués
     */
    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation,
            Attributes attributes) {
        List<ClosedPolyLine> innerRings = ringsForRole(relation, "inner");
        List<ClosedPolyLine> outerRings = ringsForRole(relation, "outer");

        // Retourne une liste vide si le polygone ne possède pas d'anneau
        // extérieur, donc pas d'enveloppe.
        if (outerRings.isEmpty()) {
            return Collections.emptyList();
        }

        // Tri de la liste des anneaux extérieurs en fonction de leur aire,
        // utilisant une fonction anonyme redéfinissant un comparateur.
        outerRings.sort((ring1, ring2) -> Double.compare(ring1.area(),
                ring2.area()));

        List<Attributed<Polygon>> relationPolygons = new ArrayList<>();

        // Parcours des anneaux extérieurs
        for (ClosedPolyLine outerRing : outerRings) {
            List<ClosedPolyLine> attachedInnerRings = new ArrayList<>();

            // Parcours des anneaux intérieurs
            for (Iterator<ClosedPolyLine> iterator = innerRings.iterator(); iterator
                    .hasNext();) {
                ClosedPolyLine potentialRing = iterator.next();

                // Ajout de l'anneau intérieur courant à la liste des trous s'il
                // est contenu dans l'anneau extérieur courant, et suppression
                // de celui-ci de la liste des anneaux intérieurs le cas
                // échéant.
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
     * Retourne l'ensemble des anneaux de la relation donnée ayant le rôle
     * spécifié.
     * 
     * @param relation
     *            la relation dont on cherche à extraire les anneaux
     * @param role
     *            le rôle des anneaux, outer ou inner
     * @return les anneaux de la relation correspondant au rôle donné, sous
     *         forme de <code>List</code>
     */
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {
        List<OSMWay> filteredWays = filterMembers(relation.members(), role);
        Graph<OSMNode> nonOrientedGraph = graphCreator(filteredWays);

        // Retourne une liste vide si tous les noeuds du graphe n'ont pas
        // exactement deux voisins
        if (!everyNodeHasTwoNeighbors(nonOrientedGraph)) {
            return Collections.emptyList();
        }

        List<ClosedPolyLine> ringsList = new ArrayList<>();
        Set<OSMNode> nonVisitedNodes = new HashSet<>(nonOrientedGraph.nodes());

        // Parcours de l'ensemble des noeuds non visités du graphe, tant qu'il
        // en reste au moins un
        while (!nonVisitedNodes.isEmpty()) {
            PolyLine.Builder polylineInConstruction = new PolyLine.Builder();
            Set<OSMNode> neighbors;
            OSMNode currentNode = nonVisitedNodes.iterator().next();

            /*
             * Parcours de l'anneau formé par le noeud initial et ses voisins.
             * Pour chaque boucle, on ajoute le noeud courant à la polyligne en
             * construction, on le retire de l'ensemble des noeuds non-visités,
             * et on construit un ensemble qui est l'intersection de l'ensemble
             * des voisins du noeud courant et de l'ensemble des noeuds
             * non-visités. Si cet ensemble est non-vide, le prochain noeud est
             * un de ses éléments. Sinon la boucle se termine.
             */
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
     * ayant le rôle spécifié.
     * 
     * @param members
     *            la liste des membres de la relation dont on cherche à extraire
     *            les chemins
     * @param role
     *            le rôle des chemins, outer ou inner
     * @return les chemins ayant le rôle donné
     */
    private List<OSMWay> filterMembers(List<Member> members, String role) {
        List<OSMWay> filteredWays = new ArrayList<>();

        for (Member member : members) {
            if (role.equals(member.role())) {
                filteredWays.add((OSMWay) member.member());
            }
        }
        return filteredWays;
    }

    /**
     * Construit un graphe non-orienté à partir des noeuds des chemins donnés.
     * Ajoute une arête entre deux noeuds s'ils se suivent dans le même chemin.
     * 
     * @param ways
     *            la liste des chemins dont les noeuds vont former le graphe
     * @return le graphe non-orienté
     */
    private Graph<OSMNode> graphCreator(List<OSMWay> ways) {
        Graph.Builder<OSMNode> graphBuilder = new Graph.Builder<>();

        for (OSMWay way : ways) {
            List<OSMNode> nodes = way.nodes();
            graphBuilder.addNode(nodes.get(0));

            for (int i = 1; i < nodes.size(); ++i) {
                graphBuilder.addNode(nodes.get(i));
                graphBuilder.addEdge(nodes.get(i), nodes.get(i - 1));
            }
        }
        return graphBuilder.build();
    }

    /**
     * Retourne vrai si tous les noeuds du graphe donné possèdent exactement
     * deux voisins.
     * 
     * @param nonOrientedGraph
     *            le graphe à tester
     * @return <code>true</code> si tous les noeuds du graphe ont deux voisins,
     *         <code>false</code> sinon
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
     * Retourne une version filtrée des attributs du chemin donné. Utilise les
     * ensembles d'attributs à garder stockés de façon statique dans la classe.
     * Détermine si le chemin est un polygone ou une polyligne grâce au booléen
     * passé en paramètre.
     * 
     * @param way
     *            le chemin dont on veut filtrer les attributs
     * @param isAPolygon
     *            <code>true</code> si le chemin forme un polygone,
     *            <code>false</code> sinon
     * @return l'intersection des attributs du chemin et des attributs à garder
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
     *            le chemin à convertir
     * @return une polyligne
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
     *            le chemin à convertir
     * @return une polyligne ouverte
     */
    private OpenPolyLine OSMWayToOpenPolyLine(OSMWay way) {
        PolyLine.Builder openPolylineBuilder = new PolyLine.Builder();

        for (OSMNode nodeToProject : way.nodes()) {
            openPolylineBuilder.addPoint(projection.project(nodeToProject
                    .position()));
        }
        return openPolylineBuilder.buildOpen();
    }

    /**
     * Convertit le chemin donné en polyligne fermée.
     * 
     * @param way
     *            le chemin à convertir
     * @return une polyligne fermée
     */
    private ClosedPolyLine OSMWayToClosedPolyLine(OSMWay way) {
        PolyLine.Builder closedPolyLineBuilder = new PolyLine.Builder();

        for (OSMNode nodeToProject : way.nonRepeatingNodes()) {
            closedPolyLineBuilder.addPoint(projection.project(nodeToProject
                    .position()));
        }
        return closedPolyLineBuilder.buildClosed();
    }

    /**
     * Retourne vrai si le chemin donné forme un polygone, c'est-à-dire s'il est
     * fermé et qu'il possède un attribut le qualifiant de surface.
     * 
     * @param way
     *            le chemin à tester
     * @return <code>true</code> si le chemin est un polygone,
     *         <code>false</code> sinon
     */
    private boolean isAPolygon(OSMWay way) {
        String areaValue = way.attributeValue("area");

        if ("yes".equals(areaValue) || "1".equals(areaValue)
                || "true".equals(areaValue)) {
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
