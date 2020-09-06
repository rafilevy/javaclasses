package uk.co.rafilevy.Graphs.Exceptions;

public class IdentifierNotFoundException extends Exception {
    public IdentifierNotFoundException(String identifier) {
        super("A Node with id: "+ identifier + " was not found in the graph.");
    }

}
