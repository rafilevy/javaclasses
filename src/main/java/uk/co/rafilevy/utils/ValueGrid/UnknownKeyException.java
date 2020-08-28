package uk.co.rafilevy.utils.ValueGrid;

/**
 * An exception indicating that an unknown key is being used to index a ValueGrid
 */
public class UnknownKeyException extends RuntimeException {
    public UnknownKeyException(Object key) {
        super(String.format("The ValueGrid does not contain the key %s.", key));
    }
}
