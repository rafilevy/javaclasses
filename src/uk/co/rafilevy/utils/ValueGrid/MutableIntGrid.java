package uk.co.rafilevy.utils.ValueGrid;

import java.util.function.BinaryOperator;

public class MutableIntGrid<A, B> extends ValueGrid<A, B, Integer> {

    public MutableIntGrid() {
        super(0);
    }

    private void operate(A a, B b, int value, BinaryOperator<Integer> f) {
        if (this.containsRow(a) && this.containsColumn(b)) this.set(a, b, f.apply(this.get(a, b), value));
        else this.set(a, b, f.apply(0, value));
    }

    public void add(A a, B b, int value) { operate(a, b, value, Integer::sum); }

    public void subtract(A a, B b, int value) { operate(a, b, -value, Integer::sum); }

    public void multiply(A a, B b, int value) { operate(a, b, value, (Integer x, Integer y)->x*y); }

    public void divide(A a, B b, int value) { operate(a, b, value, (Integer x, Integer y)->x/y); }
}
