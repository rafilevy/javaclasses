package uk.co.rafilevy.Graphs.Exceptions;

public class NonUniqueIdentifierException extends Exception {
    public NonUniqueIdentifierException(String identifier) {
        super("The identifier " + identifier + " is already in use in the graph.");
    }
}
