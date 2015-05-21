package ch.epfl.imhof.osm;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import ch.epfl.imhof.Attributes;

/**
 * Classe représentant un chemin OSM, héritant de
 * {@link ch.epfl.imhof.osm.OSMEntity OSMEntity}. Elle est immuable.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 */
public final class OSMWay extends OSMEntity {
    private final List<OSMNode> nodes;

    /**
     * Construit un chemin avec l'identifiant, les noeuds et les attributs
     * donnés
     * 
     * @param id
     *            l'identifiant du chemin
     * @param nodes
     *            la liste des noeuds du chemin
     * @param attributes
     *            les attributs du chemin
     * @throws IllegalArgumentException
     *             lève une exception si la liste de noeuds possède moins de
     *             deux éléments.
     */
    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes)
            throws IllegalArgumentException {
        super(id, attributes);
        if (nodes.size() < 2) {
            throw new IllegalArgumentException(
                    "La liste de nœuds possède moins de deux éléments.");
        }
        this.nodes = Collections.unmodifiableList(new ArrayList<>(nodes));
    }

    /**
     * Retourne le nombre de noeuds du chemin.
     * 
     * @return le nombre de noeuds
     */
    public int nodesCount() {
        return nodes.size();
    }

    /**
     * Retourne la liste des noeuds du chemin
     * 
     * @return les noeuds du chemin, sous forme de {@link java.util.List List}
     */
    public List<OSMNode> nodes() {
        return nodes;
    }

    /**
     * Retourne la liste des noeuds du chemin sans le dernier si celui-ci est
     * identique au premier.
     * 
     * @return les noeuds, sans répétition, sous forme de {@link java.util.List
     *         List}
     */
    public List<OSMNode> nonRepeatingNodes() {
        if (isClosed()) {
            return nodes.subList(0, nodesCount() - 1);
        } else {
            return nodes;
        }
    }

    /**
     * Retourne le premier noeud du chemin.
     * 
     * @return le premier noeud
     */
    public OSMNode firstNode() {
        return nodes.get(0);
    }

    /**
     * Retourne le dernier noeud du chemin.
     * 
     * @return le dernier noeud
     */
    public OSMNode lastNode() {
        return nodes.get(nodesCount() - 1);
    }

    /**
     * Retourne vrai si et seulement si le chemin est fermé (le 1er noeud est
     * identique au dernier).
     * 
     * @return <code>true</code> si le chemin est fermé, <code>false</code>
     *         sinon
     */
    public boolean isClosed() {
        return firstNode().equals(lastNode());
    }

    /**
     * Bâtisseur de la classe {@link ch.epfl.imhof.osm.OSMWay OSMWay}. Il hérite
     * du bâtisseur de la classe {@link ch.epfl.imhof.osm.OSMEntity OSMEntity}.
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static final class Builder extends OSMEntity.Builder {
        private final List<OSMNode> nodes;

        /**
         * Construit un bâtisseur pour un chemin ayant l'identifiant donné.
         * 
         * @param id
         *            l'identifiant du chemin
         */
        public Builder(long id) {
            super(id);
            nodes = new ArrayList<>();
        }

        /**
         * Ajoute un noeud au chemin en cours de construction.
         * 
         * @param newNode
         *            le noeud qu'on souhaite ajouter au chemin
         */
        public void addNode(OSMNode newNode) {
            nodes.add(newNode);
        }

        /**
         * Rédéfinition de la méthode
         * {@link ch.epfl.imhof.osm.OSMEntity.Builder#isIncomplete()
         * isIncomplete} de la superclasse, un chemin possédant moins de deux
         * noeuds est également incomplet.
         */
        @Override
        public boolean isIncomplete() {
            return (nodes.size() < 2 || super.isIncomplete());
        }

        /**
         * Construit et retourne le chemin ayant les noeuds et les attributs
         * ajoutés au bâtisseur.
         * 
         * @return le chemin
         * @throws IllegalStateException
         *             lève une exception si le chemin est incomplet
         */
        public OSMWay build() throws IllegalStateException {
            if (isIncomplete()) {
                throw new IllegalStateException(
                        "Le chemin en cours de construction est incomplet.");
            }
            return new OSMWay(this.id, nodes, this.attributesBuilder.build());
        }
    }
}
