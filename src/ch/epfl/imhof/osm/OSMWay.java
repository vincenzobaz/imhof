package ch.epfl.imhof.osm;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import ch.epfl.imhof.*;

public final class OSMWay extends OSMEntity {
    private final List<OSMNode> nodes;

    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes)
            throws IllegalArgumentException {
        super(id, attributes);
        if (nodes.size() < 2) {
            throw new IllegalArgumentException();
        }
        this.nodes = Collections.unmodifiableList(new ArrayList<>(nodes));
    }

    public int nodesCount() {
        return nodes.size();
    }

    public List<OSMNode> nodes() {
        return nodes;
    }

    public List<OSMNode> nonRepeatingNodes() {
        if (isClosed()) {
            List<OSMNode> temp = new ArrayList<>(nodes);
            nodes.remove(nodesCount() - 1);
            return temp;
        } else {
            return nodes;
        }
    }

    public OSMNode firstNode() {
        return nodes.get(0);
    }

    public OSMNode lastNode() {
        return nodes.get(nodesCount() - 1);
    }

    public boolean isClosed() {
        return (firstNode().position().latitude() == lastNode().position()
                .latitude() && firstNode().position().longitude() == lastNode()
                .position().longitude());
    }

    public static final class Builder extends OSMEntity.Builder {
        private List<OSMNode> nodes;

        public Builder(long id) {
            super(id);
            nodes = new ArrayList<OSMNode>();
        }

        public void addNode(OSMNode newNode) {
            nodes.add(newNode);
        }

        public boolean isIncomplete() {
            return (nodes.size() < 2 || super.isIncomplete());
        }

        public OSMWay build() throws IllegalStateException {
            if (isIncomplete()) {
                throw new IllegalStateException();
            }
            return new OSMWay(idBuild(), nodes, attributesBuild());
        }
    }
}
