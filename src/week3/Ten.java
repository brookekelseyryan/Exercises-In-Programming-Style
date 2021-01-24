package week3;

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.Map.Entry.comparingByValue;

/**
 * The One
 **/
public class Ten {

    public static final String STOP_WORDS_FILE_PATH = "stop_words.txt";

    // Monad
    // Must consist of a constructor, binding function, and unwrapping function
    public static class TermFrequencyCounter {
        // Value that is wrapped in the Monad
        Object value;

        // Constructor
        public TermFrequencyCounter(Object value) {
            this.value = value;
        }

        // Binding function
        public TermFrequencyCounter bind(Function<Object, Object> func) {
            this.value = func.apply(value);
            return this;
        }

        // Unwrapping function
        public Object get() {
            return value;
        }
    }

    public static void main(String[] args) throws IOException {
        // To adhere to the Monadic style, only one TermFrequencyCounter is instantiated throughout the program
        Object termFrequencyCounterValue = new TermFrequencyCounter(readFile(args[0]))
                .bind(Words::normalizeData)
                .bind(Words::removeShortWords)
                .bind(Words::removeStopWords)
                .bind(Words::countTermFrequencies)
                .get();
        // Sort and print the unwrapped TermFrequencyCounter
        sortAndPrintTop25(termFrequencyCounterValue);
    }

    /**
     * Initializes the File object for Pride and Prejudice, and puts the entirety of its contents into a string.
     */
    public static Object readFile(Object prideAndPrejudicePath) throws IOException {
        File file = new File((String) prideAndPrejudicePath);
        if (!file.exists()) {
            throw new FileNotFoundException("Pride and Prejudice file not found! Consult the README and ensure you are in the correct directory.");
        }
        return Files.readString(Paths.get((String) prideAndPrejudicePath));
    }

    /**
     * Sorts the term frequencies in descending order by count, and prints the top 25 number of terms.
     */
    public static void sortAndPrintTop25(Object obj) {
        @SuppressWarnings("unchecked")
        Map<String, Long> termFrequencies = (Map<String, Long>) obj;
        termFrequencies.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .limit(25)
                .forEach(entry -> System.out.println(entry.getKey()+" - "+entry.getValue()));
    }

    public static class Words {
        /**
         * Replaces all non-alphanumeric characters with whitespace, converts all text to lowercase, and separates into words.
         */
        public static Stream<String> normalizeData(Object fileContents) {
            String fileContents1 = (String) fileContents;
            return Arrays.stream(fileContents1.toLowerCase()
                    .replaceAll("[^A-Za-z0-9 ]", " ")
                    .split(" "));
        }

        /**
         * Removes all words that are in the stop words list.
         */
        public static Stream<String> removeStopWords(Object obj) {
            @SuppressWarnings("unchecked")
            Stream<String> words = (Stream<String>) obj;
            List<String> stopWords = null;
            try {
                stopWords = Arrays.asList(Files.readString(Paths.get(STOP_WORDS_FILE_PATH)).split(","));
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<String> finalStopWords = stopWords;
            return words.filter(word -> !finalStopWords.contains(word));
        }

        /**
         * Removes all words less than 2 characters.
         */
        public static Stream<String> removeShortWords(Object obj) {
            @SuppressWarnings("unchecked")
            Stream<String> words = (Stream<String>) obj;
            return words.filter(word -> word.length() >= 2);
        }

        /**
         * Counts the occurrences of each word.
         */
        public static Map<String, Long> countTermFrequencies(Object obj) {
            @SuppressWarnings("unchecked")
            Stream<String> words = (Stream<String>) obj;
            return words.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        }

    }
}