package uk.co.rafilevy.Graphs;

public class Edge {
    private final Node start;
    private final Node end;
    private final double weight;

    public Edge(Node start, Node end, double weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }


    public Node getStart() {
        return this.start;
    }
    public Node getEnd() {
        return this.end;
    }
    public double getWeight() {
        return this.weight;
    }

    @Override
    public String toString() {
        return this.start+"-"+this.weight+"-"+this.end;
    }
}
