package uk.co.rafilevy.MarkovModels;

import uk.co.rafilevy.utils.Pair.Pair;

import java.util.*;
import java.util.stream.Collectors;

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

    public static StringBigramModel fromStrings(Collection<String> strings, String regex) { return StringBigramModel.fromStrings(strings, regex, defaultStartPair, defaultEndPair); }

    public static StringBigramModel fromStrings(Collection<String> strings, String regex, String startString, String endString) { return StringBigramModel.fromStrings(strings, regex, new Pair(startString, startString), new Pair(endString, endString)); }

    public String generateString() {
        List<Pair<String>> sequence = super.generateSequence(startPair, endPair);
        sequence = sequence.subList(1, sequence.size()-1);
        String generated = "";
        for (int i = 0; i<sequence.size()-1; i++) generated += sequence.get(i).getSecond() + " ";
        return generated.substring(0, generated.length()-1);
    }

}
