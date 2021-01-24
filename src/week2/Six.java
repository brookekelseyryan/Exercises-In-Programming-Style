package week2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.Entry.comparingByValue;

/**
 * @author brookekelseyryan
 *
 * "Pipeline" - Chapter 6, Exercises in Programming Style
 * Functional programming style.
 * Constraints:
 * Larger problem is decomposed using functional abstraction.  Functions take input and produce output.
 * No shared state between functinos.
 * The larger problem is solved by composing functions one after the other, in a pipeline, as a faithful reproduction of mathematical function composition.
 */
public class Six {
    // Immutable constant
    public static final String STOP_WORDS_FILE_PATH = "stop_words.txt";

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("No command-line arguments found! Consult the README and ensure you are passing in the correct arguments.");
            return;
        }
        sortAndPrintTop25(countTermFrequencies(removeShortWords(removeStopWords(normalizeData(readFile(args[0]))))));
    }

    /**
     * Initializes the File object for Pride and Prejudice, and puts the entirety of its contents into a string.
     */
    public static String readFile(String prideAndPrejudicePath) throws IOException {
        File file = new File(prideAndPrejudicePath);
        if (!file.exists()) {
            throw new FileNotFoundException("Pride and Prejudice file not found! Consult the README and ensure you are in the correct directory.");
        }
        return Files.readString(Paths.get(prideAndPrejudicePath));
    }

    /**
     * Replaces all non-alphanumeric characters with whitespace, converts all text to lowercase, and separates into words.
     */
    public static Stream<String> normalizeData(String fileContents) {
        return Arrays.stream(fileContents.toLowerCase()
                .replaceAll("[^A-Za-z0-9 ]", " ")
                .split(" "));
    }

    /**
     * Removes all words that are in the stop words list.
     */
    public static Stream<String> removeStopWords(Stream<String> words) throws IOException {
        List<String> stopWords = Arrays.asList(Files.readString(Paths.get(STOP_WORDS_FILE_PATH)).split(","));
        return words.filter(word -> !stopWords.contains(word));
    }

    /**
     * Removes all words less than 2 characters.
     */
    public static Stream<String> removeShortWords(Stream<String> words) {
        return words.filter(word -> word.length() >= 2);
    }

    /**
     * Counts the occurrences of each word.
     */
    public static Map<String, Long> countTermFrequencies(Stream<String> words) {
        return words.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * Sorts the term frequencies in descending order by count, and prints the top 25 number of terms.
     */
    public static void sortAndPrintTop25(Map<String, Long> termFrequencies) {
        termFrequencies.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .limit(25)
                .forEach(System.out::println);
    }
}