package week1

import java.io.File
import java.io.FileNotFoundException
import java.util.logging.Logger
import java.util.stream.Collectors

val logger: Logger = Logger.getLogger("debug")

val TEXT = "pride-and-prejudice.txt"
val STOP_WORDS_FILE_PATH = "stop_words.txt"
val stopWords = initStopWords(STOP_WORDS_FILE_PATH)

/**
 * Parses command-line arguments and initiates program.
 */
fun main(args: Array<String>) {
    if (args.size==0) {
        println("Please provide $TEXT as a command-line argument")
        return
    } else if (args.size > 1) {
        println("Too many command-line arguments '${args[0]}' provided")
        println("Please provide $TEXT as a command-line argument")
        return
    } else if (args[0] == TEXT) {
        if (!File(args[0]).exists()) {
            throw FileNotFoundException("${args[0]} file cannot be found")
        } else {
            run(args[0])
        }
    } else {
        println("Incorrect command line argument '${args[0]}'")
        println("Please provide $TEXT as a command-line argument")
    }
}

/**
 * Essentially serves as the main function for the program, since the actual "main" function was used for command line argument parsing.
 *
 * Creates a Stream<String> from BufferedReader lines.
 * First the data is normalized by converting all text to lowercase, replacing any non-alphanumeric characters with a blank space, trimming any extraneous whitespace, and finally, split by the space delimiter.
 * Then, we analyze the words.  We remove any blank words or words less than 2 characters, and any words that were specified in the stop_words.txt file.
 * Finally, we count each of the words, and print out the 25 most frequently encountered words in descending order.
 */
fun run(file: String) {

    // Creates a regex object to filter out all non-alphanumeric characters.
    val re = Regex("[^A-Za-z0-9 ]")

    val words = File(file).bufferedReader().lines()
            // Normalizes the data
            .map(String::toLowerCase)
            .map { line -> re.replace(line, " ") }
            .map(String::trim)
            .map { line -> line.split(" ") }

            // Groups all words to single stream for convenience
            .flatMap(List<String>::stream)

            // Eliminates blank words, stop words, or words less than 2 characters
            .filter { word -> word.isNotBlank() }
            .filter { word -> isNotStopWord(word)}
            .filter{ word -> isNotShortWord(word)}

            // Collects all filtered words to a List<String>
            .collect(Collectors.toList())

    // Creates Pair<String, Int> for the 25 most frequent words
    val topWords = words.groupingBy { it }
            .eachCount()
            .toList()
            .sortedByDescending { it.second }
            .take(25)

    // Prints the results to the command line.
    topWords.forEach{ println(it)}
}

/**
 * @param path: Path to the stop_words.txt file that provides a comma-separated list of values for words to ignore from the term frequency counter.
 * @return: a MutableList<String> of the stop words.
 */
@Throws(FileNotFoundException::class)
fun initStopWords(path: String): MutableList<String>? {
    val stopWordsFile = File(path)

    if (!stopWordsFile.exists()) {
        throw FileNotFoundException("stop words file cannot be found")
    }

    return stopWordsFile.bufferedReader().lines()
            .map { line -> line.split(",") }
            .flatMap(List<String>::stream)
            .filter {  word -> word.isNotBlank() }
            .collect(Collectors.toUnmodifiableList())
}

/**
 * @param word
 * @return true if this string is not a stop word.
 */
fun isNotShortWord(word: String): Boolean {
    return word.length >= 2
}

/**
 * @param word
 * @return true if this string is not a stop word.
 */
fun isNotStopWord(word: String): Boolean {
    return !stopWords?.contains(word)!!
}


