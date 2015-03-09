package ch.epfl.imhof.osm;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// un ensemble de chemins et relations PAS DE NOEUDS (il servent seulement  à construire des chemins
public final class OSMMap {
    private final List<OSMWay> ways;
    private final List<OSMRelation> relations;

    OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations) {
        this.ways = Collections.unmodifiableList(new ArrayList<>(ways));
        this.relations = Collections
                .unmodifiableList(new ArrayList<>(relations));
    }

    public List<OSMWay> ways() {
        return ways;
    }

    public List<OSMRelation> relations() {
        return relations;
    }

    public static final class Builder {
        private Map<Long, OSMNode> nodes;
        private Map<Long, OSMWay> ways;
        private Map<Long, OSMRelation> relations;

        public Builder() {
            nodes = new HashMap<>();
            ways = new HashMap<>();
            relations = new HashMap<>();
        }

        public void addNode(OSMNode newNode) {
            nodes.put(newNode.id(), newNode);
        }

        public OSMNode nodeForId(long id) {
            return nodes.get(id);
        }

        public void addWay(OSMWay newWay) {
            ways.put(newWay.id(), newWay);
        }

        public OSMWay wayForId(long id) {
            return ways.get(id);
        }

        public void addRelation(OSMRelation newRelation) {
            relations.put(newRelation.id(), newRelation);
        }

        public OSMRelation relationForId(long id) {
            return relations.get(id);
        }

        public OSMMap build() {
            return new OSMMap(ways.values(), relations.values());
        }
    }
}
