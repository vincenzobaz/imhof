package ch.epfl.imhof;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Set;

import ch.epfl.imhof.PointGeo;

public class GraphTestPerso {
    @Test(expected = IllegalArgumentException.class)
    public void builderTest() {
        Graph.Builder<PointGeo> graphInConstruction = new Graph.Builder<>();
        PointGeo p1 = new PointGeo(Math.toRadians(40), Math.toRadians(50));
        PointGeo p2 = new PointGeo(Math.toRadians(0), Math.toRadians(0));
        PointGeo p3 = new PointGeo(Math.toRadians(-45), Math.toRadians(-20));
        PointGeo p4 = new PointGeo(Math.toRadians(90), Math.toRadians(60));
        PointGeo p5 = new PointGeo(Math.toRadians(0), Math.toRadians(33));
        graphInConstruction.addNode(p1);
        graphInConstruction.addNode(p2);
        graphInConstruction.addNode(p3);
        graphInConstruction.addNode(p5);

        graphInConstruction.addEdge(p1, p2);
        graphInConstruction.addEdge(p2, p3);
        graphInConstruction.addEdge(p1, p5);
        graphInConstruction.addEdge(p3, p5);

        Graph<PointGeo> newGraph = graphInConstruction.build();
        Set<PointGeo> nodes = newGraph.nodes();

        assertTrue(nodes.contains(p1));
        assertTrue(nodes.contains(p3));
        assertTrue(nodes.contains(p2));
        assertTrue(nodes.contains(p5));
        assertFalse(nodes.contains(p4));

        Set<PointGeo> neighbors = newGraph.neighborsOf(p1);
        assertTrue(neighbors.contains(p2));
        assertTrue(neighbors.contains(p5));
        assertFalse(neighbors.contains(p3));
        assertFalse(neighbors.contains(p4));

        newGraph.neighborsOf(p4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEdgeThrowsCorrectException() {
        Graph.Builder<PointGeo> graphInConstruction = new Graph.Builder<>();
        PointGeo p1 = new PointGeo(Math.toRadians(40), Math.toRadians(50));
        PointGeo p2 = new PointGeo(Math.toRadians(0), Math.toRadians(0));
        PointGeo p3 = new PointGeo(Math.toRadians(-45), Math.toRadians(-20));
        PointGeo p4 = new PointGeo(Math.toRadians(90), Math.toRadians(60));
        graphInConstruction.addNode(p1);
        graphInConstruction.addNode(p2);
        graphInConstruction.addNode(p3);

        graphInConstruction.addEdge(p1, p4);
    }
}
