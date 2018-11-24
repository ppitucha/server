package dev.backend.interview.server.dev.backend.interview.server.model;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.traverse.ClosestFirstIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


public class ModelImpl implements Model {
    private DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph =
            new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);

    @Override
    public synchronized String addNode(String node) {
        if (graph.containsVertex(node))
            return "ERROR: NODE ALREADY EXISTS";
        graph.addVertex(node);
        return "NODE ADDED";
    }

    @Override
    public synchronized String addEdge(String node1, String node2, Integer weight) {
        if (!graph.containsVertex(node1) || !graph.containsVertex(node2))
            return "ERROR: NODE NOT FOUND";

        DefaultWeightedEdge edge = graph.addEdge(node1, node2);
        graph.setEdgeWeight(edge, weight);
        return "EDGE ADDED";
    }

    @Override
    public synchronized String removeNode(String node) {
        if (!graph.containsVertex(node))
            return "ERROR: NODE NOT FOUND";

        graph.removeVertex(node);
        return "NODE REMOVED";
    }

    @Override
    public synchronized String removeEdge(String node1, String node2) {
        if (!graph.containsVertex(node1) || !graph.containsVertex(node2))
            return "ERROR: NODE NOT FOUND";

        Set<DefaultWeightedEdge> allEdges = graph.getAllEdges(node1, node2);
        graph.removeAllEdges(allEdges);
        return "EDGE REMOVED";
    }

    @Override
    public synchronized String shortestPath(String node1, String node2) {
        if (!graph.containsVertex(node1) || !graph.containsVertex(node2))
            return "ERROR: NODE NOT FOUND";

        ShortestPathAlgorithm<String, DefaultWeightedEdge> dijkstraAlg =
                new DijkstraShortestPath<>(graph);

        GraphPath<String, DefaultWeightedEdge> path = dijkstraAlg.getPath(node1, node2);

        if (path == null)
            return String.valueOf(Integer.MAX_VALUE);
        return String.valueOf(path.getLength());
    }

    @Override
    public synchronized String closerThan(String node, Integer limit) {
        if (!graph.containsVertex(node))
            return "ERROR: NODE NOT FOUND";

        ClosestFirstIterator<String, DefaultWeightedEdge> iterator =
                new ClosestFirstIterator<>(graph, node, limit - 1);

        List<String> result = new ArrayList<>();
        while (iterator.hasNext()) {
            String element = iterator.next();
            if (!element.equals(node))
                result.add(element);
        }

        Collections.sort(result);
        return String.join(",", result);
    }
}