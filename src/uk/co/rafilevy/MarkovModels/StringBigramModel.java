package uk.co.rafilevy.MarkovModels;

import uk.co.rafilevy.utils.Pair.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An extension of a MarkovModel to be used specifically for text generation
 * Very similar to {@link StringMarkovModel} except works with transitions of sequences of consecutive string rather than single strings
 * Will create sequences more closely related to original set but at higher risk of over-fitting
 */
public class StringBigramModel extends MarkovModel<Pair<String>> implements TextGenerativeModel {
    private static final Pair<String> defaultStartPair = new Pair("\\s", "\\s");
    private static final Pair<String> defaultEndPair = new Pair("\\e", "\\e");

    private final Pair<String> startPair;
    private final Pair<String> endPair;

    private StringBigramModel(MarkovModel<Pair<String>> model, Pair<String> startPair, Pair<String> endPair) {
        super(model);
        this.startPair = startPair;
        this.endPair = endPair;
    }

    private static List<Pair<String>> stringToBigramSequence(String s, String regex, Pair<String> startPair, Pair<String> endPair) {
        List<Pair<String>> bigramSequence = new ArrayList<>();

        String[] splitString = s.split(regex);

        bigramSequence.add(startPair);
        bigramSequence.add(new Pair(startPair.getSecond(), splitString[0])); //Adds start string -> first string
        for (int i = 0; i<splitString.length-1; i++) bigramSequence.add( new Pair(splitString[i], splitString[i+1]) ); //Adds all pairs
        bigramSequence.add( new Pair(splitString[splitString.length-1], endPair.getSecond()) ); //Adds last string -> end string
        bigramSequence.add(endPair);

        return bigramSequence;
    }

    private static StringBigramModel fromStrings(Collection<String> strings, String regex, Pair<String> startPair, Pair<String> endPair) {
        Set<List<Pair<String>>> bigramSequences = strings.stream().map( (String s) -> StringBigramModel.stringToBigramSequence(s, regex, startPair, endPair) ).collect(Collectors.toSet());
        return new StringBigramModel(MarkovModel.fromSequences(bigramSequences), startPair, endPair);
    }

    /**
     * A static factory method to create a model given a collection of string state sequences to be split into sequences with a given regex
     * @param strings the collection of string state sequences
     * @param regex the regex used to split the strings into actual state sequences
     * @param startString the unique string not seen anywhere else in the sequences used internally indicate the start of a sentence
     * @param endString the unique string not seen anywhere else in the sequences used to indicate the start of a sentence
     * @return a {@code StringBigramModel} used for generating string sequences according to the transition probabilities of string pairs calculated from the given collection of string sequences
     */
    public static StringBigramModel fromStrings(Collection<String> strings, String regex, String startString, String endString) { return StringBigramModel.fromStrings(strings, regex, new Pair(startString, startString), new Pair(endString, endString)); }

    /**
     * A static factory method to create a model given a collection of string state sequences to be split into sequences with a given regex using a default start and end string pair
     * @param strings the collection of string state sequences
     * @param regex the regex used to split the strings into actual state sequences
     * @return a {@code StringBigramModel} used for generating string sequences according to the transition probabilities of string pairs calculated from the given collection of string sequences
     */
    public static StringBigramModel fromStrings(Collection<String> strings, String regex) { return StringBigramModel.fromStrings(strings, regex, defaultStartPair, defaultEndPair); }

    /**
     * Generates a random string sequence using the model's probabilities of transitioning from a given string pair to another
     * @return a generated string sequence concatenated into a single string
     */
    public String generateString() {
        List<Pair<String>> sequence = super.generateSequence(startPair, endPair);
        sequence = sequence.subList(1, sequence.size()-1);
        String generated = "";
        for (int i = 0; i<sequence.size()-1; i++) generated += sequence.get(i).getSecond() + " ";
        return generated.substring(0, generated.length()-1);
    }

}
