package uk.co.rafilevy.utils.ValueGrid;

public class UnknownKeyException extends RuntimeException {
    public UnknownKeyException(Object key) {
        super(String.format("The ValueGrid does not contain the key %s.", key));
    }
}
