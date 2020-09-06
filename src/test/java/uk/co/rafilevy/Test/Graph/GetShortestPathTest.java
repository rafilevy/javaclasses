package uk.co.rafilevy.Test.Graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.rafilevy.Graphs.Edge;
import uk.co.rafilevy.Graphs.Exceptions.IdentifierNotFoundException;
import uk.co.rafilevy.Graphs.Exceptions.NonUniqueIdentifierException;
import uk.co.rafilevy.Graphs.Graph;
import uk.co.rafilevy.Graphs.Path;

public class GetShortestPathTest {

    @Test
    public void SingletonGraphReturnsSingleNode() throws NonUniqueIdentifierException, IdentifierNotFoundException {
        //Arrange
        Graph g = new Graph();

        //Act
        g.addNode("a");
        Path p = g.getShortestPath("a", "a");

        //Assert
        Assertions.assertEquals(0, p.length());
        Assertions.assertEquals(1, p.size());
        Assertions.assertEquals(p.getNode(0), g.getNodeWithIdentifier("a"));
    }

    @Test
    public void ReturnsShortestPath() throws NonUniqueIdentifierException, IdentifierNotFoundException {
        //Arrange
        Graph g = new Graph();

        //Act
        g.addNode("a");
        g.addNode("b");
        g.addNode("c");
        g.addNode("d");
        g.addNode("e");

        g.addEdge("a", "b", 1);
        g.addEdge("a", "c", 4);
        g.addEdge("b", "c", 1);
        g.addEdge("b", "d", 5);
        g.addEdge("c", "d", 2);
        g.addEdge("c", "e", 6);
        g.addEdge("d", "e", 2);

        Path p = g.getShortestPath("a", "e");

        System.out.println(p);
        Assertions.assertEquals(6, p.length());
        Assertions.assertEquals(5, p.size());
        Assertions.assertEquals("{a}-1.0->{b}-1.0->{c}-2.0->{d}-2.0->{e}", p.toString());
    }

    @Test
    public void NoPathReturnsNull() throws IdentifierNotFoundException, NonUniqueIdentifierException {
        Graph g = new Graph();
        g.addNode("a");
        g.addNode("b");
        g.addNode("c");
        g.addNode("d");

        g.addEdge("a", "b", 1);
        g.addEdge("c", "d", 1);

        Path p = g.getShortestPath("a", "d");

        Assertions.assertNull(p);
    }
}
