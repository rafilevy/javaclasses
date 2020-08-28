package uk.co.rafilevy.utils.ValueGrid;

import java.util.*;


/**
 * ValueGrid is a container where rows and columns index a value
 * Is essentially a wrapper around a {@code Map<A, Map<B, V>}
 * All possible values default to a specified null value if not set.
 * @param <A> The row label type
 * @param <B> The column label type
 * @param <V> The value type
 */
public class ValueGrid<A, B, V> {
    protected Map<A, Map<B, V>> matrix;
    protected Set<A> rows;
    protected Set<B> cols;

    private final V nullValue;

    /**
     * Constructs a new, empty ValueGrid,
     * null value is set to {@code null}
     */
    public ValueGrid() {
        matrix = new HashMap<>();
        rows = new HashSet<>();
        cols = new HashSet<>();
        nullValue = null;
    }

    /**
     * Constructs a new, empty ValueGrid,
     * @param nullValue the default value of unset entries
     */
    public ValueGrid(V nullValue) {
        matrix = new HashMap<>();
        rows = new HashSet<>();
        cols = new HashSet<>();
        this.nullValue = nullValue;
    }

    /**
     * Returns the value in the specified row or column
     * @param a the row being indexed
     * @param b the column being indexed
     * @return The value in row {@code a} column {@code b} or {@code nullValue} if not set
     * @throws UnknownKeyException indicates that either the row or column have never been added
     */
    public V get(A a, B b) throws UnknownKeyException {
        Boolean rowErr;
        if (rowErr = !rows.contains(a) || !cols.contains(b)) throw new UnknownKeyException(rowErr ? a : b);
        return matrix.get(a).containsKey(b) ? matrix.get(a).get(b) : nullValue;
    }

    /**
     * Returns a copy of a specified column as a {@code Map}
     * @param b the column to be returned
     * @return A {@code Map<A, V>} object representing a copy of the column
     * @throws UnknownKeyException indicates that either the row or column have never been added
     */
    public Map<A, V> getColumn(B b) {
        Map<A, V> column = new HashMap<>();
        for (A a : rows) {
            column.put(a, this.get(a, b));
        }
        return column;
    }

    /**
     * Returns a copy of a specified row as a {@code Map}
     * @param a the row to be returned
     * @return a copy of row {@code a}
     * @throws UnknownKeyException indicates that either the row or column have never been added
     */
    public Map<B, V> getRow(A a)  {
        Map<B, V> row = new HashMap<>();
        for (B b : cols) {
           row.put(b, this.get(a, b));
        }
        return row;
    }

    /**
     * Sets the value in row {@code a} column {@code b} and creates the rows and columns if not previously seen
     * @param a the row to be set
     * @param b the column to be set
     * @param v the value to set
     */
    public void set(A a, B b, V v) {
        if (!rows.contains(a)) {
            matrix.put(a, new HashMap<>());
            rows.add(a);
        } if (!cols.contains(b)) cols.add(b);
        matrix.get(a).put(b, v);
    }

    /**
     * Returns a set of all the rows which have been added
     * @return a set containing all the row labels
     */
    public Set<A> getRows() {
        return rows;
    }

    /**
     * Returns a set of all the column which have been added
     * @return a set containing all the column labels
     */
    public Set<B> getColumns() {
        return cols;
    }

    /**
     * Returns the size of the grid - rows * cols
     * @return the size of the grid
     */
    public int size() {
        return rows.size() * cols.size();
    }

    /**
     * Returns whether or not the grid contains a row labeled {@code a}
     * @param a the row in question
     * @return if the grid contains row {@code a}
     */
    public Boolean containsRow(A a) { return rows.contains(a); }

    /**
     * Returns whether or not the grid contains a column labeled {@code b}
     * @param b the column in question
     * @return if the grid contains column {@code b}
     */
    public Boolean containsColumn(B b) { return cols.contains(b); }

    /**
     * Returns a boolean indicating whether or not the grid is empty
     * @return if the grid is empty
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public String toString() {
        return matrix.toString();
    }
}
