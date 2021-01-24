package week5.twenty.src.framework;

import java.util.Map;
import java.util.stream.Stream;

public interface TFFreqs {
    Stream<Map.Entry<String, Long>> top25Words(Stream<String> words);
}
