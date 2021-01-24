package week2

import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author brookekelseyryan
 *
 * "Code Golf" - Chapter 7, Exercises in Programming Style
 * Objective is to make code as concise as possible while maintaining readability and correctness.
 */
fun main(args: Array<String>) {
    val stopWords = Files.readString(Paths.get("stop_words.txt")).split(",")

    Regex("[^A-Za-z0-9]").replace(Files.readString(Paths.get(args[0])).toLowerCase(), " ")
            .splitToSequence(" ")
            .filter{w -> (!stopWords.contains(w)) && w.length >= 2}
            .groupingBy{it}
            .eachCount()
            .entries.sortedBy { -it.value }
            .take(25)
            .forEach{println(it)}
}