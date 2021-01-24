package week3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Recursion
 **/
public class Eight {

    public static HashMap<String, Integer> wordFrequencies;
    // note: lowered from professor's 5000 to 500 because got a stack overflow error on repl.it
    public static final int RECURSION_LIMIT = 500;

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("No command-line arguments found! Consult the README and ensure you are passing in the correct arguments.");
            return;
        }
        // init words
        List<String> words = Arrays.asList(Files.readString(Paths.get(args[0])).toLowerCase().replaceAll("[^A-Za-z0-9 ]", " ").split(" "));
        // init stop words
        List<String> stopWords = Arrays.asList(Files.readString(Paths.get("stop_words.txt")).split(","));
        // init global variable wordFrequencies
        wordFrequencies = new HashMap<>();

        // in order to avoid a stack overflow error, we only process RECURSION_LIMIT number of words at a time
        for(int i=0; i< words.size(); i+=RECURSION_LIMIT) {
            if (i+RECURSION_LIMIT < words.size()) {
                wordFrequencies.putAll(count(words.subList(i, i+RECURSION_LIMIT), stopWords, wordFrequencies));
            } else {
                wordFrequencies.putAll(count(words.subList(i, words.size()), stopWords, wordFrequencies));
            }
        }

        // calling the recursive sort function on our wordFrequencies map
        ArrayList<Map.Entry<String, Integer>> sorted = sort(wordFrequencies, new ArrayList<>());
        // recursively prints from a sublist of sorted 25 most frequent words
        wfPrint(sorted.subList(0, 25));
    }

    /**
     * Recursively counts the words.  We take the first word, and if it is not a stop word or a short word, we add it to our word frequencies counter and update appropriately.  Then we eliminate that first word and recursively call itself until the wordList is empty, which is our base case.
     **/
    public static HashMap<String, Integer> count(List<String> wordList, List<String> stopWords, HashMap<String, Integer> wordFrequencies) {
        // base case
        if (wordList.isEmpty()) {
            return wordFrequencies;
        }
        // inductive case
        String word = wordList.get(0);
        if ((!stopWords.contains(word)) && word.length()>=2) {
            wordFrequencies.putIfAbsent(word, 0);
            wordFrequencies.put(word, wordFrequencies.get(word)+1);
        }
        return count(wordList.subList(1, wordList.size()), stopWords, wordFrequencies);
    }

    /**
     * Recursively sorts the entries.  Finds the maximum element of the given map, removes said element, then recursively calls itself until the map has no more elements to sort.  Sorted elements are stored in an ArrayList.
     **/
    public static ArrayList<Map.Entry<String, Integer>> sort(HashMap<String, Integer> unsorted, ArrayList<Map.Entry<String, Integer>> sorted) {
        // base case
        if (unsorted.isEmpty()) {
            return sorted;
        }

        // inductive case
        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : unsorted.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        unsorted.remove(maxEntry.getKey(), maxEntry.getValue());
        sorted.add(maxEntry);

        return sort(unsorted, sorted);
    }


    /**
     * Recursively prints the list of word frequencies.  We print out the first element, then recursively call itself on the second element onwards, until the list is empty, which is the base case.
     **/
    public static void wfPrint(List<Map.Entry<String, Integer>> wf) {
        // inductive case
        if (wf.size() > 0) {
            System.out.println(wf.get(0).getKey() + " - " + wf.get(0).getValue());
            wfPrint(wf.subList(1, wf.size()));
        }
    }

}
