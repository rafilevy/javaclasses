package uk.co.rafilevy.utils.Pair;

import java.util.Objects;

public class Pair<A> {
    private A first;
    private A second;

    public Pair(A first, A second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() { return first; }
    public A getSecond() { return second; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair<?> pair = (Pair<?>) o;
        return first.equals(pair.first) &&
                second.equals(pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "(" +first.toString() + ", " + second.toString() + ")";
    }
}
