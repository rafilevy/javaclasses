package uk.co.rafilevy.MarkovModels;

import java.util.*;
import java.util.stream.Collectors;

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

    public static StringMarkovModel fromStrings(Collection<String> strings, String regex) { return StringMarkovModel.fromStrings(strings, regex, defaultStartString, defaultEndString); }

    public String generateString() {
        List<String> sequence = super.generateSequence(startString, endString);
        sequence = sequence.subList(1, sequence.size()-1);
        return String.join(" ", sequence);
    }

}
