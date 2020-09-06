package uk.co.rafilevy.Graphs.Exceptions;

public class NonUniqueIdentifierException extends Exception {
    public NonUniqueIdentifierException(String identifier) {
        super("A Node with id: "+ identifier + " already exists in the graph.");
    }

}
