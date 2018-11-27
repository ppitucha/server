package dev.backend.interview.server.model;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface Model {

    boolean addNode(String node);

    boolean addEdge(String node1, String node2, Integer weight);

    boolean removeNode(String node);

    boolean removeEdge(String node1, String node2);

    int shortestPath(String node1, String node2) throws NodeNotFoundException;

    List<String> closerThan(String node, Integer limit) throws NodeNotFoundException;
}
