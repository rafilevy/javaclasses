package uk.co.rafilevy.MarkovModels;

import uk.co.rafilevy.utils.ValueGrid.MutableIntGrid;
import uk.co.rafilevy.utils.ValueGrid.ValueGrid;

import java.util.*;

public class MarkovModel<A> {

    private Random randomGenerator;

    private ValueGrid<A, A, Double> transitionMatrix;

    protected MarkovModel(MarkovModel<A> model) {
        this.transitionMatrix = model.transitionMatrix;
        this.randomGenerator = model.randomGenerator;
    }

    private MarkovModel(ValueGrid<A, A, Double> transitionMatrix) {
        this.transitionMatrix = transitionMatrix;
        randomGenerator = new Random();
    }

    private static<T> MarkovModel<T> fromOccurrenceMatrix(ValueGrid<T, T, Integer> occurrenceMatrix) {
        ValueGrid<T, T, Double> transitionMatrix = new ValueGrid<>();
        for (T row : occurrenceMatrix.getRows()) {
            double total = occurrenceMatrix.getRow(row).values().stream().mapToInt(Integer::intValue).sum();
            for (T col : occurrenceMatrix.getRow(row).keySet()) transitionMatrix.set(row, col, (double)occurrenceMatrix.get(row, col) / total);
        }
        return new MarkovModel<>(transitionMatrix);
    }

    public static<T> MarkovModel<T> fromSequences(Collection<List<T>> sequences) {
        MutableIntGrid<T, T> occurrenceMatrix = new MutableIntGrid<>();
        for (List<T> sequence : sequences) {
            for (int i = 0; i<sequence.size()-1; i++) occurrenceMatrix.add(sequence.get(i), sequence.get(i+1), 1);
        }
        return MarkovModel.fromOccurrenceMatrix(occurrenceMatrix);
    }

    public List<A> generateSequence(A startState, int length) {
        List<A> sequence = new ArrayList<>();
        sequence.add(startState);
        A current = startState;
        while (sequence.size() < length) {
            current = randomTransition(current);
            sequence.add(current);
        }
        return sequence;
    }

    public List<A> generateSequence(A startState, A endState) {
        List<A> sequence = new ArrayList<>();
        sequence.add(startState);
        A current = startState;
        while (!current.equals(endState)) {
            current = randomTransition(current);
            sequence.add(current);
        }
        return sequence;
    }

    private A randomTransition(A state) {
        double rand = randomGenerator.nextDouble();
        for (Map.Entry<A, Double> transitionProb : transitionMatrix.getRow(state).entrySet()) {
            rand -= transitionProb.getValue();
            if (rand <= 0) {
                return transitionProb.getKey();
            }
        }
        return null;
    }

}
