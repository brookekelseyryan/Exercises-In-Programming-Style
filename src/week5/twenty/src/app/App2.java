package week5.twenty.src.app;

import week5.twenty.src.framework.TFApplication;

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
 * This application uses BufferedReader instead of Files.
 */
public class App2 extends TFApplication {
    @Override
    public Stream<String> extractWords(String pathToFile) throws IOException {
        File file = new File(pathToFile);
        if (!file.exists()) {
            throw new FileNotFoundException("Pride and Prejudice file not found! Consult the README and ensure you are in the correct directory.");
        }

        List<String> stopWords = Arrays.asList(Files.readString(Paths.get("stop_words.txt")).split(","));

        return new BufferedReader(new FileReader(pathToFile)).lines()
                .map(line -> line.toLowerCase())
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .map(word -> word.replace("[^a-zA-Z0-9]", " "))
                .filter(word -> !stopWords.contains(word))
                .filter(word -> word.length() >= 2);
    }

    @Override
    public Stream<Map.Entry<String, Long>> top25Words(Stream<String> words) {
        return words.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .limit(25);
    }
}
