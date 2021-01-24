package week5.twenty.src.framework;

import java.io.IOException;
import java.util.stream.Stream;

public interface TFWords {
    Stream<String> extractWords(String pathToFile) throws IOException;
}
