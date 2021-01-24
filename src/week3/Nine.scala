import java.nio.file.{Files, Paths}

import scala.collection.immutable.ListMap

/**
* Kick-Forward
**/
object Nine {

  def main(args: Array[String]) = {
    readFile(args(0), normalizeData)
  }

  def readFile(path: String, fun: (String, (Array[String], (Array[String], Any => Unit) => Unit) => Unit) => Unit): Unit = {
    fun.apply(Files.readString(Paths.get(path)), removeStopWords)
  }

  def normalizeData(fileContents: String, fun: (Array[String], (Array[String], Any => Unit) => Unit) => Unit): Unit = {
    fun.apply(fileContents.toLowerCase().replaceAll("[^A-Za-z0-9 ]", " ").split(" "), countSortPrint)
  }

  def removeStopWords(words: Array[String], fun: (Array[String], Any => Unit) => Unit): Unit = {
    val stopWords = Files.readString(Paths.get("stop_words.txt")).split(",")
    fun.apply(words.filter(word => !stopWords.contains(word)).filter(word => word.length>=2), noop)
  }

  def countSortPrint(words: Array[String], fun: Any => Unit): Unit = {
    ListMap(words.groupBy(identity).view.mapValues(_.size).toSeq.sortWith(_._2 > _._2):_*).take(25).toMap.foreach(entry => println(entry._1 + " - " + entry._2))
    fun.apply()
  }


  val noop: Any => Unit = (a: Any) => ()

}
