package uk.co.rafilevy.MarkovModels;

import uk.co.rafilevy.utils.ValueGrid.MutableIntGrid;
import uk.co.rafilevy.utils.ValueGrid.UnknownKeyException;
import uk.co.rafilevy.utils.ValueGrid.ValueGrid;

import java.util.*;

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

    public static void main(String[] args) {
        List<Integer> hiddenSeq = List.of(1,2,3,4,5,6);
        List<Integer> observedSeq = List.of(1,2,3,4,5,6);
        Map<List<Integer>, List<Integer>> pairing = Map.of(hiddenSeq, observedSeq);
        HiddenMarkovModel<Integer, Integer> hmm = HiddenMarkovModel.fromSequences(pairing);
        System.out.println(hmm.decodeSequence(List.of(1,2,3)));
    }
}
