package week5.twenty.src.framework;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

public class TFApplication implements TFFreqs, TFWords{
    @Override
    public Stream<Map.Entry<String, Long>> top25Words(Stream<String> words) {
        return null;
    }

    @Override
    public Stream<String> extractWords(String pathToFile) throws IOException {
        return null;
    }
}