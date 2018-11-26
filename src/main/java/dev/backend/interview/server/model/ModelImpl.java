package dev.backend.interview.server.model;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.traverse.ClosestFirstIterator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class ModelImpl implements Model {
    private Graph<String, DefaultWeightedEdge> graph = new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);

    @Override
    public synchronized boolean addNode(String node) {
        return graph.addVertex(node);
    }

    @Override
    public synchronized boolean addEdge(String node1, String node2, Integer weight) {
        if (!graph.containsVertex(node1) || !graph.containsVertex(node2))
            return false;

        DefaultWeightedEdge edge = graph.addEdge(node1, node2);
        graph.setEdgeWeight(edge, weight);
        return true;
    }

    @Override
    public synchronized boolean removeNode(String node) {
        return graph.removeVertex(node);
    }

    @Override
    public synchronized boolean removeEdge(String node1, String node2) {
        if (!graph.containsVertex(node1) || !graph.containsVertex(node2))
            return false;

        Set<DefaultWeightedEdge> allEdges = graph.getAllEdges(node1, node2);
        graph.removeAllEdges(allEdges);

        return true;
    }

    @Override
    public synchronized int shortestPath(String node1, String node2) throws NodeNotFoundException {
        if (!graph.containsVertex(node1) || !graph.containsVertex(node2))
            throw new NodeNotFoundException();

        ShortestPathAlgorithm<String, DefaultWeightedEdge> dijkstraAlg =
                new DijkstraShortestPath<>(graph);

        final Double pathWeight = dijkstraAlg.getPathWeight(node1, node2);

        if (pathWeight.equals(Double.MAX_VALUE))
            return Integer.MAX_VALUE;

        return pathWeight.intValue();
    }

    @Override
    public synchronized List<String> closerThan(String node, Integer limit) throws NodeNotFoundException {
        if (!graph.containsVertex(node))
            throw new NodeNotFoundException();

        ClosestFirstIterator<String, DefaultWeightedEdge> iterator =
                new ClosestFirstIterator<>(graph, node, limit - 1);

        Iterable<String> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), false)
                .filter(el -> !el.equals(node))
                .sorted()
                .collect(Collectors.toList());
    }
}