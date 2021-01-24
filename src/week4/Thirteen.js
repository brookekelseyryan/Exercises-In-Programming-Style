// Closed Maps, Chapter 13
// I chose to do this assignment in JavaScript because its object model seemed to support the idea of closed maps as described in the book.

function extractWords(obj, pathToFile) {
    var fs = require("fs");
    obj["data"] = fs.readFileSync(pathToFile, "utf8", (error, data) => {
        if(error) {
            throw error;
        }}).toLowerCase().replace(/[^0-9a-z]/gi, ' ').split(" ");
}

function loadStopWords(obj) {
    var fs = require("fs");
    obj["stopWords"] = fs.readFileSync("stop_words.txt", "utf8", (error, data) => {
        if(error) {
            throw error;
        }}).toLowerCase().split(",")
}

function incrementCount(obj, w) {
    if (obj["freqs"][w] != null) {
        obj["freqs"][w] += 1
    } else {
        obj["freqs"][w] = 1
    }
}


dataStorageObject = {
    "data": [],
    "init": (pathToFile) => {extractWords(dataStorageObject, pathToFile)},
    "words": () => { return dataStorageObject["data"]}
}

stopWordsObject = {
    "stopWords": [],
    "init": () => {loadStopWords(stopWordsObject)},
    "isStopWord": (word) => {return (stopWordsObject["stopWords"].includes(word) || word.length <2)}
}

wordFreqsObj = {
    "freqs": {},
    "incrementCount": (w) => {incrementCount(wordFreqsObj, w)},
    "sorted": () => {
        return Object.entries(wordFreqsObj["freqs"]).sort((a, b) => b[1] - a[1])
    }
}

dataStorageObject["init"](process.argv[2])
stopWordsObject["init"]()

dataStorageObject["words"]().forEach(function(word) {
    if (!stopWordsObject["isStopWord"](word)) {
        wordFreqsObj["incrementCount"](word)
    }
})

console.log("--------------")
console.log("Exercise 13.1:")
console.log("--------------")

const sorted = wordFreqsObj["sorted"]();
for ( [key, value] of sorted.slice(0,25)) {
    console.log(`${key} - ${value}`);
}

console.log("--------------")
console.log("Exercise 13.2:")
console.log("--------------")

// Adds method to wordFreqsObj at the bottom of the file, as instructed in 13.2
wordFreqsObj["top25"] = () => {
    const sorted = Object.entries(wordFreqsObj["freqs"]).sort((a, b) => b[1] - a[1])
    for ( [key, value] of sorted.slice(0,25)) {
        console.log(`${key} - ${value}`);
    }
}
// Calls method
wordFreqsObj["top25"]()