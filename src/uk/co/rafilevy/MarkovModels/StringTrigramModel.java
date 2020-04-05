package uk.co.rafilevy.MarkovModels;

import uk.co.rafilevy.utils.Pair.Trip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StringTrigramModel extends MarkovModel<Trip<String>> implements TextGenerativeModel {

    private static final Trip<String> defaultStartTrip = new Trip("\\s", "\\s", "\\s");
    private static final Trip<String> defaultEndTrip = new Trip("\\e", "\\e", "\\e");

    private final Trip<String> startTrip;
    private final Trip<String> endTrip;

    private StringTrigramModel(MarkovModel<Trip<String>> model, Trip<String> startTrip, Trip<String> endTrip) {
        super(model);
        this.startTrip = startTrip;
        this.endTrip = endTrip;
    }

    private static List<Trip<String>> stringToTrigramSequence(String s, String regex, Trip<String> startTrip, Trip<String> endTrip) {
        List<Trip<String>> trigramSequence = new ArrayList<>();

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

    private static StringTrigramModel fromStrings(Collection<String> strings, String regex, Trip<String> startTrip, Trip<String> endTrip) {
        Set<List<Trip<String>>> trigramSequences = strings.stream().map( (String s) -> StringTrigramModel.stringToTrigramSequence(s, regex, startTrip, endTrip) ).collect(Collectors.toSet());
        return new StringTrigramModel(MarkovModel.fromSequences(trigramSequences), startTrip, endTrip);
    }

    public static StringTrigramModel fromStrings(Collection<String> strings, String regex) { return StringTrigramModel.fromStrings(strings, regex, defaultStartTrip, defaultEndTrip); }

    public static StringTrigramModel fromStrings(Collection<String> strings, String regex, String startString, String endString) { return StringTrigramModel.fromStrings(strings, regex, new Trip(startString, startString, startString), new Trip(endString, endString, endString)); }

    public String generateString() {
        List<Trip<String>> sequence = super.generateSequence(startTrip, endTrip);
        sequence = sequence.subList(1, sequence.size()-1);
        String generated = "";
        for (int i = 0; i<sequence.size()-2; i++) generated += sequence.get(i).getThird() + " ";
        return generated.substring(0, generated.length()-1);
    }

}
