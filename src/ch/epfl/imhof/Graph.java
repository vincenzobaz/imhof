package ch.epfl.imhof;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public final class Graph<N> {
    private final Map<N, Set<N>> neighbors;

    public Graph(Map<N, Set<N>> neighbors) {
        this.neighbors = Collections.unmodifiableMap(new HashMap<>(neighbors));
    }

    public Set<N> nodes() {
        return neighbors.keySet();
    }

    public Set<N> neighborsOf(N node) throws IllegalArgumentException {
        if (!neighbors.containsKey(node)) {
            throw new IllegalArgumentException(
                    "Le nœud donné ne fait pas partie du graphe.");
        }
        return neighbors.get(node);
    }

    public static final class Builder<N> {
        private Map<N, Set<N>> neighbors;

        public Builder() {
            this.neighbors = new HashMap<>();
        }

        public void addNode(N n) {
            if (!neighbors.containsKey(n)) {
                neighbors.put(n, null);
            }
        }

        public void addEdge(N n1, N n2) throws IllegalArgumentException {
            if (!(neighbors.containsKey(n1) && neighbors.containsKey(n2))) {
                throw new IllegalArgumentException(
                        "Au moins un des deux nœuds n'appartient pas au graphe en cours de construction");
            }
            mixItUp(n1, n2);
            mixItUp(n2, n1);
        }
        
        private void mixItUp(N n1, N n2) {
            Set<N> temp;
            if (neighbors.get(n1) == null) {
                temp = new HashSet<>();
            } else {
                temp = new HashSet<>(neighbors.get(n1));
            }
            temp.add(n2);
            neighbors.put(n1, temp);
        }
        
        public Graph<N> build() {
            return new Graph<>(neighbors);
        }
    }
}
