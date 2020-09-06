package uk.co.rafilevy.Test.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.co.rafilevy.Graphs.*;
import uk.co.rafilevy.Graphs.Exceptions.IdentifierNotFoundException;
import uk.co.rafilevy.Graphs.Exceptions.NonUniqueIdentifierException;

public class LengthTest {
    @Test
    public void EmptyPathReturnsZero() {
        //Arrange
        Path p = new Path();

        //Assert
        double length = p.length();
        Assertions.assertEquals( 0, length );
    }

    @Test
    public void PathReturnsLength() throws NonUniqueIdentifierException, IdentifierNotFoundException {
        //Arrange
        Graph g = new Graph();
        Path p = new Path();

        //Act
        String a = g.addNode("a");
        String b = g.addNode("b");
        String c = g.addNode("c");
        g.addEdge(a, b, 5);
        g.addEdge(b, c, 2);

        p.addNode(g.getNodeWithIdentifier(a));
        p.addNode(g.getNodeWithIdentifier(b));
        p.addNode(g.getNodeWithIdentifier(c));

        //Assert
        double length = p.length();
        Assertions.assertEquals(7, length);
    }

    @Test
    public void NonPathReturnsInf() {

    }
}
