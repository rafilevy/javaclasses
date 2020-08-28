package uk.co.rafilevy.Graphs;

import uk.co.rafilevy.utils.Tuple.Pair;

import java.util.*;

public class Node {

    private final HashSet<Pair<Node, Double>> neighbours;
    private final HashMap<String, Boolean> flags;
    private final HashMap<String, Double> values;
    public final String identifier;

    public Node(String identifier) {
        this.identifier = identifier;
        this.neighbours = new HashSet<>();
        this.flags = new HashMap<>();
        this.values = new HashMap<>();
    }

    public boolean getFlag(String f) {
        if (!flags.containsKey(f)) return false;
        return flags.get(f);
    }
    public boolean setFlag(String f, boolean b) {
        return flags.put(f, b);
    }
    public boolean removeFlag(String f) {
        return flags.remove(f);
    }
    public void clearFlags() {
        flags.clear();
    }

    public double getValue(String f) {
        return values.get(f);
    }
    public double setValue(String f, double value) {
        return values.put(f, value);
    }
    public double removeValue(String f) { return values.remove(f); }
    public boolean containsValue(String f) {return values.containsKey(f); }
    public void clearValues() {
        values.clear();
    }

    protected void addNeighbour(Node o, double v) {
        neighbours.add(new Pair(o, v));
    }
    protected boolean removeNeighbour(Node o) {
        return neighbours.removeIf((e) -> e.getFirst().equals(o));
    }
    public Iterable<Pair<Node, Double>> getNeighbours() {
        return new HashSet<>(neighbours);
    }
    public boolean hasNeighbour(Node a) {
        for (Pair<Node, Double> n : neighbours) {
            if (n.getFirst().equals(a)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "{"+this.identifier+"}";
    }
}
