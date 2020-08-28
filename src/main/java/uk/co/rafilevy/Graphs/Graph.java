package uk.co.rafilevy.Graphs;

import uk.co.rafilevy.Graphs.Exceptions.NonUniqueIdentifierException;
import uk.co.rafilevy.utils.Tuple.Pair;

import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

public class Graph {
    private final HashMap<String, Node> nodes;
    private boolean directed;

    public Graph() {
        nodes = new HashMap<>();
        directed = false;
    }

    public Node addNode(String identifier) throws NonUniqueIdentifierException {
        if (nodes.containsKey(identifier)) throw new NonUniqueIdentifierException(identifier);
        Node n = new Node(identifier);
        this.nodes.put(identifier, n);
        return n;
    }
    public Node removeNode(String identifier) {
        return nodes.remove(identifier);
    }

    public boolean addEdge(String ida, String idb, double v) {
        if (!nodes.containsKey(ida) || !nodes.containsKey(idb)) return false;
        Node a = nodes.get(ida);
        Node b = nodes.get(idb);
        a.addNeighbour(b, v);
        b.addNeighbour(a, v);
        return true;
    }
    public boolean addDirectedEdge(String ida, String idb, double v) {
        if (!nodes.containsKey(ida) || !nodes.containsKey(idb)) return false;
        directed = true;
        Node a = nodes.get(ida);
        Node b = nodes.get(idb);
        a.addNeighbour(b, v);
        b.addNeighbour(a, v);
        return true;
    }

    public Node getNodeWithIdentifier(String identifier) {
        return nodes.get(identifier);
    }
    public Collection<Node> getNodes() {
        return nodes.values();
    }

    public boolean isEmpty() { return nodes.isEmpty(); }
    public boolean isDirected() {
        return directed;
    }
    public boolean isConnected() {
        //Empty graphs are trivially connected
        if (this.isEmpty()) return true;

        //Perform a DFS from a random start node marking nodes as visited
        Node current;
        Deque<Node> toVisit = new LinkedList<>();
        toVisit.addLast(this.nodes.values().iterator().next());
        while (!toVisit.isEmpty()) {
            current = toVisit.removeLast();
            current.setFlag("_visited", true);
            for (Pair<Node, Double> edge : current.getNeighbours()) {
                Node n = edge.getFirst();
                if (!n.getFlag("_visited")) toVisit.addLast(n);
            }
        }

        //Check if all nodes have been visited and remove flags
        boolean allVisited = true;
        for (Node n : nodes.values()) {
            allVisited = allVisited && n.getFlag("_visited");
            n.removeFlag("_visited");
        }

        return allVisited;
    }
    public double getWeight() {
        double total = 0;
        for (Node n : this.nodes.values()) {
            for (Pair<Node, Double> edge : n.getNeighbours()) {
                total += edge.getSecond();
            }
        }
        if (!directed) total /= 2;
        return total;
    }
}
