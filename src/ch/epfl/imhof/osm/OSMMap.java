package ch.epfl.imhof.osm;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

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
     * Construit une nouvelle carte OSM avec les chemins et les relations
     * fournis.
     * 
     * @param ways
     *            les chemins de la carte, une liste de <code>OSMWay</code>
     * @param relations
     *            les relations de la carte, une liste de
     *            <code>OSMRelation</code>
     */
    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations) {
        this.ways = Collections.unmodifiableList(new ArrayList<>(ways));
        this.relations = Collections
                .unmodifiableList(new ArrayList<>(relations));
    }

    /**
     * Retourne la liste des chemins de la carte.
     * 
     * @return les chemins de la carte, sous forme de <code>List</code>
     */
    public List<OSMWay> ways() {
        return ways;
    }

    /**
     * Retourne la liste des relations de la carte.
     * 
     * @return les relations de la carte, sous forme de <code>List</code>
     */
    public List<OSMRelation> relations() {
        return relations;
    }

    /**
     * Bâtisseur de la classe <code>OSMMap</code>.
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static final class Builder {
        private final Map<Long, OSMNode> nodes;
        private final Map<Long, OSMWay> ways;
        private final Map<Long, OSMRelation> relations;

        /**
         * Constructeur par défaut du bâtisseur.
         */
        public Builder() {
            nodes = new HashMap<>();
            ways = new HashMap<>();
            relations = new HashMap<>();
        }

        /**
         * Ajoute le noeud donné au bâtisseur.
         * 
         * @param newNode
         *            le noeud qu'on souhaite ajouter à la carte en construction
         */
        public void addNode(OSMNode newNode) {
            nodes.put(newNode.id(), newNode);
        }

        /**
         * Retourne le noeud associé à l'identifiant passé en paramètre, ou
         * <code>null</code> si le noeud n'a pas été ajouté au bâtisseur.
         * 
         * @param id
         *            l'identifiant du noeud qu'on recherche
         * @return le noeud recherché s'il a été ajouté au bâtisseur,
         *         <code>null</code> dans le cas contraire.
         */
        public OSMNode nodeForId(long id) {
            return nodes.get(id);
        }

        /**
         * Ajoute le chemin donné au bâtisseur.
         * 
         * @param newWay
         *            le chemin qu'on souhaite ajouter à la carte en
         *            construction
         */
        public void addWay(OSMWay newWay) {
            ways.put(newWay.id(), newWay);
        }

        /**
         * Retourne le chemin associé à l'identifiant passé en paramètre, ou
         * <code>null</code> si le chemin n'a pas été ajouté au bâtisseur.
         * 
         * @param id
         *            l'identifiant du chemin recherché
         * @return le chemin recherché s'il a été ajouté au bâtisseur,
         *         <code>null</code> sinon
         */
        public OSMWay wayForId(long id) {
            return ways.get(id);
        }

        /**
         * Ajoute la relation donnée au bâtisseur.
         * 
         * @param newRelation
         *            la relation qu'on souhaite ajouter à la carte en
         *            construction
         */
        public void addRelation(OSMRelation newRelation) {
            relations.put(newRelation.id(), newRelation);
        }

        /**
         * Retourne la relation associée à l'identifiant passé en paramètre, ou
         * <code>null</code> si la relation n'a pas été ajoutée au bâtisseur.
         * 
         * @param id
         *            l'identifiant de la relation recherchée
         * @return la relation recherchée si elle a été ajoutée au bâtisseur,
         *         <code>null</code> sinon
         */
        public OSMRelation relationForId(long id) {
            return relations.get(id);
        }

        /**
         * Construit et retourne une carte OSM avec les chemins et les relations
         * ajoutés jusqu'à présent au bâtisseur.
         * 
         * @return un nouvel objet de type <code>OSMMap</code>
         */
        public OSMMap build() {
            return new OSMMap(ways.values(), relations.values());
        }
    }
}
