package uk.co.rafilevy.MarkovModels;

import uk.co.rafilevy.utils.ValueGrid.MutableIntGrid;
import uk.co.rafilevy.utils.ValueGrid.ValueGrid;

import java.util.*;

/**
 * A model representing an ordered sequence of states with a fixed probability of transitioning from a state to a consecutive state
 * The class can be used to generate sequences probabilistically following the model
 * @see <a href="https://en.wikipedia.org/wiki/Markov_chain">en.wikipedia.org/wiki/Markov_chain</a>
 * @param <A> the superset of the model's states
 */
public class MarkovModel<A> {

    private Random randomGenerator;

    private ValueGrid<A, A, Double> transitionMatrix;

    /**
     * Creates a new MarkovModel identical to an existing one
     * used for creating subclass instances from static factory methods of this class
     * @param model the model to be copied
     */
    protected MarkovModel(MarkovModel<A> model) {
        this.transitionMatrix = model.transitionMatrix;
        this.randomGenerator = model.randomGenerator;
    }

    /**
     * Creates a model given a ValueGrid representing probabilities of transitioning between any two given states
     * @param transitionMatrix the ValueGrid storing state transition probabilities
     */
    private MarkovModel(ValueGrid<A, A, Double> transitionMatrix) {
        this.transitionMatrix = transitionMatrix;
        randomGenerator = new Random();
    }

    /**
     * A static factory method creating a new MarkovModel given a ValueGrid which has counted the number of state transitions between any two given states
     * Converts these occurrences into probabilities as if they are entirely representative of the model
     * @param occurrenceMatrix the ValueGrid storing the number of transitions between states in a given set of state sequences
     * @param <T> the superset of the model's states
     * @return a new model created from the occurrence ValueGrid
     */
    private static<T> MarkovModel<T> fromOccurrenceMatrix(ValueGrid<T, T, Integer> occurrenceMatrix) {
        ValueGrid<T, T, Double> transitionMatrix = new ValueGrid<>();
        for (T row : occurrenceMatrix.getRows()) {
            double total = occurrenceMatrix.getRow(row).values().stream().mapToInt(Integer::intValue).sum();
            for (T col : occurrenceMatrix.getRow(row).keySet()) transitionMatrix.set(row, col, (double)occurrenceMatrix.get(row, col) / total);
        }
        return new MarkovModel<>(transitionMatrix);
    }

    /**
     * A static factory method creating a new MarkovModel given a collection of state sequences
     * @param sequences a collection of state sequences to create the model from
     * @param <T> the superset of the model's states
     * @return a new model created from the state sequences
     */
    public static<T> MarkovModel<T> fromSequences(Collection<List<T>> sequences) {
        MutableIntGrid<T, T> occurrenceMatrix = new MutableIntGrid<>();
        for (List<T> sequence : sequences) {
            for (int i = 0; i<sequence.size()-1; i++) occurrenceMatrix.add(sequence.get(i), sequence.get(i+1), 1);
        }
        return MarkovModel.fromOccurrenceMatrix(occurrenceMatrix);
    }

    /**
     * Generates a random sequence according to the model's transition probabilities with a fixed length
     * @param startState the state to start the sequence with
     * @param length the length of the sequence generated
     * @return a generated state sequence
     */
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

    /**
     * Generates a random sequence according to the model's transition probabilities with a given start and end state
     * The sequence will be continually generated until the end state is reached at which point it will terminate
     * @param startState the state to start the sequence with
     * @param endState the state to end the sequence on
     * @return a generated state sequence
     */
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
