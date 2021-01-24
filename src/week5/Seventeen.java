package week5;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Note: Professor said it is acceptable to use her java implementation from exercise 11 to build off of for 17.1.
 * Original implementation can be found here:
 * https://github.com/crista/exercises-in-programming-style/blob/master/11-things/tf_10.java
 */
public class Seventeen {

    /*
     * The main function
     */
    public static void main(String[] args) throws IOException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {

        Reflection.printPretty(Reflection.PURPLE, "----------------------------------------------------------------------\n");
        Reflection.printPretty(Reflection.PURPLE_BOLD_BRIGHT, "Exercise 17.1:\n");
        Reflection.printPretty(Reflection.PURPLE,"----------------------------------------------------------------------\n");

        new WordFrequencyController(args[0]).run();

        Reflection.printPretty(Reflection.PURPLE, "----------------------------------------------------------------------\n");
        Reflection.printPretty(Reflection.PURPLE_BOLD_BRIGHT, "Exercise 17.2:\n");
        Reflection.printPretty(Reflection.PURPLE,"----------------------------------------------------------------------\n");

        Reflection.promptUserForInput().forEach(className -> {
            Reflection.of(className)
                    .printClassName()
                    .printSuperClass()
                    .printInterfaces()
                    .printFields()
                    .printConstructor()
                    .printMethods();
        });
    }

}

/**
 * Helper class with methods that uses reflection to extract information such as class fields, methods,
 */
class Reflection {

    public Class<?> aClass;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String PURPLE = "\u001B[35m";
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";
    public static final String CYAN = "\033[0;36m";
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";
    public static final String WHITE = "\033[0;37m";
    public static final String WHITE_BOLD = "\033[1;37m";
    public static final String GREEN = "\033[0;32m";
    public static final String RED = "\u001B[31m";

    // Helper function that prints in pretty colors :)
    public static void printPretty(String color, String line) {
        System.out.print(color + line + ANSI_RESET);
    }

    public Reflection(Class<?> aClass) {
        this.aClass = aClass;
    }

    public static Reflection of(Class<?> className) {
        return new Reflection(className);
    }

    public Reflection printClassName() {
        printPretty(PURPLE, "\n----------------------------------------------------------------------\n");
        printPretty(PURPLE_BOLD_BRIGHT, aClass + "\n");
        printPretty(PURPLE,"----------------------------------------------------------------------\n");

        printPretty(CYAN_BOLD_BRIGHT, "class ");
        printPretty(WHITE_BOLD, aClass.getSimpleName());
        return this;
    }

    public Reflection printSuperClass() {
        if (aClass.getSuperclass() != null) {
            printPretty(CYAN_BOLD_BRIGHT, " extends ");
            printPretty(WHITE, aClass.getSuperclass().getTypeName());
        }
        return this;
    }

    /**
     * Prints fields information: its modifier (i.e. private, public, etc), its generic type, and its name
     */
    public Reflection printFields() {
        Field[] declaredFields = aClass.getDeclaredFields();
        if (declaredFields.length > 0) {
            printPretty(GREEN, "\n/**\n");
            printPretty(GREEN, " * Declared Fields\n");
            printPretty(GREEN, " */\n");
            Arrays.stream(declaredFields).forEach((Field field) -> {
                printPretty(CYAN_BOLD_BRIGHT, Modifier.toString(field.getModifiers()));
                printPretty(CYAN, " " + field.getGenericType().getTypeName());
                printPretty(WHITE, " " + field.getName() + ";\n");
            });
        }
        return this;
    }

    public Reflection printConstructor() {
        Constructor<?>[] constructors = aClass.getConstructors();
        if (constructors.length != 0) {
            printPretty(GREEN, "\n/**\n");
            printPretty(GREEN, " * Constructors\n");
            printPretty(GREEN, " */\n");
            Arrays.stream(constructors).forEach((constructor) -> {
                printPretty(CYAN_BOLD_BRIGHT, Modifier.toString(constructor.getModifiers()));
                printPretty(WHITE, " " + constructor.getName() + "(");
                printParameters(constructor.getParameters());
                printPretty(WHITE, ");\n");
            });
        }
        return this;
    }

    public Reflection printMethods() {
        Method[] methods = aClass.getDeclaredMethods();
        if (methods.length > 0) {
            printPretty(GREEN, "\n/**\n");
            printPretty(GREEN, " * Declared Methods\n");
            printPretty(GREEN, " */\n");
            Arrays.stream(methods).forEach(method -> {
                printPretty(CYAN_BOLD_BRIGHT, Modifier.toString(method.getModifiers()));
                printPretty(CYAN, " " + method.getGenericReturnType().getTypeName());
                printPretty(WHITE_BOLD, " " + method.getName() + "(");
                printParameters(method.getParameters());
                printPretty(WHITE_BOLD, ");\n");
            });
        }
        printPretty(WHITE, "\n}\n\n");
        return this;
    }

    private Reflection printParameters(Parameter[] parameters) {
        if (parameters.length > 0) {
            printPretty(WHITE, parameters[0].getType().getTypeName());
            printPretty(WHITE, " " + parameters[0].getName());
            for (int i = 1; i < parameters.length; i++) {
                printPretty(WHITE, ", " + parameters[i].getType().getSimpleName());
                printPretty(WHITE, " " + parameters[i].getName());
            }
        }
        return this;
    }

    public Reflection printInterfaces() {
        if (aClass.getGenericInterfaces().length != 0) {
            printPretty(CYAN_BOLD_BRIGHT, " implements ");
            Arrays.stream(aClass.getGenericInterfaces()).forEach(iface -> {
                printPretty(WHITE, iface.getTypeName());
            });
        }
        printPretty(WHITE, " {");
        return this;
    }

    public static List<Class<?>> promptUserForInput() throws ClassNotFoundException {
        boolean needInput = true;

        while (needInput) {
            printPretty(PURPLE, "----------------------------------------------------------------------\n");
            printPretty(PURPLE_BOLD_BRIGHT, "Please enter a class name of an application class:\n");
            printPretty(PURPLE,"----------------------------------------------------------------------\n");
            printPretty(PURPLE_BOLD_BRIGHT,"DataStorageManager");
            printPretty(PURPLE, "       Models the contents of the file.\n");
            printPretty(PURPLE_BOLD_BRIGHT,"StopWordManager");
            printPretty(PURPLE, "          Models the stop word filter.\n");
            printPretty(PURPLE_BOLD_BRIGHT,"WordFrequencyManager");
            printPretty(PURPLE, "     Keeps the word frequency data.\n");
            printPretty(PURPLE_BOLD_BRIGHT,"Reflection\n");
            printPretty(PURPLE_BOLD_BRIGHT,"Seventeen\n");
            printPretty(PURPLE_BOLD_BRIGHT,"TFExercise\n");
            printPretty(PURPLE_BOLD_BRIGHT,"WordFrequencyPair\n");
            printPretty(PURPLE_BOLD_BRIGHT,"MutableInteger\n");
            printPretty(PURPLE_BOLD_BRIGHT,"All");
            printPretty(PURPLE, "                      Prints all of the above classes.\n");

            printPretty(PURPLE_BOLD_BRIGHT, "\nClass Name: ");
            String input = new Scanner(System.in).nextLine();

            if (input.equals("All")) {
                return List.of(Seventeen.class.getClassLoader().loadClass("DataStorageManager"),
                        Seventeen.class.getClassLoader().loadClass("StopWordManager"),
                        Seventeen.class.getClassLoader().loadClass("WordFrequencyManager"),
                        Seventeen.class.getClassLoader().loadClass("Reflection"),
                        Seventeen.class.getClassLoader().loadClass("Seventeen"),
                        Seventeen.class.getClassLoader().loadClass("TFExercise"),
                        Seventeen.class.getClassLoader().loadClass("WordFrequencyPair"),
                        Seventeen.class.getClassLoader().loadClass("MutableInteger"));
            }

            try {
                return List.of(Seventeen.class.getClassLoader().loadClass(input));
            } catch (ClassNotFoundException e) {
                printPretty(RED, "Class not found for given input.  Please try again. \n");
            }

        }
        return null;
    }
}

/*
 * The classes
 */
abstract class TFExercise {
    public String getInfo() {
        return this.getClass().getName();
    }
}

class WordFrequencyController extends TFExercise {
    private DataStorageManager storageManager;
    private StopWordManager stopWordManager;
    private WordFrequencyManager wordFreqManager;

    public WordFrequencyController(String pathToFile) throws IOException {
        this.storageManager = new DataStorageManager(pathToFile);
        this.stopWordManager = new StopWordManager();
        this.wordFreqManager = new WordFrequencyManager();
    }

    /**
     * Exercise 17.1
     * In the run method of WordFrequencyController (or equivalent in your code), invoke the methods of the DataStorageManager, StopWordManager, and WordFrequencyCounter objects using reflection
     */
    public void run() throws ClassNotFoundException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // Get classes using reflection
        ClassLoader classLoader = this.getClass().getClassLoader();
        Class<?> dataStorageManager = classLoader.loadClass("DataStorageManager");
        Class<?> wordFrequencyManager = classLoader.loadClass("WordFrequencyManager");
        Class<?> stopWordManager = classLoader.loadClass("StopWordManager");

        // Obtain appropriate methods
        // We can safely type-cast because we know the return type of the method we are obtaining
        List<String> words = (List<String>) dataStorageManager.getMethod("getWords").invoke(this.storageManager);
        Method isStopWord = stopWordManager.getMethod("isStopWord", String.class);
        Method incrementCount = wordFrequencyManager.getMethod("incrementCount", String.class);

        // Call reflectively obtained methods in the for-loop
        for (String word : words) {
            if (!(boolean)isStopWord.invoke(this.stopWordManager, word)) {
                incrementCount.invoke(this.wordFreqManager, word);
            }
        }

        // Obtain sorted method from WordFrequencyManager, and get result
        // We can safely type-cast because we know the return type of the method we are obtaining
        List<WordFrequencyPair> sortedWords = (List<WordFrequencyPair>) wordFrequencyManager.getMethod("sorted").invoke(this.wordFreqManager);
        int numWordsPrinted = 0;

        for (WordFrequencyPair pair : sortedWords) {
            System.out.println(pair.getWord() + " - " + pair.getFrequency());

            numWordsPrinted++;
            if (numWordsPrinted >= 25) {
                break;
            }
        }
    }
}

/** Models the contents of the file. */
class DataStorageManager extends TFExercise {
    private List<String> words;

    public DataStorageManager(String pathToFile) throws IOException {
        this.words = new ArrayList<String>();

        Scanner f = new Scanner(new File(pathToFile), "UTF-8");
        try {
            f.useDelimiter("[\\W_]+");
            while (f.hasNext()) {
                this.words.add(f.next().toLowerCase());
            }
        } finally {
            f.close();
        }
    }

    public List<String> getWords() {
        return this.words;
    }

    public String getInfo() {
        return super.getInfo() + ": My major data structure is a " + this.words.getClass().getName();
    }
}

/** Models the stop word filter. */
class StopWordManager extends TFExercise {
    private Set<String> stopWords;

    public StopWordManager() throws IOException {
        this.stopWords = new HashSet<String>();

        Scanner f = new Scanner(new File("stop_words.txt"), "UTF-8");
        try {
            f.useDelimiter(",");
            while (f.hasNext()) {
                this.stopWords.add(f.next());
            }
        } finally {
            f.close();
        }

        // Add single-letter words
        for (char c = 'a'; c <= 'z'; c++) {
            this.stopWords.add("" + c);
        }
    }

    public boolean isStopWord(String word) {
        return this.stopWords.contains(word);
    }

    public String getInfo() {
        return super.getInfo() + ": My major data structure is a " + this.stopWords.getClass().getName();
    }
}

/** Keeps the word frequency data. */
class WordFrequencyManager extends TFExercise {
    private Map<String, MutableInteger> wordFreqs;

    public WordFrequencyManager() {
        this.wordFreqs = new HashMap<String, MutableInteger>();
    }

    public void incrementCount(String word) {
        MutableInteger count = this.wordFreqs.get(word);
        if (count == null) {
            this.wordFreqs.put(word, new MutableInteger(1));
        } else {
            count.setValue(count.getValue() + 1);
        }
    }

    public List<WordFrequencyPair> sorted() {
        List<WordFrequencyPair> pairs = new ArrayList<WordFrequencyPair>();
        for (Map.Entry<String, MutableInteger> entry : wordFreqs.entrySet()) {
            pairs.add(new WordFrequencyPair(entry.getKey(), entry.getValue().getValue()));
        }
        Collections.sort(pairs);
        Collections.reverse(pairs);
        return pairs;
    }

    public String getInfo() {
        return super.getInfo() + ": My major data structure is a " + this.wordFreqs.getClass().getName();
    }
}

class MutableInteger {
    private int value;

    public MutableInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

class WordFrequencyPair implements Comparable<WordFrequencyPair> {
    private String word;
    private int frequency;

    public WordFrequencyPair(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public int getFrequency() {
        return frequency;
    }

    public int compareTo(WordFrequencyPair other) {
        return this.frequency - other.frequency;
    }
}