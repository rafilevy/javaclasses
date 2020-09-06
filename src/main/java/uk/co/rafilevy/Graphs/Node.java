package uk.co.rafilevy.Graphs;

import java.util.*;

public class Node implements Cloneable {

    private final HashSet<Edge> neighbours;
    private final HashSet<String> flags;
    private final HashMap<String, Double> values;
    private final HashMap<String, Node> helperNodes;
    public final String identifier;

    public Node(String identifier) {
        this.identifier = identifier;
        this.neighbours = new HashSet<>();
        this.flags = new HashSet<>();
        this.values = new HashMap<>();
        this.helperNodes = new HashMap<>();
    }

    public Boolean getFlag(String f) {
        return flags.contains(f);
    }
    public Boolean setFlag(String f) {
        return flags.add(f);
    }
    public Boolean unsetFlag(String f) {
        return flags.remove(f);
    }
    public void clearFlags() {
        flags.clear();
    }

    public Double getValue(String f) {
        return values.get(f);
    }
    public Double setValue(String f, double value) {
        return values.put(f, value);
    }
    public Double removeValue(String f) { return values.remove(f); }
    public boolean containsValue(String f) {return values.containsKey(f); }
    public void clearValues() {
        values.clear();
    }

    public Node getHelper(String f) {
        return helperNodes.get(f);
    }
    public Node setHelper(String f, Node n) {
        return helperNodes.put(f, n);
    }
    public Node removeHelper(String f) { return helperNodes.remove(f); }
    public boolean containsHelper(String f) { return helperNodes.containsKey(f); }
    public void clearHelpers() {
        helperNodes.clear();
    }

    protected void addNeighbour(Node o, double v) {
        neighbours.add(new Edge(this, o, v));
    }
    protected boolean removeNeighbour(Node o) {
        return neighbours.removeIf((e) -> e.getEnd().equals(o));
    }
    public Iterable<Edge> getNeighbours() {
        return new HashSet<>(neighbours);
    }
    public double getDistanceTo(Node a) {
        for (Edge e : neighbours) {
            if (e.getEnd().equals(a)) return e.getWeight();
        }
        return Double.POSITIVE_INFINITY;
    }
    public boolean hasNeighbour(Node a) {
        for (Edge e : neighbours) {
            if (e.getEnd().equals(a)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "{"+this.identifier+"}";
    }

}
