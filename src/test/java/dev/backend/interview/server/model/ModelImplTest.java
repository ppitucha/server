package dev.backend.interview.server.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.*;

public class ModelImplTest {
    private Model model;

    @Before
    public void init() {
        model = ModelProvider.getModel();
    }

    @Test
    public void testNodes() {
        final String node1 = UUID.randomUUID().toString();
        final String node2 = UUID.randomUUID().toString();

        //remove not existing
        assertFalse(model.removeNode(node1));
        assertFalse(model.removeNode(node2));

        assertTrue(model.addNode(node1));
        assertTrue(model.addNode(node2));

        //adding again
        assertFalse(model.addNode(node1));
        assertFalse(model.addNode(node2));
        assertTrue(model.removeNode(node1));
        assertTrue(model.removeNode(node2));

        //remove not existing
        assertFalse(model.removeNode(node1));
        assertFalse(model.removeNode(node2));

        assertTrue(model.addNode(node1));
        assertTrue(model.addNode(node2));
        assertTrue(model.removeNode(node1));
        assertTrue(model.removeNode(node2));
    }

    @Test
    public void testEdges() {
        final String node1 = UUID.randomUUID().toString();
        final String node2 = UUID.randomUUID().toString();
        final String node3 = UUID.randomUUID().toString();
        final String node4 = UUID.randomUUID().toString();

        assertTrue(model.addNode(node1));
        assertTrue(model.addNode(node2));
        assertTrue(model.addNode(node3));
        assertTrue(model.addNode(node4));

        assertTrue(model.removeEdge(node1, node2));
        assertTrue(model.removeEdge(node2, node3));
        assertTrue(model.removeEdge(node3, node4));
        assertTrue(model.removeEdge(node4, node1));

        assertTrue(model.addEdge(node1, node2, 1));
        assertTrue(model.addEdge(node2, node3, 1));
        assertTrue(model.addEdge(node3, node4, 1));
        assertTrue(model.addEdge(node4, node1, 1));
        assertTrue(model.addEdge(node1, node1, 10));

        assertTrue(model.addEdge(node1, node2, 2));
        assertTrue(model.addEdge(node2, node3, 2));

        assertTrue(model.removeEdge(node1, node2));
        assertTrue(model.removeEdge(node2, node3));
        assertTrue(model.removeEdge(node3, node4));
        assertTrue(model.removeEdge(node4, node1));

        assertTrue(model.removeNode(node1));
        assertTrue(model.removeNode(node2));

        assertFalse(model.addEdge(node1, node2, 1));
        assertTrue(model.addEdge(node3, node4, 1));

        assertTrue(model.removeNode(node3));
        assertTrue(model.removeNode(node4));

        assertFalse(model.removeEdge(node1, node2));
        assertFalse(model.removeEdge(node2, node3));
        assertFalse(model.removeEdge(node3, node4));
        assertFalse(model.removeEdge(node4, node1));
    }

    @Test
    public void testShortestPath() {
        final String node1 = UUID.randomUUID().toString();
        final String node2 = UUID.randomUUID().toString();
        final String node3 = UUID.randomUUID().toString();
        final String node4 = UUID.randomUUID().toString();
        final String node5 = UUID.randomUUID().toString();

        assertTrue(model.addNode(node1));
        assertTrue(model.addNode(node2));
        assertTrue(model.addNode(node3));
        assertTrue(model.addNode(node4));
        assertTrue(model.addNode(node5));

        assertTrue(model.addEdge(node1, node2, 1));
        assertTrue(model.addEdge(node2, node3, 2));
        assertTrue(model.addEdge(node3, node4, 3));
        assertTrue(model.addEdge(node1, node4, 4));
        try {
            assertEquals(4, model.shortestPath(node1, node4));
            assertEquals(Integer.MAX_VALUE, model.shortestPath(node1, node5));
        } catch (NodeNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCloserThan() {
        final String node1 = UUID.randomUUID().toString();
        final String node2 = UUID.randomUUID().toString();
        final String node3 = UUID.randomUUID().toString();
        final String node4 = UUID.randomUUID().toString();
        final String node5 = UUID.randomUUID().toString();

        assertTrue(model.addNode(node1));
        assertTrue(model.addNode(node2));
        assertTrue(model.addNode(node3));
        assertTrue(model.addNode(node4));
        assertTrue(model.addNode(node5));

        assertTrue(model.addEdge(node1, node2, 1));
        assertTrue(model.addEdge(node2, node3, 2));
        assertTrue(model.addEdge(node3, node4, 3));

        try {
            final ArrayList<String> result = new ArrayList<>();
            result.add(node2);
            result.add(node3);
            Collections.sort(result);

            assertArrayEquals(result.toArray(), model.closerThan(node1, 6).toArray());
            assertArrayEquals(new ArrayList<String>().toArray(), model.closerThan(node5, 6).toArray());
        } catch (NodeNotFoundException e) {
            e.printStackTrace();
        }
    }


}