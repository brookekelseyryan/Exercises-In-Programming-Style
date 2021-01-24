package week5.twenty.src.app;

import week5.twenty.src.framework.TFApplication;

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
 * I used my Cookbook (Five.java) code for this application.
 */
public class App1 extends TFApplication {
    @Override
    public Stream<String> extractWords(String pathToFile) throws IOException {
        File file = new File(pathToFile);
        if (!file.exists()) {
            throw new FileNotFoundException("Pride and Prejudice file not found! Consult the README and ensure you are in the correct directory.");
        }
        String fileContents = Files.readString(Paths.get(pathToFile));

        List<String> stopWords = Arrays.asList(Files.readString(Paths.get("stop_words.txt")).split(","));

        return Arrays.stream(fileContents.toLowerCase()
                .replaceAll("[^A-Za-z0-9 ]", " ")
                .split(" "))
                .filter(word -> !stopWords.contains(word))
                .filter(word -> word.length() >= 2);
    }

    @Override
    public Stream<Map.Entry<String, Long>> top25Words(Stream<String> words) {
        return words.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .limit(25);    }
}
