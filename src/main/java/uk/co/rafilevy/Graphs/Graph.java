package uk.co.rafilevy.Graphs;

import uk.co.rafilevy.Graphs.Exceptions.IdentifierNotFoundException;
import uk.co.rafilevy.Graphs.Exceptions.NonUniqueIdentifierException;

import java.util.*;
import java.util.function.BiFunction;


public class Graph {
    private final HashMap<String, Node> nodes;
    private boolean directed;

    public Graph() {
        nodes = new HashMap<>();
        directed = false;
    }

    public Graph(Graph g) {
        this.nodes = new HashMap<>(g.nodes);
        this.directed = g.directed;
    }

    public String addNode(String identifier) throws NonUniqueIdentifierException {
        if (nodes.containsKey(identifier)) throw new NonUniqueIdentifierException(identifier);
        Node n = new Node(identifier);
        this.nodes.put(identifier, n);
        return n.identifier;
    }
    public String removeNode(String identifier) {
        return nodes.remove(identifier).identifier;
    }

    public void addEdge(String ida, String idb, double v) throws IdentifierNotFoundException {
        if (!this.nodes.containsKey(ida)) throw new IdentifierNotFoundException(ida);
        else if (!this.nodes.containsKey(idb)) throw new IdentifierNotFoundException(idb);
        Node a = this.nodes.get(ida);
        Node b = this.nodes.get(idb);
        a.addNeighbour(b, v);
        b.addNeighbour(a, v);
    }
    public void addEdge(String ida, String idb) throws IdentifierNotFoundException {
        this.addEdge(ida, idb, 0.0);
    }

    public void addDirectedEdge(String ida, String idb, double v) throws IdentifierNotFoundException {
        if (!this.nodes.containsKey(ida)) throw new IdentifierNotFoundException(ida);
        else if (!this.nodes.containsKey(idb)) throw new IdentifierNotFoundException(idb);
        Node a = this.nodes.get(ida);
        Node b = this.nodes.get(idb);
        this.directed = true;
        a.addNeighbour(b, v);
    }
    public void addDirectedEdge(String ida, String idb) throws IdentifierNotFoundException {
        this.addDirectedEdge(ida, idb, 0.0);
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

        if (this.directed) {
            Deque<Node> toVisit = new LinkedList<>();
            for (Node s : this.nodes.values()) {
                Node current;
                toVisit.addLast(s);
                while (!toVisit.isEmpty()) {
                    current = toVisit.removeLast();
                    current.setFlag("_visited");
                    for (Edge edge : current.getNeighbours()) {
                        Node n = edge.getEnd();
                        if (!n.getFlag("_visited")) toVisit.addLast(n);
                    }
                }

                //Check if all nodes have been visited and remove flags
                boolean allVisited = true;
                for (Node n : nodes.values()) {
                    allVisited = allVisited && n.getFlag("_visited");
                    n.unsetFlag("_visited");
                }
                if (!allVisited) return false;
            }
            return true;
        }
        else {
            //Perform a DFS from a random start node marking nodes as visited
            Node current;
            Deque<Node> toVisit = new LinkedList<>();
            toVisit.addLast(this.nodes.values().iterator().next());
            while (!toVisit.isEmpty()) {
                current = toVisit.removeLast();
                current.setFlag("_visited");
                for (Edge edge : current.getNeighbours()) {
                    Node n = edge.getEnd();
                    if (!n.getFlag("_visited")) toVisit.addLast(n);
                }
            }

            //Check if all nodes have been visited and remove flags
            boolean allVisited = true;
            for (Node n : nodes.values()) {
                allVisited = allVisited && n.getFlag("_visited");
                n.unsetFlag("_visited");
            }

            return allVisited;
        }
    }
    public double getWeight() {
        double total = 0;
        for (Node n : this.nodes.values()) {
            for (Edge edge : n.getNeighbours()) {
                total += edge.getWeight();
            }
        }
        if (!directed) total /= 2;
        return total;
    }

    public Path getShortestPath(String idFrom, String idTo, BiFunction<Node, Node, Double> heuristicFunction) throws IdentifierNotFoundException {
        if (!this.nodes.containsKey(idFrom)) throw new IdentifierNotFoundException(idFrom);
        else if (!this.nodes.containsKey(idTo)) throw new IdentifierNotFoundException(idTo);
        Node from = this.nodes.get(idFrom);
        Node to = this.nodes.get(idTo);


        class shortestPathNodeComparator implements Comparator<Node> {
            @Override
            public int compare(Node n1, Node n2) {
                Double v1 = n1.getValue("_distance") + heuristicFunction.apply(n1, to);
                Double v2 = n2.getValue("_distance") + heuristicFunction.apply(n2, to);
                return v1.compareTo(v2);
            }
        }

        PriorityQueue<Node> openList = new PriorityQueue<>(new shortestPathNodeComparator());
        from.setValue("_distance", 0);
        openList.add(from);
        while (!openList.isEmpty()) {
            Node current = openList.poll();
            current.setFlag("_visited");
            if (current == to) break;
            double currentDist = current.getValue("_distance");
            for (Edge e : current.getNeighbours()) {
                Node n = e.getEnd();
                if (!n.getFlag("_visited")) {
                    double dist = currentDist + e.getWeight() + heuristicFunction.apply(n, to);
                    if (!n.containsValue("_distance") || n.getValue("_distance") > dist) {
                        openList.remove(n);
                        n.setValue("_distance", dist);
                        n.setHelper("_from", current);
                        openList.add(n);
                    }
                }
            }
        }

        if (!to.getFlag("_visited")) {
            for (Node n : this.getNodes()) {
                n.removeHelper("_from");
                n.removeValue("_distance");
                n.unsetFlag("_visited");
            }
            return null;
        }

        Path p = new Path();
        Node current = to;
        while (current.containsHelper("_from")) {
            p.addNode(current);
            current = current.getHelper("_from");
        }
        p.addNode(from);
        p.reverse();

        for (Node n : this.getNodes()) {
            n.removeHelper("_from");
            n.removeValue("_distance");
            n.unsetFlag("_visited");
        }

        return p;
    }
    public Path getShortestPath(String idFrom, String idTo) throws IdentifierNotFoundException {
        return this.getShortestPath(idFrom, idTo, (a, b) -> 0.0);
    }
}
