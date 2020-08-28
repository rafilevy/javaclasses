package uk.co.rafilevy.MarkovModels;

import uk.co.rafilevy.utils.Tuple.Trip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An extension of a MarkovModel to be used specifically for text generation
 * Very similar to {@link StringMarkovModel} except works with transitions of sequences of three strings rather than single strings
 * Will create sequences more closely related to original set but at high risk of over-fitting without very large training set of string sequences
 */
public class StringTrigramModel extends MarkovModel<Trip<String, String, String>> implements TextGenerativeModel {

    private static final Trip<String, String, String> defaultStartTrip = new Trip("\\s", "\\s", "\\s");
    private static final Trip<String, String, String> defaultEndTrip = new Trip("\\e", "\\e", "\\e");

    private final Trip<String, String, String> startTrip;
    private final Trip<String, String, String> endTrip;

    private StringTrigramModel(MarkovModel<Trip<String, String, String>> model, Trip<String, String, String> startTrip, Trip<String, String, String> endTrip) {
        super(model);
        this.startTrip = startTrip;
        this.endTrip = endTrip;
    }

    private static List<Trip<String, String, String>> stringToTrigramSequence(String s, String regex, Trip<String, String, String> startTrip, Trip<String, String, String> endTrip) {
        List<Trip<String, String, String>> trigramSequence = new ArrayList<>();

        String[] splitString = s.split(regex);

        trigramSequence.add(startTrip);
        trigramSequence.add( new Trip<>(startTrip.getFirst(), startTrip.getFirst(), splitString[0]) );
        trigramSequence.add( new Trip<>(startTrip.getFirst(), splitString[0], splitString[1]) );
        for (int i = 0; i<splitString.length-2; i++) trigramSequence.add( new Trip(splitString[i], splitString[i+1], splitString[i+2]) );
        trigramSequence.add( new Trip<>( splitString[splitString.length-2],  splitString[splitString.length-1], endTrip.getFirst()) );
        trigramSequence.add( new Trip<>( splitString[splitString.length-1], endTrip.getFirst(), endTrip.getFirst()) );
        trigramSequence.add(endTrip);

        return trigramSequence;
    }

    private static StringTrigramModel fromStrings(Collection<String> strings, String regex, Trip<String, String, String> startTrip, Trip<String, String, String> endTrip) {
        Set<List<Trip<String, String, String>>> trigramSequences = strings.stream().map( (String s) -> StringTrigramModel.stringToTrigramSequence(s, regex, startTrip, endTrip) ).collect(Collectors.toSet());
        return new StringTrigramModel(MarkovModel.fromSequences(trigramSequences), startTrip, endTrip);
    }

    /**
     * A static factory method to create a model given a collection of string state sequences to be split into sequences with a given regex
     * @param strings the collection of string state sequences
     * @param regex the regex used to split the strings into actual state sequences
     * @param startString the unique string not seen anywhere else in the sequences used internally indicate the start of a sentence
     * @param endString the unique string not seen anywhere else in the sequences used to indicate the start of a sentence
     * @return a {@code StringTrigramModel} used for generating string sequences according to the transition probabilities of string triples calculated from the given collection of string sequences
     */
    public static StringTrigramModel fromStrings(Collection<String> strings, String regex, String startString, String endString) { return StringTrigramModel.fromStrings(strings, regex, new Trip(startString, startString, startString), new Trip(endString, endString, endString)); }

    /**
     * A static factory method to create a model given a collection of string state sequences to be split into sequences with a given regex using a default start and end string
     * @param strings the collection of string state sequences
     * @param regex the regex used to split the strings into actual state sequences
     * @return a {@code StringTrigramModel} used for generating string sequences according to the transition probabilities of string triples calculated from the given collection of string sequences
     */
    public static StringTrigramModel fromStrings(Collection<String> strings, String regex) { return StringTrigramModel.fromStrings(strings, regex, defaultStartTrip, defaultEndTrip); }

    /**
     * Generates a random string sequence using the model's probabilities of transitioning from a given string triple to another
     * @return a generated string sequence concatenated into a single string
     */
    public String generateString() {
        List<Trip<String, String, String>> sequence = super.generateSequence(startTrip, endTrip);
        sequence = sequence.subList(1, sequence.size()-1);
        String generated = "";
        for (int i = 0; i<sequence.size()-2; i++) generated += sequence.get(i).getThird() + " ";
        return generated.substring(0, generated.length()-1);
    }

}
