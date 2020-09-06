package uk.co.rafilevy.Graphs;

import java.util.ArrayList;
import java.util.Iterator;

public class Path implements Iterable<Edge> {
    private final ArrayList<Node> nodes;
    private boolean reversed;

    public Path() {
        this.nodes = new ArrayList<>();
        reversed = false;
    }

    public void addNode(Node n) {
        this.nodes.add(n);
    }

    public boolean containsNode(Node n) {
        return nodes.contains(n);
    }

    public Node getNode(int index) {
        return this.reversed ? nodes.get(this.size() - 1 - index) : nodes.get(index);
    }

    public int size() {
        return this.nodes.size();
    }

    public double length() {
        double total = 0;
        for (Edge e : this) {
            total += e.getWeight();
        }
        return total;
    }

    public void reverse() {
        this.reversed = !this.reversed;
    }

    private class PathIterator implements Iterator<Edge> {
        private int currentIndex;
        private final int size;

        private PathIterator() {
            this.size = size();
            if (!reversed) {
                this.currentIndex = 0;
            } else {
                this.currentIndex = size - 1;
            }
        }

        @Override
        public boolean hasNext() {
            if (!reversed) {
                return currentIndex < this.size - 1;
            } else {
                return currentIndex > 0;
            }
        }

        @Override
        public Edge next() {
            Node from = nodes.get(currentIndex);
            Node to;
            if (!reversed) {
                to = nodes.get(currentIndex + 1);
                currentIndex++;
            } else {
                to = nodes.get(currentIndex - 1);
                currentIndex--;
            }
            return new Edge(from, to, from.getDistanceTo(to));
        }
    }

    @Override
    public Iterator<Edge> iterator() {
        return new PathIterator();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Edge e : this) s.append(e.getStart()).append("-").append(e.getWeight()).append("->");
        s.append(this.getNode(this.size() - 1));
        return s.toString();
    }
}

