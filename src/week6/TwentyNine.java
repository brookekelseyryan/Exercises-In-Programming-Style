package week6;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Stream;

import static java.util.Map.Entry.comparingByValue;

/**
 * Actors -- Ch 29, Exercises in Programming Style
 *
 * The larger problem is decomposed into things that make sense for the problem domain.
 * Each thing has a queue meant for other things to place messages in it.
 * Each thing is a capsule of data that exposes only its ability to receive messages via the queue.
 * Each thing has its own thread of execution independent of others.
 *
 * @author Brooke Ryan
 */
public class TwentyNine {

    // Tasks
    static class Task {
        public static final String DIE = "die";
        public static final String INIT = "init";
        public static final String FILTER = "filter";
        public static final String TOP25 = "top25";
        public static final String WORD = "word";
        public static final String SEND_WORD_FREQS = "send_word_freqs";
        public static final String RUN = "run";
    }

    // Pride and Prejudice has 122,189, plus extra for Project Gutenberg text.
    public static final Integer CAPACITY = 150000;

    public static final String STOP_WORDS_PATH = "../stop_words.txt";

    // By default, logging off.  To set logging, pass in "logging" as a command line argument after ../pride-and-prejudice.txt
    static boolean logging = false;

    public static void log(String message) {
        if (logging) {
            System.out.println(message);
        }
    }

    /**
     * Defines a class that implements the generic behavior of active objects
     * Active objects inherit from Thread, a class that supports concurrent threads of execution
     * Any active application objects inherit the behavior from ActiveWFObject.
     */
    abstract class ActiveWFObject extends Thread {
        ArrayBlockingQueue<Message> queue = new ArrayBlockingQueue<Message>(CAPACITY);
        boolean terminate = false;

        public ActiveWFObject(String className) {
            this.setName("Thread-" + className);
            this.start();
            log(this.getName());
        }

        public void enqueue(Message message) {
            queue.add(message);
        }

    }

    public void send(ActiveWFObject receiver, Message message) {
        receiver.enqueue(message);
    }

    /**
     * Models the contents of the file
     */
    class DataStorageManager extends ActiveWFObject {

        List<String> data;
        StopWordManager stopWordManager;

        public DataStorageManager() {
            // Calls the super constructor with its class name for Thread-naming.
            super(DataStorageManager.class.getSimpleName());
        }

        public void dispatch(Message message) {
            String task = message.getTask();
            if (task.equals(Task.INIT)) {
                this.init(message);
            } else if (task.equals(Task.SEND_WORD_FREQS)) {
                this.processWords(message);
            } else {
                send(stopWordManager, message);
            }
        }

        public void init(Message message)  {
            // We can safely typecast because we only ever expect a StopWordsManager here
            stopWordManager = (StopWordManager) message.getReceiver();

            String pathToFile = message.getArgument();
            File file = new File((String) pathToFile);

            // Have to surround with try/catch instead of throwing exception to preserve contract
            try {
                data = Arrays.asList(Files.readString(Paths.get(pathToFile)).toLowerCase()
                        .replaceAll("[^A-Za-z0-9 ]", " ")
                        .split(" "));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void processWords(Message message) {
            ActiveWFObject recipient = message.getReceiver();
            data.forEach(word ->
                    send(stopWordManager, new Message().setTask(Task.FILTER).setArgument(word))
            );
            send(stopWordManager, new Message().setTask(Task.TOP25).setReceiver(recipient));
        }

        // Run method is spawned concurrently when the thread's start method is called in the constructor
        // Runs an infinite loop that takes one message from the queue, possibly blocking if the queue is empty,
        // and that dispatches that message.
        // One special message die breaks the loop and makes the thread stop.
        @Override
        public void run() {
            while (!this.terminate) {
                try {
                    Message message = this.queue.take();
                    this.dispatch(message);
                    if (message.task.equals(Task.DIE)) {
                        this.terminate = true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     * Models the stop word filter
     */
    class StopWordManager extends ActiveWFObject {
        List<String> stopWords;
        WordFrequencyManager wordFrequencyManager;

        public StopWordManager() {
            super(StopWordManager.class.getSimpleName());
        }

        public void init(Message message) {
            // We can safely typecast here because we know the init message will always be of this type
            wordFrequencyManager = (WordFrequencyManager) message.getReceiver();

            // Have to surround with try/catch instead of throwing exception to preserve contract
            try {
                stopWords = Arrays.asList(Files.readString(Paths.get(STOP_WORDS_PATH)).split(","));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void dispatch(Message message) {
            if (message.getTask().equals(Task.INIT)) {
                this.init(message);
            } else if (message.getTask().equals(Task.FILTER)) {
                this.filter(message);
            } else {
                send(this.wordFrequencyManager, message);
            }
        }

        public void filter(Message message) {
            String word = message.getArgument();
            if (!stopWords.contains(word) && word.length() >= 2) {
                send(this.wordFrequencyManager, new Message().setTask(Task.WORD).setArgument(word));
            }
        }

        @Override
        public void run() {
            while (!this.terminate) {
                try {
                    Message message = this.queue.take();
                    this.dispatch(message);
                    if (message.task.equals(Task.DIE)) {
                        this.terminate = true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Keeps the word frequency data
     */
    class WordFrequencyManager extends ActiveWFObject {

        Map<String, Integer> wordFreqs = new HashMap<>();

        public WordFrequencyManager() {
            super(WordFrequencyManager.class.getSimpleName());
        }

        public void dispatch(Message message) {
            String task = message.getTask();
            if (task.equals(Task.WORD)) {
                this.incrementCount(message);
            } else if (task.equals(Task.TOP25)) {
                this.top25(message);
            }
        }

        public void incrementCount(Message message) {
            String word = message.getArgument();
            if (wordFreqs.containsKey(word)) {
                wordFreqs.put(word, wordFreqs.get(word) + 1);
            } else {
                wordFreqs.put(word, 1);
            }
        }

        public void top25(Message message) {
            ActiveWFObject recipient = message.getReceiver();
            Stream<Map.Entry<String, Integer>> freqsSorted = wordFreqs.entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(comparingByValue()))
                    .limit(25);
            send(recipient, new Message().setTask(Task.TOP25).setMap(freqsSorted));
        }

        @Override
        public void run() {
            while (!this.terminate) {
                try {
                    Message message = this.queue.take();
                    this.dispatch(message);
                    if (message.task.equals(Task.DIE)) {
                        this.terminate = true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class WordFrequencyController extends ActiveWFObject{

        DataStorageManager dataStorageManager;

        public WordFrequencyController() {
            super(WordFrequencyController.class.getSimpleName());
        }

        public void dispatch(Message message) {
            String task = message.getTask();
            if (task.equals(Task.RUN)) {
                this.run(message);
            } else if (task.equals(Task.TOP25)) {
                this.display(message);
            } else {
                System.err.println("Message not understood " + task);
            }
        }

        public void run(Message message) {
            this.dataStorageManager = (DataStorageManager) message.getReceiver();
            send(dataStorageManager, Message.of().setTask(Task.SEND_WORD_FREQS).setReceiver(this));
        }

        public void display(Message message) {
            Stream<Map.Entry<String, Integer>> wordFreqs = message.getMap();
            wordFreqs.forEach(entry ->
                    System.out.println(entry.getKey() + " - " + entry.getValue())
            );
            send(this.dataStorageManager, Message.of().setTask(Task.DIE));
            this.terminate = true;
        }

        @Override
        public void run() {
            while (!this.terminate) {
                try {
                    Message message = this.queue.take();
                    this.dispatch(message);
                    if (message.task.equals(Task.DIE)) {
                        this.terminate = true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * The main function
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.err.println("Please consult the README and ensure you are passing pride-and-prejudice as a command line argument!");
            return;

        } else if (args.length ==2 && args[1].equals("logging")) {      // for my own debugging, not part of assignment
            logging = true;
        }
        new TwentyNine().run(args[0]);
    }

    public void run(String arg) throws InterruptedException {
        WordFrequencyManager wordFrequencyManager = new WordFrequencyManager();

        StopWordManager stopWordManager = new StopWordManager();
        send(stopWordManager, Message.of("init", wordFrequencyManager));

        DataStorageManager dataStorageManager = new DataStorageManager();
        send(dataStorageManager, Message.of("init", stopWordManager, arg));

        WordFrequencyController wordFrequencyController = new WordFrequencyController();
        send(wordFrequencyController, Message.of("run", dataStorageManager));

        // Wait for the active objects to finish
        List<ActiveWFObject> threads = List.of(wordFrequencyManager, stopWordManager, dataStorageManager, wordFrequencyController);
        for (ActiveWFObject thread : threads) {
            thread.join();
        }
    }

    /**
     * Wrapper class, Java equivalent of professors' list in Python.
     * Used a pattern similar to the factory/builder pattern for conciseness in use.
     */
    static class Message {
        String task;
        ActiveWFObject receiver;
        String argument;
        Stream<Map.Entry<String, Integer>> map;

        private Message(String task, ActiveWFObject receiver, String argument) {
            this.task = task;
            this.receiver = receiver;
            this.argument = argument;
        }

        public Message() {}

        public static Message of(String task, ActiveWFObject receiver, String argument) {
            return new Message(task, receiver, argument);
        }

        public static Message of(String task, ActiveWFObject receiver) {
            return of(task, receiver, null);
        }

        public static Message of () {
            return new Message();
        }

        public Stream<Map.Entry<String, Integer>> getMap() {
            return map;
        }

        public Message setMap(Stream<Map.Entry<String, Integer>> map) {
            this.map = map;
            return this;
        }

        public String getTask() {
            return task;
        }

        public Message setTask(String task) {
            this.task = task;
            return this;
        }

        public ActiveWFObject getReceiver() {
            return receiver;
        }

        public Message setReceiver(ActiveWFObject receiver) {
            this.receiver = receiver;
            return this;
        }

        public String getArgument() {
            return argument;
        }

        public Message setArgument(String argument) {
            this.argument = argument;
            return this;
        }
    }
}
