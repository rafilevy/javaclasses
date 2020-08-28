package uk.co.rafilevy.Graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Path {
    private final ArrayList<Node> nodes;

    public Path() {
        this.nodes = new ArrayList<>();
    }
    public Path(Collection<Node> nodes) {
        this.nodes = new ArrayList<>(nodes);
    }

    public Iterator<Node> getNodes() {
        return nodes.iterator();
    }
    public boolean containsNode(Node n) {
        return nodes.contains(n);
    }

    @Override
    public String toString() {
        String s = "";
        for (Node n : this.nodes) {
            s += n.toString() + " -> ";
        }
        return s.substring(0, s.length() - 4);
    }
}
