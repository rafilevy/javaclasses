package uk.co.rafilevy.Test.Graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.rafilevy.Graphs.Exceptions.IdentifierNotFoundException;
import uk.co.rafilevy.Graphs.Exceptions.NonUniqueIdentifierException;
import uk.co.rafilevy.Graphs.Graph;
import uk.co.rafilevy.Graphs.Node;

public class IsDirectedTest {
    @Test
    public void DirectedGraphReturnsTrue() throws NonUniqueIdentifierException, IdentifierNotFoundException {
        //Arrange
        Graph g = new Graph();

        //Act
        String a = g.addNode("a");
        String b = g.addNode("b");
        g.addNode("c");

        g.addDirectedEdge(a, b);

        //Assert
        Assertions.assertTrue(g.isDirected());
    }
    @Test
    public void UndirectedGraphReturnsFalse() throws NonUniqueIdentifierException, IdentifierNotFoundException {
        //Arrange
        Graph g = new Graph();

        //Act
        String a = g.addNode("a");
        String b = g.addNode("b");
        String c = g.addNode("c");

        g.addEdge(a, b);
        g.addEdge(b, c);

        //Assert
        Assertions.assertFalse(g.isDirected());
    }
}
