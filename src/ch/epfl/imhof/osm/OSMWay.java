package ch.epfl.imhof.osm;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import ch.epfl.imhof.Attributes;

/**
 * Classe représentant un chemin OSM, héritant de OSMEntity. Elle est immuable.
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
     *            l'identifiant du chemin, un long
     * @param nodes
     *            les noeuds du chemin, une liste de OSMNode
     * @param attributes
     *            les attributs du chemin, un Attributes
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
     * @return le nombre de noeuds, un int
     */
    public int nodesCount() {
        return nodes.size();
    }

    /**
     * Retourne la liste des noeuds du chemin
     * 
     * @return les noeuds, sous forme de List
     */
    public List<OSMNode> nodes() {
        return nodes;
    }

    /**
     * Retourne la liste des noeuds du chemin sans le dernier si celui-ci est
     * identique au premier.
     * 
     * @return les noeuds, sans répétition, sous forme de List
     */
    public List<OSMNode> nonRepeatingNodes() {
        if (isClosed()) {
            List<OSMNode> temp = new ArrayList<>(nodes);
            nodes.remove(nodesCount() - 1);
            return temp;
        } else {
            return nodes;
        }
    }

    /**
     * Retourne le premier noeud du chemin.
     * 
     * @return le premier noeud, un OSMNode
     */
    public OSMNode firstNode() {
        return nodes.get(0);
    }

    /**
     * Retourne le dernier noeud du chemin.
     * 
     * @return le dernier noeud, un OSMNode
     */
    public OSMNode lastNode() {
        return nodes.get(nodesCount() - 1);
    }

    /**
     * Retourne vrai si et seulement si le chemin est fermé (le 1er noeud est
     * identique au dernier).
     * 
     * @return true si le chemin est fermé, false sinon
     */
    public boolean isClosed() {
        return (firstNode().position().latitude() == lastNode().position()
                .latitude() && firstNode().position().longitude() == lastNode()
                .position().longitude());
    }

    /**
     * Bâtisseur de la classe OSMWay
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     */
    public static final class Builder extends OSMEntity.Builder {
        private List<OSMNode> nodes;

        /**
         * Constructeur du bâtisseur
         * 
         * @param id
         *            l'identifiant du chemin, un long
         */
        public Builder(long id) {
            super(id);
            nodes = new ArrayList<>();
        }

        /**
         * Ajoute un noeud au chemin en cours de construction.
         * 
         * @param newNode
         *            le noeud qu'on souhaite ajouter au chemin, un OSMNode
         */
        public void addNode(OSMNode newNode) {
            nodes.add(newNode);
        }

        /**
         * Rédéfinition de la méthode isIncomplete, un chemin possédant moins de
         * deux noeuds est également incomplet.
         */
        @Override
        public boolean isIncomplete() {
            return (nodes.size() < 2 || super.isIncomplete());
        }

        /**
         * Construit et retourne le chemin ayant les noeuds et les attributs
         * ajoutés au bâtisseur.
         * 
         * @return le chemin, un OSMWay
         * @throws IllegalStateException
         *             lève une exception si le chemin est incomplet
         */
        public OSMWay build() throws IllegalStateException {
            if (isIncomplete()) {
                throw new IllegalStateException(
                        "Le chemin en cours de construction est incomplet.");
            }
            return new OSMWay(idBuild(), nodes, attributesBuild());
        }
    }
}
