package ch.epfl.imhof;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Classe représentant un graphe non-orienté. Elle est immuable et générique,
 * son paramètre représente le type des noeuds du graphe.
 * 
 * @author Vincenzo Bazzucchi (249733)
 * @author Nicolas Phan Van (239293)
 *
 * @param <N>
 *            le type des noeuds du graphe
 */
public final class Graph<N> {
    private final Map<N, Set<N>> neighbors;

    /**
     * Construit un nouveau graphe à partir de la table d'adjacence passée en
     * paramètre
     * 
     * @param neighbors
     *            la table d'adjacence du graphe, une Map
     */
    public Graph(Map<N, Set<N>> neighbors) {
        Map<N, Set<N>> temp = new HashMap<>();
        for (Map.Entry<N, Set<N>> m : neighbors.entrySet()) {
            temp.put(m.getKey(),
                    Collections.unmodifiableSet(new HashSet<>(m.getValue())));
        }
        this.neighbors = Collections.unmodifiableMap(temp);
    }

    /**
     * Retourne l'ensemble des noeuds du graphe
     * 
     * @return les noeuds du graphe, sous forme de Set
     */
    public Set<N> nodes() {
        return neighbors.keySet();
    }

    /**
     * Retourne l'ensemble des noeuds voisins du noeud donné
     * 
     * @param node
     *            le noeud dont on cherche les voisins
     * @return les voisins du noeud, sous forme de Set
     * @throws IllegalArgumentException
     *             lève une exception si le noeud passé en paramètre ne fait pas
     *             partie du graphe
     */
    public Set<N> neighborsOf(N node) throws IllegalArgumentException {
        if (!neighbors.containsKey(node)) {
            throw new IllegalArgumentException(
                    "Le nœud donné ne fait pas partie du graphe.");
        }
        return neighbors.get(node);
    }

    /**
     * Bâtisseur de la classe Graph
     * 
     * @author Vincenzo Bazzucchi (249733)
     * @author Nicolas Phan Van (239293)
     *
     * @param <N>
     *            le type des noeuds du graphe en construction
     */
    public static final class Builder<N> {
        private Map<N, Set<N>> neighbors;

        /**
         * Constructeur par défaut du bâtisseur
         */
        public Builder() {
            neighbors = new HashMap<>();
        }

        /**
         * Ajoute le noeud donné au graphe en cours de construction, s'il n'en
         * fait pas déjà partie
         * 
         * @param n
         *            le noeud qu'on souhaite ajouter au graphe
         */
        public void addNode(N n) {
            if (!neighbors.containsKey(n)) {
                neighbors.put(n, new HashSet<>());
            }
        }

        /**
         * Ajoute une arête entre les deux noeuds donnés au graphe en cours de
         * construction
         * 
         * @param n1
         *            le premier noeud de l'arête
         * @param n2
         *            le second noeud de l'arête
         * @throws IllegalArgumentException
         *             lève une exception si un des noeuds, ou les deux,
         *             n'appartient pas au graphe en construction
         */
        public void addEdge(N n1, N n2) throws IllegalArgumentException {
            if (!(neighbors.containsKey(n1) && neighbors.containsKey(n2))) {
                throw new IllegalArgumentException(
                        "Au moins un des deux nœuds n'appartient pas au graphe en cours de construction");
            }
            neighbors.get(n1).add(n2);
            neighbors.get(n2).add(n1);
        }

        /**
         * Construit le graphe composé des noeuds et arêtes ajoutés jusqu'à
         * présent au bâtisseur
         * 
         * @return le nouveau graphe
         */
        public Graph<N> build() {
            return new Graph<>(neighbors);
        }
    }
}
