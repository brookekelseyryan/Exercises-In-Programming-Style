package week2;

import java.io.*;
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
 * "Cookbook" - Chapter 5, Exercises in Programming Style.
 * Procedural programming style.
 *
 * Constraints:
 * Complexity of control flow tamed by dividing the larger problem into smaller units using procedural abstraction.
 * Procedures may share state in the form of global variables.
 * The larger problem is solved by consecutively applying the procedures that contribute to the shared state.
 */
public class Five {

    // Immutable constant
    public static final String STOP_WORDS_FILE_PATH = "stop_words.txt";

    // Shared mutable data
    public static File PRIDE_AND_PREJUDICE_FILE;
    public static String fileContents = "";
    public static List<String> stopWords = Collections.<String> emptyList();
    public static Map<String, Long> termFrequencies = Collections.<String,Long> emptyMap();
    public static Stream<String> words = Stream.empty();

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("No command-line arguments found! Consult the README and ensure you are passing in the correct arguments.");
            return;
        }
        readFile(args[0]);
        initializeStopWords();
        normalizeData();
        removeStopWords();
        removeShortWords();
        countTermFrequencies();
        sortAndPrintTop(25);
    }

    /**
     * Initializes the File object for Pride and Prejudice, and puts the entirety of its contents into a string.
     */
    public static void readFile(String prideAndPrejudicePath) throws IOException {
        PRIDE_AND_PREJUDICE_FILE = new File(prideAndPrejudicePath);
        if (!PRIDE_AND_PREJUDICE_FILE.exists()) {
            throw new FileNotFoundException("Pride and Prejudice file not found! Consult the README and ensure you are in the correct directory.");
        }
        fileContents = Files.readString(Paths.get(prideAndPrejudicePath));
    }

    /**
     * Initializes the globally accessible stop words as specified by the comma-separated values in stop_words.txt.
     * @throws IOException
     */
    public static void initializeStopWords() throws IOException {
        stopWords = Arrays.asList(Files.readString(Paths.get(STOP_WORDS_FILE_PATH)).split(","));
    }

    /**
     * Replaces all non-alphanumeric characters with whitespace, converts all text to lowercase, and separates into words.
     */
    public static void normalizeData() {
        words = Arrays.stream(fileContents.toLowerCase()
                .replaceAll("[^A-Za-z0-9 ]", " ")
                .split(" "));
    }

    /**
     * Removes all words that are in the stop words list.
     */
    public static void removeStopWords() {
        words = words.filter(word -> !stopWords.contains(word));
    }

    /**
     * Removes all words less than 2 characters.
     */
    public static void removeShortWords() {
        words = words.filter(word -> word.length() >= 2);
    }

    /**
     * Counts the occurrences of each word.
     */
    public static void countTermFrequencies() {
        termFrequencies = words.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * Sorts the term frequencies in descending order by count, and prints the top "num" number of terms.
     * @param num of terms to print to the console.
     */
    public static void sortAndPrintTop(int num) {
        termFrequencies.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .limit(num)
                .forEach(System.out::println);
    }
}