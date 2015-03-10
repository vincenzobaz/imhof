package ch.epfl.imhof;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public final class Graph<N> {
    private final Map<N, Set<N>> neighbors;

    public Graph(Map<N, Set<N>> neighbors) {
        Map<N, Set<N>> temp = new HashMap<>();
        for (Map.Entry<N, Set<N>> m : neighbors.entrySet()) {
            temp.put(m.getKey(),
                    Collections.unmodifiableSet(new HashSet<>(m.getValue())));
        }
        this.neighbors = Collections.unmodifiableMap(new HashMap<>(temp));
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
            neighbors = new HashMap<>();
        }

        public void addNode(N n) {
            if (!neighbors.containsKey(n)) {
                neighbors.put(n, new HashSet<>());
            }
        }

        public void addEdge(N n1, N n2) throws IllegalArgumentException {
            if (!(neighbors.containsKey(n1) && neighbors.containsKey(n2))) {
                throw new IllegalArgumentException(
                        "Au moins un des deux nœuds n'appartient pas au graphe en cours de construction");
            }
            neighbors.get(n1).add(n2);
            neighbors.get(n2).add(n1);
        }

        public Graph<N> build() {
            return new Graph<>(neighbors);
        }
    }
}
