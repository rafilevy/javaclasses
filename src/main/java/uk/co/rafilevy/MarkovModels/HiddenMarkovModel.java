package uk.co.rafilevy.MarkovModels;

import uk.co.rafilevy.utils.ValueGrid.MutableIntGrid;
import uk.co.rafilevy.utils.ValueGrid.UnknownKeyException;
import uk.co.rafilevy.utils.ValueGrid.ValueGrid;

import java.util.*;

/**
 * A derivative of a MarkovModel where there is a hidden ordered sequence of states of type  with fixed transition probabilities between states
 * These hidden states each emit a value such that there is an observed ordered sequence of emitted values
 * The class can be used to generate sequences probabilistically following the model or to decode an emitted sequence giving the most probable hidden sequence that generated it
 * @see <a href="https://en.wikipedia.org/wiki/Hidden_Markov_model">en.wikipedia.org/wiki/Hidden_Markov_model</a>
 * @param <H> The superset of the hidden sequence's states
 * @param <E> The superset of the emitted sequence's states
 */
public class HiddenMarkovModel<H, E> {

    private Random randomGenerator;

    private ValueGrid<H, H, Double> transitionMatrix;
    private ValueGrid<H, E, Double> emissionMatrix;

    private HiddenMarkovModel(ValueGrid<H, H, Double> transitionMatrix, ValueGrid<H, E, Double> emissionMatrix, long randomSeed) {
        this.transitionMatrix = transitionMatrix;
        this.emissionMatrix = emissionMatrix;
        randomGenerator = new Random(randomSeed);
    }

    private HiddenMarkovModel(ValueGrid<H, H, Double> transitionMatrix, ValueGrid<H, E, Double> emissionMatrix) {
        this.transitionMatrix = transitionMatrix;
        this.emissionMatrix = emissionMatrix;
        randomGenerator = new Random();
    }

    private static<HT, ET> HiddenMarkovModel<HT, ET> fromOccurrenceMatrices(ValueGrid<HT, HT, Integer> transitionOccurrenceMatrix, ValueGrid<HT, ET, Integer> emissionOccurrenceMatrix) {
        ValueGrid<HT, HT, Double> transitionMatrix = new ValueGrid<>();
        ValueGrid<HT, ET, Double> emissionMatrix = new ValueGrid<>();
        for (HT row : transitionOccurrenceMatrix.getRows()) {
            double total = transitionOccurrenceMatrix.getRow(row).values().stream().mapToInt(Integer::intValue).sum();
            for (HT col : transitionOccurrenceMatrix.getRow(row).keySet())
                transitionMatrix.set(row, col, (double) transitionOccurrenceMatrix.get(row, col) / total);
        }
        for (HT row : emissionOccurrenceMatrix.getRows()) {
            double total = emissionOccurrenceMatrix.getRow(row).values().stream().mapToInt(Integer::intValue).sum();
            for (ET col : emissionOccurrenceMatrix.getRow(row).keySet())
                emissionMatrix.set(row, col, (double) emissionOccurrenceMatrix.get(row, col) / total);
        }

        return new HiddenMarkovModel<>(transitionMatrix, emissionMatrix);
    }

    /**
     * A static factory method which creates a model given pairs of hidden and emitted sequences which define the model's transition and emission probabilities
     * @param sequences a mapping of hidden sequences and the emission sequences they generated which the model's probabilities will be defined from
     * @param <HT> the superset of the hidden sequence's states
     * @param <ET> the superset of the emitted sequence's states
     * @return a model with probabilities defined by the given sequence pairs
     */
    public static<HT, ET> HiddenMarkovModel<HT, ET> fromSequences(Map<List<HT>, List<ET>> sequences) {
        MutableIntGrid<HT, HT> transitionOccurrences = new MutableIntGrid<>();
        MutableIntGrid<HT, ET> emissionOccurrences = new MutableIntGrid<>();

        for (Map.Entry<List<HT>, List<ET>> sequencePair : sequences.entrySet()) {
            List<HT> hiddenSequence = sequencePair.getKey();
            List<ET> emissionSequence = sequencePair.getValue();
            for (int i = 0; i<hiddenSequence.size()-1; i++) {
                transitionOccurrences.add(hiddenSequence.get(i), hiddenSequence.get(i+1), 1);
                emissionOccurrences.add(hiddenSequence.get(i), emissionSequence.get(i), 1);
            }
            emissionOccurrences.add(hiddenSequence.get(hiddenSequence.size()-1), emissionSequence.get(emissionSequence.size()-1), 1);
        }

        return HiddenMarkovModel.fromOccurrenceMatrices(transitionOccurrences, emissionOccurrences);
    }

    /**
     * Generates a random emitted sequence following the model's probabilities with a fixed length
     * @param startState the state to start the hidden sequence with
     * @param length the length of the generated sequence
     * @return a randomly generated sequence
     */
    public List<E> generateSequence(H startState, int length) {
        List<E> emissionSequence = new ArrayList<>();
        H current = startState;
        emissionSequence.add(randomEmission(current));
        while (emissionSequence.size() < length) {
            current = randomTransition(current);
            emissionSequence.add(randomEmission(current));
        }
        return emissionSequence;
    }

    /**
     * Generates a random emitted sequence following the model's probabilities with a given start and end state
     * @param startState the state to start the hidden sequence with
     * @param endState the hidden state after which the sequence ends
     * @return a randomly generated sequence
     */
    public List<E> generateSequence(H startState, H endState) {
        List<E> emissionSequence = new ArrayList<>();
        H current = startState;
        emissionSequence.add(randomEmission(current));
        while (!current.equals(endState)) {
            current = randomTransition(current);
            emissionSequence.add(randomEmission(current));
        }
        return emissionSequence;
    }

    /**
     * Calculates the most probable hidden state sequence which may have generated a given emitted sequence
     * @see <a href="https://en.wikipedia.org/wiki/Viterbi_algorithm">en.wikipedia.org/wiki/Viterbi_algorithm</a>
     * @param sequence the emitted sequence to be decoded
     * @return the most probable hidden state sequence which generated the given emitted sequence
     */
    public List<H> decodeSequence(List<E> sequence) {
        LinkedList<Map<H, H>> helperFunction = new LinkedList<>();
        Map<H, Double> prevProbabilityMap = new HashMap<>();

        Set<H> hiddenStates = transitionMatrix.getRows();

        //For initial state (with no transition into it)
        for (H hiddenState : hiddenStates) {
            prevProbabilityMap.put(hiddenState, Math.log(emissionMatrix.get(hiddenState, sequence.get(0))));
        }

        for (int i = 1; i<sequence.size(); i++) {
            helperFunction.addFirst(new HashMap<>());
            Map<H, Double> probabilityMap = new HashMap<>();

            for (H hiddenState : hiddenStates) {
                H maxProbPrevState = null;
                double maxLogProb = Double.NEGATIVE_INFINITY;
                for (H prevHiddenState : hiddenStates) {
                    try {
                        double logProb = prevProbabilityMap.get(prevHiddenState) + Math.log(transitionMatrix.get(prevHiddenState, hiddenState)) + Math.log(emissionMatrix.get(hiddenState, sequence.get(i)));
                        if (logProb > maxLogProb) {
                            maxLogProb = logProb;
                            maxProbPrevState = prevHiddenState;
                        }
                    } catch (UnknownKeyException e) { continue; }

                }

                probabilityMap.put(hiddenState, maxLogProb);
                helperFunction.getFirst().put(hiddenState, maxProbPrevState);
            }
            prevProbabilityMap = probabilityMap;
        }

        LinkedList<H> hiddenSequence = new LinkedList<>();
        H current = prevProbabilityMap.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey();
        hiddenSequence.addFirst(current);
        for (Map<H, H> helperMap : helperFunction) hiddenSequence.addFirst((current = helperMap.get(current)));

        return hiddenSequence;
    }

    private H randomTransition(H state) {
        double rand = randomGenerator.nextDouble();
        for (Map.Entry<H, Double> transitionProb : transitionMatrix.getRow(state).entrySet()) {
            rand -= transitionProb.getValue();
            if (rand <= 0) {
                return transitionProb.getKey();
            }
        }
        return null;
    }

    private E randomEmission(H state) {
        double rand = randomGenerator.nextDouble();
        for (Map.Entry<E, Double> emissionProb : emissionMatrix.getRow(state).entrySet()) {
            rand -= emissionProb.getValue();
            if (rand <= 0) {
                return emissionProb.getKey();
            }
        }
        return null;
    }
}
