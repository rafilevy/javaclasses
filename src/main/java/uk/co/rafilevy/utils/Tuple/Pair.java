package uk.co.rafilevy.utils.Tuple;

import java.util.Objects;

/**
 * An ordered pair storing two items of type {@code A}
 * @param <A> the type of the items being stored
 */
public class Pair<A, B> {
    private A first;
    private B second;

    /**
     * Creates a pair with two given items
     * @param first the first item in the pair
     * @param second the second item in the pair
     */
    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Gets the first item from the pair
     * @return the first item from the pair
     */
    public A getFirst() { return first; }

    /**
     * Gets the second item from the pair
     * @return the second item from the pair
     */
    public B getSecond() { return second; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return first.equals(pair.first) &&
                second.equals(pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "(" + first.toString() + ", " + second.toString() + ")";
    }
}