package uk.co.rafilevy.utils.Pair;

import java.util.Objects;

public class Trip<A> {
    private A first;
    private A second;
    private A third;

    public Trip(A first, A second, A third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public A getFirst() { return first; }
    public A getSecond() { return second; }
    public A getThird() { return  third; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Trip<?> trip = (Trip<?>) o;
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
