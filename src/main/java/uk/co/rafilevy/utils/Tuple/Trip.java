package uk.co.rafilevy.utils.Tuple;

import java.util.Objects;

/**
 * An ordered triple storing three items of type {@code A}
 * @param <A> the type of the stored items
 */
public class Trip<A, B, C> {
    private A first;
    private B second;
    private C third;

    /**
     * Creates a triple with three given items
     * @param first the first item in the triple
     * @param second the second item in the triple
     * @param third the third item in the triple
     */
    public Trip(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Gets the first item in the triple
     * @return the first item in the triple
     */
    public A getFirst() { return first; }

    /**
     * Gets the second item in the triple
     * @return the second item in the triple
     */
    public B getSecond() { return second; }

    /**
     * Gets the third item in the triple
     * @return the third item in the triple
     */
    public C getThird() { return  third; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trip)) return false;
        Trip<?, ?, ?> trip = (Trip<?, ?, ?>) o;
        return first.equals(trip.first) &&
                second.equals(trip.second) &&
                third.equals(trip.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString() {
        return "(" +first.toString() + ", " + second.toString() + ", " + third.toString() + ")";
    }
}
