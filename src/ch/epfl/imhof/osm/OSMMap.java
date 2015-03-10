package ch.epfl.imhof.osm;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// un ensemble de chemins et relations PAS DE NOEUDS (il servent seulement  à construire des chemins

/**
 * Classe représentant une carte OpenStreetMap. Elle est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class OSMMap {
    private final List<OSMWay> ways;
    private final List<OSMRelation> relations;

    /**
     * Construit une nouvelle carte avec les chemins et les relations donnés
     * 
     * @param ways
     *            les chemins de la carte, une liste de OSMWay
     * @param relations
     *            les relations de la carte, une liste de OSMRelation
     */
    OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations) {
        this.ways = Collections.unmodifiableList(new ArrayList<>(ways));
        this.relations = Collections
                .unmodifiableList(new ArrayList<>(relations));
    }

    /**
     * Accesseur des chemins de la carte
     * 
     * @return la liste des chemins de la carte
     */
    public List<OSMWay> ways() {
        return ways;
    }

    /**
     * Accesseur des relations de la carte
     * 
     * @return la liste des relations de la carte
     */
    public List<OSMRelation> relations() {
        return relations;
    }

    /**
     * Bâtisseur de la classe OSMMap
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static final class Builder {
        private Map<Long, OSMNode> nodes;
        private Map<Long, OSMWay> ways;
        private Map<Long, OSMRelation> relations;

        /**
         * Constructeur par défaut du bâtisseur
         */
        public Builder() {
            nodes = new HashMap<>();
            ways = new HashMap<>();
            relations = new HashMap<>();
        }

        /**
         * Ajoute le noeud donné au bâtisseur
         * 
         * @param newNode
         *            le noeud qu'on souhaite ajouter au bâtisseur
         */
        public void addNode(OSMNode newNode) {
            nodes.put(newNode.id(), newNode);
        }

        /**
         * Retourne le noeud associé à l'identifiant passé en paramètre, ou null
         * si le noeud n'a pas été ajouté au bâtisseur
         * 
         * @param id
         *            l'identifiant du noeud qu'on recherche
         * @return le noeud recherché s'il a été ajouté au bâtisseur, null sinon
         */
        public OSMNode nodeForId(long id) {
            return nodes.get(id);
        }

        /**
         * Ajoute le chemin donné à la carte en cours de construction
         * 
         * @param newWay
         *            le chemin qu'on souhaite ajouter à la carte
         */
        public void addWay(OSMWay newWay) {
            ways.put(newWay.id(), newWay);
        }

        /**
         * Retourne le chemin associé à l'identifiant passé en paramètre, ou
         * null si le chemin n'a pas été ajouté au bâtisseur
         * 
         * @param id
         *            l'identifiant du chemin recherché
         * @return le chemin recherché s'il a été ajouté à la carte en
         *         construction, null sinon
         */
        public OSMWay wayForId(long id) {
            return ways.get(id);
        }

        /**
         * Ajoute la relation donnée à la carte en cours de construction
         * 
         * @param newRelation
         *            la relation qu'on souhaite ajouter à la carte
         */
        public void addRelation(OSMRelation newRelation) {
            relations.put(newRelation.id(), newRelation);
        }

        /**
         * Retourne la relation associée à l'identifiant passé en paramètre, ou
         * null si la relation n'a pas été ajoutée au bâtisseur
         * 
         * @param id
         *            l'identifiant de la relation recherchée
         * @return la relation recherchée si elle a été ajoutée à la carte en
         *         construction, null sinon
         */
        public OSMRelation relationForId(long id) {
            return relations.get(id);
        }

        /**
         * Construit une carte OSM avec les chemins et les relations ajoutés
         * jusqu'à présent
         * 
         * @return la nouvelle carte
         */
        public OSMMap build() {
            return new OSMMap(ways.values(), relations.values());
        }
    }
}
