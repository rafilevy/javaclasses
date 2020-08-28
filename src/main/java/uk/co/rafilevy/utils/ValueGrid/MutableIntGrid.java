package uk.co.rafilevy.utils.ValueGrid;

import java.util.function.BinaryOperator;

/**
 * MutableIntGrid is a ValueGrid containing integer values with some methods aiding mutation of values in the grid
 * @param <A> The row label type
 * @param <B> The column label type
 */
public class MutableIntGrid<A, B> extends ValueGrid<A, B, Integer> {

    /**
     * Constructs a new empty MutableIntGrid
     * Null value is set to 0
     */
    public MutableIntGrid() {
        super(0);
    }

    /**
     * Constructs a new empty MutableIntGrid with a specified null value
     * @param nullValue the default value of undefined items
     */
    public MutableIntGrid(int nullValue) {
        super(nullValue);
    }

    /**
     * Modifies the item at row {@code a} column {@code b} by applying the binary function f to it taking second parameter {@code value}
     * @param a the row to be modified
     * @param b the column to be modified
     * @param value the second parameter to modification function f
     * @param f the binary operator to be applied to the item
     */
    private void operate(A a, B b, int value, BinaryOperator<Integer> f) {
        if (this.containsRow(a) && this.containsColumn(b)) this.set(a, b, f.apply(this.get(a, b), value));
        else this.set(a, b, f.apply(0, value));
    }

    /**
     * Adds a value to the item in row {@code a} column {@code b}
     * @param a the row to be modified
     * @param b the column to be modified
     * @param value the value to be added to the specified item
     */
    public void add(A a, B b, int value) { operate(a, b, value, Integer::sum); }

    /**
     * Subtracts a value from the item in row {@code a} column {@code b}
     * @param a the row to be modified
     * @param b the column to be modified
     * @param value the value to be subtracted from the specified item
     */
    public void subtract(A a, B b, int value) { operate(a, b, -value, Integer::sum); }

    /**
     * Multiplies the item in row {@code a} column {@code b} by a specified value
     * @param a the row to be modified
     * @param b the column to be modified
     * @param value the value to multiply the specified item by
     */
    public void multiply(A a, B b, int value) { operate(a, b, value, (Integer x, Integer y)->x*y); }

    /**
     * Divides (Integer division) the item in row {@code a} column {@code b} by a specified value
     * @param a the row to be modified
     * @param b the column to be modified
     * @param value the value to divide the specified item by
     */
    public void divide(A a, B b, int value) { operate(a, b, value, (Integer x, Integer y) -> x / y); }


    /**
     * Gives the remainder the item in row {@code a} column {@code b} when divided by a specified value
     * @param a the row to be modified
     * @param b the column to be modified
     * @param value the value to divide the specified item by
     */
    public void modulo(A a, B b, int value) { operate(a, b, value, (Integer x, Integer y) -> x % y);}
}
