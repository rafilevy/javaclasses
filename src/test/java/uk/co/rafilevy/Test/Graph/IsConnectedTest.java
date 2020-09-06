package uk.co.rafilevy.Test.Graph;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import uk.co.rafilevy.Graphs.Exceptions.IdentifierNotFoundException;
import uk.co.rafilevy.Graphs.Exceptions.NonUniqueIdentifierException;
import uk.co.rafilevy.Graphs.Graph;
import uk.co.rafilevy.Graphs.Node;

public class IsConnectedTest {

    @Test
    public void SingletonGraphReturnsTrue() throws NonUniqueIdentifierException {
        //Arrange
        Graph g = new Graph();

        //Act
        g.addNode("a");

        //Assert
        boolean connected = g.isConnected();
        Assertions.assertTrue(connected);
    }

    @Test
    public void EmptyGraphReturnsTrue() {
        //Arrange
        Graph g = new Graph();

        //Assert
        boolean connected = g.isConnected();
        Assertions.assertTrue(connected);
    }

    @Test
    public void ConnectedUndirectedGraphReturnsTrue() throws NonUniqueIdentifierException, IdentifierNotFoundException {
        //Arrange
        Graph graph = new Graph();

        //Act
        graph.addNode("a");
        graph.addNode("b");
        graph.addNode("c");
        graph.addEdge("a", "b");
        graph.addEdge("b", "b");

        //Assert
        boolean is_connected = graph.isConnected();
        Assertions.assertTrue(is_connected);
    }

    @Test
    public void DisconnectedUndirectedGraphReturnsFalse() throws NonUniqueIdentifierException, IdentifierNotFoundException {
        //Arrange
        Graph graph = new Graph();

        //Act
        String a = graph.addNode("a");
        String b = graph.addNode("b");
        String c = graph.addNode("c");
        String d = graph.addNode("d");
        graph.addEdge(a, b);
        graph.addEdge(c, d);

        //Assert
        boolean is_disconnected = !graph.isConnected();
        Assertions.assertTrue(is_disconnected);
    }

    @Test
    public void ConnectedDirectedGraphReturnsTrue() throws NonUniqueIdentifierException, IdentifierNotFoundException {
        //Arrange
        Graph graph = new Graph();

        //Act
        String a = graph.addNode("a");
        String b = graph.addNode("b");
        String c = graph.addNode("c");
        graph.addDirectedEdge(a, b);
        graph.addDirectedEdge(b, c);
        graph.addDirectedEdge(c, a);

        //Assert
        boolean is_connected = graph.isConnected();
        Assertions.assertTrue(is_connected);
    }

    @Test
    public void DisconnectedDirectedGraphReturnsFalse() throws NonUniqueIdentifierException, IdentifierNotFoundException {
        //Arrange
        Graph graph = new Graph();

        //Act
        String a = graph.addNode("a");
        String b = graph.addNode("b");
        String c = graph.addNode("c");
        graph.addDirectedEdge(a, b);
        graph.addDirectedEdge(b, c);

        //Assert
        boolean is_disconnected = !graph.isConnected();
        Assertions.assertTrue(is_disconnected);
    }

}
