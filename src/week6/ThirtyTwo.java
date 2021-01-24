package week6;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.Entry.comparingByValue;

/**
 * Chapter 32, Double Map Reduce
 * Exercises in Programming Style
 *
 * 32.1: Java implementation of:
 * Input data is divided in blocks.
 * A map function applies a given worker function to each block of data, potentially in parallel.
 * The results of the many worker functions are reshuffled.
 * The reshuffled blocks of data are given as input to a second map function that takes a reducible function as input.
 *
 * 32.3: Different regrouping
 * Change the example program so that the regroup function recognizes the words alphabetically into only five groups: a-e, f-j, k-o, p-t, u-z.
 * Be mindful of what this does to the counting step.
 *
 * @author Brooke Ryan, brooke.ryan@uci.edu
 */
public class ThirtyTwo {

    // By default, logging off.  To set logging, pass in "logging" as a command line argument.
    static boolean logging = false;
    public static final String STOP_WORDS_PATH = "../stop_words.txt";

    // Groups for mapping
    public static final List<String> group1 = List.of("a", "b", "c", "d", "e");
    public static final List<String> group2 = List.of("f", "g", "h", "i", "j");
    public static final List<String> group3 = List.of("k", "l", "m", "n", "o");
    public static final List<String> group4 = List.of("p", "q", "r", "s", "t");
    public static final List<String> group5 = List.of("u", "v", "w", "x", "y", "z");

    public static void log(Object message) {
        if (logging && message != null) {
            System.out.println(message.toString());
        }
    }

    /**
     * Partitions the input dataString into chunks of nLines.
     * @return A stream of "chunks".
     */
    public Stream<String> partition(String dataString, int nLines) {
        String[] lines = dataString.split("\n");
        List<String> chunks = new ArrayList<>();
        int range = 0;

        while (range < lines.length) {
            // Doing this to prevent index out of bounds exception in cases towards end of file
            int min = Integer.min(range + nLines, lines.length);
            StringJoiner stringJoiner = new StringJoiner("\n");
            for (int i=range; i<min; i++) {
                stringJoiner.add(lines[i]);
            }
            chunks.add(stringJoiner.toString());
            range = min;
        }

        return chunks.stream();
    }

    /**
     * Takes a String, returns a list of pairs (word,1), one for each word in the input, so
     * [(w_1,1), (w_2,1), ..., (w_n,1)]
     * @param dataString: This is a "chunk", a large string composed of nLines, from the partition method.
     * @return List of pairs, one for each word in the input (excluding stop words)
     */
    public static List<Pair<String,Integer>> splitWords(String dataString) {

        List<String> stopWords = new ArrayList<>();
        try {
            stopWords = Arrays.asList(Files.readString(Paths.get(STOP_WORDS_PATH)).split(","));
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> finalStopWords = stopWords;
        return Arrays.stream(dataString.replaceAll("[^A-Za-z0-9 ]", " ")
                .toLowerCase()
                .split(" ")).filter(word -> !finalStopWords.contains(word) && word.length()>1)
                .map(word -> new Pair<>(word, 1))
                .collect(Collectors.toList());
    }

    /**
     * Takes a list of lists of pairs of the form
     * [ [(w_1,1), (w_2,1), ..., (w_n,1)],
     *   [(w_1,1), (w_2,1), ..., (w_n,1)],
     *   ...]
     * and returns a dictionary mapping each unique word to the corresponding list of pairs, so
     * {
     *     w_1: [(w_1,1), (w_1,1), ...],
     *     w_2: [(w_2,1), (w_2,1), ...],
     *     ...}
     */
    public Map<String, List<Pair<String,Integer>>> regroup(List<List<Pair<String, Integer>>> pairsList) {
        Map<String, List<Pair<String,Integer>>> grouping = new HashMap<>();

        grouping.put(group1.toString(), new ArrayList<>());
        grouping.put(group2.toString(), new ArrayList<>());
        grouping.put(group3.toString(), new ArrayList<>());
        grouping.put(group4.toString(), new ArrayList<>());
        grouping.put(group5.toString(), new ArrayList<>());

        pairsList.stream()
                .flatMap(Collection::stream)        // now we have a Stream of our pairs
                .forEach(pair -> grouping.putAll(alphabetize(pair.first, grouping)));

        return grouping;
    }

    /**
     * Utility function to help with organizing grouping
     * Given a word, put it in its appropriate alphabetical group
     */
    public Map<String, List<Pair<String,Integer>>> alphabetize(String word, Map<String, List<Pair<String, Integer>>> grouping) {

        String firstLetter = String.valueOf(word.charAt(0));

        if (group1.contains(firstLetter)) {
            grouping = addPairToListAndUpdateGroup(word, grouping, group1);
        } else if (group2.contains(firstLetter)) {
            grouping = addPairToListAndUpdateGroup(word, grouping, group2);
        } else if (group3.contains(firstLetter)) {
            grouping = addPairToListAndUpdateGroup(word, grouping, group3);
        } else if (group4.contains(firstLetter)) {
            grouping = addPairToListAndUpdateGroup(word, grouping, group4);
        } else if (group5.contains(firstLetter)) {
            grouping = addPairToListAndUpdateGroup(word, grouping, group5);
        }

        return grouping;
    }

    public Map<String, List<Pair<String,Integer>>> addPairToListAndUpdateGroup(String word, Map<String, List<Pair<String,Integer>>> grouping, List<String> alphaGroup) {
        List<Pair<String, Integer>> pairs = grouping.get(alphaGroup.toString());
        pairs.add(new Pair<>(word, 1));
        grouping.put(alphaGroup.toString(), pairs);
        return grouping;
    }

    /**
     * Counts the words within its word group.
     */
    public List<Pair<String, Integer>> countWords(List<Pair<String,Integer>> listOfPairsInAlphaGroup) {
        return new ArrayList<>(listOfPairsInAlphaGroup.stream()
                .collect(Collectors
                        .toMap(Pair::getFirst,                                  // a mapping function to produce keys
                                pair -> pair,                                   // a mapping function to produce values
                                // a merge function, used to resolve collisions between values associated with the same key, as supplied
                                (pair1, pair2) -> new Pair<>(pair1.getFirst(), pair1.getSecond() + pair2.getSecond())))
                .values());
    }

    /**
     * Auxiliary functions
     */
    public String readFile(String pathToFile) throws IOException {
        return Files.readString(Paths.get(pathToFile));
    }

    public void sort(Map<String, Integer> wordFreq) {
        wordFreq.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .limit(25)
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
    }

    /**
     * The main function
     */
    public static void main (String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Too few arguments, please consult the README");
            return;
        } else if (args.length ==2 && args[1].equals("logging")) {      // for my own debugging, not part of assignment
            logging = true;
            log("logging set to true");
        }

        new ThirtyTwo().run(args[0]);
    }

    public void run(String arg) throws IOException {
        List<List<Pair<String, Integer>>> splits = partition(readFile(arg), 200)
                .map(ThirtyTwo::splitWords).collect(Collectors.toList());

        Map<String, List<Pair<String, Integer>>> splitsPerAlphaGroup = regroup(splits);

        List<Pair<String, Integer>> wordFreqs = splitsPerAlphaGroup.values().stream().map(this::countWords).collect(ArrayList::new, List::addAll, List::addAll);

        wordFreqs.sort((p1, p2) -> -p1.getSecond().compareTo(p2.getSecond()));
        for (int i = 0; i < 25; i++) {
            Pair<String, Integer> freq = wordFreqs.get(i);
            System.out.println(freq.getFirst() + " - " + freq.getSecond());
        }
    }

    /**
     * Helper class to implement Pair, basically just copied the Kotlin class to Java.
     */
    public static class Pair<K,V> implements Serializable {
        private final K first;
        private final V second;

        public final K getFirst() {
            return this.first;
        }

        public final V getSecond() {
            return this.second;
        }

        public Pair<K,V> of(K first, V second) {
            return new Pair<>(first, second);
        }

        public Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }

    }


}
