package week6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.util.Map.Entry.comparingByValue;

/**
 * Chapter 30, Dataspaces
 * Exercises in Programming Style
 *
 * Existence of one or more units that execute concurrently.
 * Existence of one or more data spaces where concurrent units store and retrieve data.
 * No direct data exchanges between the concurrent units, other than via data spaces.
 *
 * @author Brooke Ryan, brooke.ryan@uci.edu
 */
public class Thirty {

    // Pride and Prejudice has 122,189, plus extra for Project Gutenberg text.
    public static final int CAPACITY = 150000;
    public static final String STOP_WORDS_PATH = "../stop_words.txt";
    public static final int NUM_WORKERS = 5;

    private static BlockingQueue<String> wordSpace;
    private static BlockingQueue<HashMap<String, Integer>> freqSpace;
    private static Set<String> stopWords = new HashSet<>();

    // By default, logging off.  To set logging, pass in "logging" as a command line argument.
    static boolean logging = false;

    public static void log(Object message) {
        if (logging && message != null) {
            System.out.println(message.toString());
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 1) {
            System.err.println("Incorrect number of arguments, please consult the README!");
            return;
        } else if (args.length ==2 && args[1].equals("logging")) {      // for my own debugging, not part of assignment
            logging = true;
            log("logging set to true");
        }

        new Thirty().run(args[0]);
    }

    public void run(String arg) throws IOException, InterruptedException {
        // Two data spaces
        wordSpace = new ArrayBlockingQueue<>(CAPACITY);
        freqSpace = new ArrayBlockingQueue<>(CAPACITY);

        Files.lines(Path.of(STOP_WORDS_PATH)).forEach(line -> Collections.addAll(stopWords, line.split(",")));
        stopWords.addAll(Arrays.asList("a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z".split(",")));

        // Let's have this thread populate the word space
        Files.lines(Paths.get(arg))
                .map(String::toLowerCase)
                .map(line -> line.replaceAll("[^A-Za-z0-9 ]", " "))
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .filter(word -> !word.isBlank())
                .forEach(wordSpace::add);

        // Let's create the workers and launch them at their jobs
        List<Thread> workers = new ArrayList<>(NUM_WORKERS);
        for (int i=0; i<NUM_WORKERS; i++) {
            workers.add(new Thread(processWords));      // processWords is a Runnable, equivalent of Python's thread target
        }

        for (Thread worker : workers) {
            worker.start();
        }

        // Let's wait for the workers to finish
        for (Thread worker : workers) {
            worker.join();
        }

        // Let's merge the partial frequency results by consuming frequency data from the frequency space
        Map<String, Integer> wordFreqs = new HashMap<>();
        while (!freqSpace.isEmpty()) {
            Map<String, Integer> freqs = freqSpace.poll(1, TimeUnit.SECONDS);
            freqs.forEach((key, value) -> {
                Integer count;
                if (wordFreqs.containsKey(key)) {
                    count = freqs.get(key) + wordFreqs.get(key);
                } else {
                    count = freqs.get(key);
                }
                wordFreqs.put(key, count);
            });
        }

        // Print top 25 words
        wordFreqs.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .limit(25)
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
    }


    /**
     * Worker function that consumes words from the word space and sends partial results to the frequency space
     */
    public Runnable processWords = () -> {
        int count = 0;
        HashMap<String, Integer> wordFrequencies = new HashMap<>();
        while (!wordSpace.isEmpty()) {
            count++;
            String word = "";
            // I think we use poll because professor passes in a timeout=1 in the Python version
            try {
                word = wordSpace.poll(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                break;
            }
            if (!stopWords.contains(word)) {
                if (wordFrequencies.containsKey(word)) {
                    wordFrequencies.put(word, wordFrequencies.get(word) + 1);
                } else {
                    wordFrequencies.put(word, 1);
                }
            }
        }
        try {
            freqSpace.put(wordFrequencies);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };


}
