package uk.co.rafilevy.MarkovModels;

import java.util.*;

/**
 * An extension of a MarkovModel to be used specifically for text generation
 * State sequences are supplied as strings rather than lists and can be decoded by using a regex to split the string into a sequence
 * Sequences are then generated as normal and turned into strings by concatenating them with a " " in between
 * Useful for generating texts in the style of other text corpora
 */
public class StringMarkovModel extends MarkovModel<String> implements TextGenerativeModel {

    private static final String defaultStartString = "\\s";
    private static final String defaultEndString = "\\e";

    private final String startString;
    private final String endString;

    private StringMarkovModel(MarkovModel<String> model, String startString, String endString) {
        super(model);
        this.startString = startString;
        this.endString = endString;
    }

    /**
     * A static factory method to create a model given a collection of string state sequences to be split into sequences with a given regex
     * @param strings the collection of string state sequences
     * @param regex the regex used to split the strings into actual state sequences
     * @param startString the unique string not seen anywhere else in the sequences used internally indicate the start of a sentence
     * @param endString the unique string not seen anywhere else in the sequences used to indicate the start of a sentence
     * @return a StringMarkovModel used for generating string sequences according to the transition probabilities calculated from the given collection of string sequences
     */
    public static StringMarkovModel fromStrings(Collection<String> strings, String regex, String startString, String endString) {
        Set<List<String>> sequences = new HashSet<>();
        for (String string : strings) {
            List<String> sequence = new ArrayList<>();
            sequence.add(startString);
            for (String item : string.split(regex)) sequence.add(item);
            sequence.add(endString);
            sequences.add(sequence);
        }
        return new StringMarkovModel(MarkovModel.fromSequences(sequences), startString, endString);
    }

    /**
     * A static factory method to create a model given a collection of string state sequences to be split into sequences with a given regex with a default start and end string
     * @param strings the collection of string state sequences
     * @param regex the regex used to split the strings into actual state sequences
     * @return a StringMarkovModel used for generating string sequences according to the transition probabilities calculated from the given collection of string sequences
     */
    public static StringMarkovModel fromStrings(Collection<String> strings, String regex) { return StringMarkovModel.fromStrings(strings, regex, defaultStartString, defaultEndString); }

    /**
     * Generates a random string sequence using the model's probabilities of transitioning from a given string to another
     * @return a generated string sequence concatenated into a single string
     */
    public String generateString() {
        List<String> sequence = super.generateSequence(startString, endString);
        sequence = sequence.subList(1, sequence.size()-1);
        return String.join(" ", sequence);
    }

}
