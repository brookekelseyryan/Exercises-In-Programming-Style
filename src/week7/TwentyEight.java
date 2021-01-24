package week7;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Map.Entry.comparingByKey;
import static java.util.Map.Entry.comparingByValue;

/**
 * @author Brooke Ryan, brooke.ryan@uci.edu
 *
 * "Lazy Rivers" - Chapter 28, Exercises in Programming Style
 *
 * Data is available in streams, rather than as a complete whole.
 * Functions are filters/transformers from one kind of data stream to  another.
 * Data is processed from upstream on a need basis from downstream.
 *
 * Wrote this program in Java because, while the language does not have a "yield" equivalent from Python, the
 * Streaming APIs in Java by definition meet the program constraints for Chapter 28. From the Java Documentation:
 *
 * "A stream pipeline consists of a source (which might be an array, a collection, a generator function, an I/O channel,
 * etc), zero or more intermediate operations (which transform a stream into another stream, such as filter(Predicate)),
 * and a terminal operation (which produces a result or side-effect, such as count() or forEach(Consumer)). Streams are
 * lazy; computation on the source data is only performed when the terminal operation is initiated, and source elements
 * are consumed only as needed." https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html
 *
 * This meets the constraints of the exercise because a series of values are produced over time, rather than computing
 * them all at once and sending them back like a list.  Streams in Java lazily execute the computation specified.
 */
public class TwentyEight {
    // Immutable constant
    public static final String STOP_WORDS_FILE_PATH = "../stop_words.txt";

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("No command-line arguments found! Consult the README and ensure you are passing in the correct arguments.");
            return;
        }

        countAndSortWords(args[0]).sorted(Collections.reverseOrder(comparingByValue()))
                .limit(25)
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));

    }

    /**
     * Exercise 28.2: Change the program, without  changing the style, so that the first generator yields an entire line.
     *
     * @return Stream<String> Files.Lines returns a Stream<String> which contains a reference to an open file. The file
     * is closed by closing the stream.
     */
    public static Stream<String> readFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("Pride and Prejudice file not found! Consult the README and ensure you are in the correct directory.");
        }
        return Files.lines(Paths.get(fileName));
    }

    /**
     * Exercise 28.2: Change the program, without changing the style, so that the second function yields words from those lines
     * using the proper library functions.
     *
     * @return Stream<String> Uses regex library function to replace all non-alphanumeric characters with whitespace,
     * converts all text to lowercase, and separates into words.
     */
    public static Stream<String> allWords(String fileName) throws IOException {
        return readFile(fileName)
                .map(String::toLowerCase)
                .map(line -> line.replaceAll("[^A-Za-z0-9 ]", " "))
                .flatMap(line -> Arrays.stream(line.split(" ")));
    }

    /**
     * Removes all words that are in the stop words list.
     */
    public static Stream<String> nonStopWords(String fileName) throws IOException {
        List<String> stopWords = Arrays.asList(Files.readString(Paths.get(STOP_WORDS_FILE_PATH)).split(","));

        return allWords(fileName).filter(word -> !stopWords.contains(word) && word.length() >= 2);
    }

    /**
     * Counts the occurrences of each word.
     * @return Returns a Stream of Map Entries that represent (Word, Count).
     */
    public static Stream<Map.Entry<String, Integer>> countAndSortWords(String fileName) throws IOException {
        Map<String, Integer> termFrequencyMap = new HashMap<>();

        nonStopWords(fileName).forEach(word -> {
            if (!termFrequencyMap.containsKey(word)) {
                termFrequencyMap.put(word, 1);
            } else {
                termFrequencyMap.put(word, termFrequencyMap.get(word) + 1);
            }
        });
        // This implementation differs from the professors' python solution, because Java does not have yield capabilities,
        // there's no way to return 5000 elements at a time.  So, we must compromise and send the whole map.
        return termFrequencyMap.entrySet().stream();
    }

}