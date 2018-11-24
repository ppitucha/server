package dev.backend.interview.server.dev.backend.interview.server.model;

public interface Model {
    String addNode(String node);

    String addEdge(String node1, String node2, Integer weight);

    String removeNode(String node);

    String removeEdge(String node1, String node2);

    String shortestPath(String node1, String node2);

    String closerThan(String node, Integer limit);
}
