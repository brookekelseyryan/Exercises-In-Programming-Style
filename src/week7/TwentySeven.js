/*
 * Chapter 27, Spreadsheets.
 * Exercises in Programming Style
 *
 * I chose to do this homework in JavaScript because creating a mapping/data structure similar to example solution
 * would have been straightforward here.
 * @author Brooke Ryan, brooke.ryan@uci.edu
 */

/*
 * The columns. Each column is a data element and a formula.
 * The first two columns are the input data, so null formulas.
 */
allWords = {
    "data": [],
    "formula": null
}

stopWords = {
    "data": [],
    "formula": null
}

/*
 * Words not part of the stop_words list.
 */
nonStopWords = {
    "data": [],
    // For each word in allWords, if it is not a stop word, add it to the end of nonStopWords data
    "formula": () => {
        let nsw = []
        allWords["data"].forEach(function(word) {
            if (!stopWords["data"].includes(word) && word.length >= 2) {
                nsw.push(word)
            }
        })
        return nsw
    }
}

/*
 * Formula populates dataElement as an array of a set, the set by definition only contains the unique words of the non-Stop words.
 */
uniqueWords = {
    "data": [],
    "formula": () => {
        return [...new Set(nonStopWords["data"])]
    }
}

/*
 * For each word in the non-Stop words column, if it is also in the uniqueWords column, then increase the count column
 * (which will be the same row as the word in the uniqueWords column)
 */
counts = {
    "data": [],
    "formula": () => {
        let count = []
        nonStopWords["data"].forEach(function(word){
            if (uniqueWords["data"].includes(word)) {
                rowOfWord = uniqueWords["data"].indexOf(word)
                if (count[rowOfWord] == null) {
                    count[rowOfWord] = 0
                }
                count[rowOfWord] += 1
            }
        })
        return count
    }
}

const collect = require('collect.js');

sortedData = {
    "data": [],
    "formula": () => {
        // combine the values of the uniqueWords column with the counts column for each row
        sorted = collect(uniqueWords["data"]).zip(counts["data"]).toArray()

        return (sorted).sort((a, b) => b[1] - a[1])
    }
}

// The entire spreadsheet
allColumns = [nonStopWords, uniqueWords, counts, sortedData]


/*
 * The active procedure over the columns of data.
 * Call this every time the input data changes, or periodically.
 */
function update(pathToFile) {
    allWords["data"] = fs.readFileSync(pathToFile, "utf8", (error, data) => {
        if(error) {
            throw error;
        }}).toLowerCase().replace(/[^0-9a-z]/gi, ' ').split(" ");
    stopWords["data"] = fs.readFileSync("../stop_words.txt", "utf8", (error, data) => {
        if(error) {
            throw error;
        }}).toLowerCase().split(",")

    // Apply the formula in each column
    // Differs from professors implementation, adds the data to the column within the function instead of returning it
    allColumns.forEach(column => {
        if (column["formula"] != null) {
            column["data"] = column["formula"]()
        }
    })

    for ( [key, value] of sortedData["data"].slice(0,25)) {
        console.log(`${key} - ${value}`);
    }
return
}

// Helper functions
Array.prototype.unique = function () {
    return [...new Set(this)]
}

/*
 * The main function
 */
// Load the data into the first two columns
var fs = require("fs");


// Update the columns with formulas
console.log("Running default program with " + process.argv[2] + " as text file input:")
update(process.argv[2])

const readline = require('readline').createInterface({
    input: process.stdin,
    output: process.stdout
});

readline.question('Im hungry for MORE...please browse the books in the parent directory and enter ../title.txt as input: (please note lengthy books will take much longer than others!)\n', filePath => {
    console.log(`Updating spreadsheet with ${filePath} as text file input:`);
    update(filePath)
});


return;