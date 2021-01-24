# README

Table of Contents
=================

   * [README](#readme)
      * [About](#about)
      * [Part X: Neural Networks](#part-x-neural-networks)
      * [Dependencies:](#dependencies)
         * [Python](#python)
         * [Libraries](#libraries)
         * [Terminal](#terminal)
      * [To Run from the Command Line:](#to-run-from-the-command-line)
         * [35.py](#35py)
         * [36.py](#36py)
         * [40.py](#40py)
         * [Notes](#notes)
      * [Troubleshooting](#troubleshooting)
   * [Week 8](#week-8)
   * [Week 7](#week-7)
   * [Week 6](#week-6)
   * [Week 5](#week-5)
      * [Running and Building the Projects](#running-and-building-the-projects)
      * [Twenty](#twenty)
         * [Compilation](#compilation)
   * [Week 4](#week-4)
      * [Running and Building the Projects](#running-and-building-the-projects-1)
   * [Week 3](#week-3)
      * [Building the Projects](#building-the-projects)
      * [Running the Projects](#running-the-projects)
   * [Week 2](#week-2)
      * [Repl.it](#replit)
      * [Building the Projects](#building-the-projects-1)
      * [Running the Projects](#running-the-projects-1)
      * [Expected Output](#expected-output)
   * [README](#readme-1)
      * [Running the application](#running-the-application)
      * [Troubleshooting](#troubleshooting-1)
         * [Potential Errors and Solutions](#potential-errors-and-solutions)
      * [Contact](#contact)

## About
Feel free to use this repository as a reference (e.g., for correctness verification or as an inspiration), but please do not submit copies the code provided here as homework solutions for a later edition of UCI CS 253 -- you won't learn anything from doing so, and you will most likely get caught.


## Part X: Neural Networks

from *Exercises in Programming Style*:

- **35.3**: Implement a NN that transforms characters into their LEET counterparts.  Use any encoding and any LEET alphabet.
- **36.4**: Implement a NN that learns to transform characters into their LEET counterparts.  Use any encoding and any LEET alphabet. 
- **40.2**: Change the example program so that it also eliminates 2-letter words.



## Dependencies:

Ensure that your system has the following dependencies before running the programs.  For additional help or installation guides, follow the link to its official page.

### Python

[64-bit Python 3.8.6](https://www.python.org/downloads/release/python-386/)

### Libraries

[Keras 2.4.3](https://pypi.org/project/Keras/)

[Tensorflow 2.3.1](https://pypi.org/project/tensorflow/)

[Numpy 1.18.5](https://pypi.org/project/numpy/1.18.5/)

### Terminal 

[Git Bash terminal](https://git-scm.com/downloads) 



## To Run from the Command Line:

Starting in the folder from which you downloaded the .zip file, unzip its contents*

```
unzip BrookeRyan_Week9.zip -d BrookeRyan_Week9
```

Navigate to the Week9 directory:

```
cd BrookeRyan_Week9
```

### 35.py

```
python 35.py pride-and-prejudice.txt
```

### 36.py

```
python 36.py pride-and-prejudice.txt
```

### 40.py

```
python 40.py pride-and-prejudice.txt
```

### Notes

*Some systems may not come pre-installed with the unzip command.  Another option is to navigate to the folder in the file explorer, and right click on the icon to extract contents.*  

*On some systems, `python` should be replaced with `python3`.*

*The output of the files is quite long.  If you wish to terminate the program prematurely, use **Ctrl+C**.* 



## Troubleshooting

Ensure that you are running the program with a Python version between 3.5 and 3.8.  

Also ensure that your PATH variable points to the correct version of Python.  

Guide to installing libraries on Mac OS: https://piazza.com/class/kfl7e412uvcdn?cid=81_f1 

# Week 8

```
cd Week8
```
```
python Three.py ../pride-and-prejudice.txt
```
Please note: takes about 25 seconds to run.

# Week 7
```
cd Week7
```
```
npm install collect.js --save
```
```
node TwentySeven.js ../pride-and-prejudice.txt
```
The program will then prompt for another book.  Make sure you enter ../ in front of the title. For example:
```
../moby-dick.txt
```
Terminate the program by pressing Control C.


Compile TwentyEight.java.
```
javac TwentyEight.java
```
Run TwentyEight.
```
java TwentyEight ../pride-and-prejudice.txt
```

# Week 6 
Navigate to the Week6 directory. 
```
cd Week6
```
Compile TwentyNine.java.
```
javac TwentyNine.java
```
Run TwentyNine.
```
java TwentyNine ../pride-and-prejudice.txt
```

Compile Thirty.java.
```
javac Thirty.java
```
Run Thirty.
```
java Thirty ../pride-and-prejudice.txt
```

Compile ThirtyTwo.java.
```
javac ThirtyTwo.java
```
Run ThirtyTwo.
```
java ThirtyTwo ../pride-and-prejudice.txt
```

# Week 5 

## Running and Building the Projects 
Navigate to the Week5 directory.
```
cd Week5
```
Compile Seventeen.java.
```
javac Seventeen.java
```
Run Seventeen
```
java Seventeen ../pride-and-prejudice.txt
```
Please note:  for exercise 17.2, I designed the output to look like it is highlighted for syntax like in an IDE. You can look through my Reflection class to ensure it is in fact printing out all the appropriate information via Java reflection libraries! 

## Twenty 

Navigate to the folder within Week5.
```
cd Twenty/plugins
```
Run the program
```
java -cp framework.jar Framework pride-and-prejudice.txt
```

### Compilation
This is only included for completeness and error checking if you wish.  All the jars and such are already compiled for you.

```
cd Twenty
cd ./src/framework
javac *.java
jar cfm framework.jar manifest.mf *.class
mv framework.jar ../../plugins
cd ../app
javac -cp ../framework/ *.java
jar cf App1.jar App1.class
jar cf App2.jar App2.class
mv *.jar../../plugins
cd ../../plugins
java -cp framework.jar Framework pride-and-prejudice.txt
```

# Week 4

## Running and Building the Projects
Navigate to the Week4 directory.  
```
cd Week4
```

Run Twelve.rb
```
ruby Twelve.rb pride-and-prejudice.txt
```

Run Thirteen.js
```
node Thirteen.js pride-and-prejudice.txt
```

Build Sixteen.kt
```
kotlinc Sixteen.kt -include-runtime -d Sixteen.jar
```

Run Sixteen.kt
```
java -jar Sixteen.jar pride-and-prejudice.txt 
```



# Week 3 

## Building the Projects
Navigate to the Week3 directory. 
```
> cd Week3
```
Build Eight.kt.
```
> javac Eight.java
```

Build Nine.scala.
```
> scalac Nine.scala
```

Build Ten.java.
```
> javac Ten.java
```


## Running the Projects
Run Eight: 
```
java Eight pride-and-prejudice.txt
```
Run Nine: 
```
scala Nine pride-and-prejudice.txt
```
Run Ten: 
```
java Ten pride-and-prejudice.txt
```


# Week 2 

## Repl.it

https://repl.it/join/sxackdqp-brookekryan



## Building the Projects

Navigate to the Week2 directory. 

```
> cd Week2
```

Build Five.java.

```
> javac Five.java 
```

Build Six.java. 

```
> javac Six.java
```

Build Seven.kt.

```
> kotlinc Seven.kt -include-runtime -d Seven.jar
```
Please note, the kotlin build takes a bit longer, approx. 30 seconds.


## Running the Projects 

Run Five: 

```
> java Five pride-and-prejudice.txt
```

Run Six:

```
> java Six pride-and-prejudice.txt
```

Run Seven: 

```
> java -jar Seven.jar pride-and-prejudice.txt 
```



## Expected Output 

```
> java Five pride-and-prejudice.txt
mr=786
elizabeth=635
very=488
darcy=418
such=395
mrs=343
much=329
more=327
bennet=323
bingley=306
jane=295
miss=283
one=275
know=239
before=229
herself=227
though=226
well=224
never=220
sister=218
soon=216
think=211
now=209
time=203
good=201
> java Six pride-and-prejudice.txt
mr=786
elizabeth=635
very=488
darcy=418
such=395
mrs=343
much=329
more=327
bennet=323
bingley=306
jane=295
miss=283
one=275
know=239
before=229
herself=227
though=226
well=224
never=220
sister=218
soon=216
think=211
now=209
time=203
good=201
> java -jar Seven.jar pride-and-prejudice.txt
mr=786
elizabeth=635
very=488
darcy=418
such=395
mrs=343
much=329
more=327
bennet=323
bingley=306
jane=295
miss=283
one=275
know=239
before=229
herself=227
though=226
well=224
never=220
sister=218
soon=216
think=211
now=209
time=203
good=201
```

Ensure the output matches:  https://github.com/crista/exercises-in-programming-style/blob/master/test/pride-and-prejudice.txt


# README 

## Running the application

*Instructions taken from official Kotlin documentation and adapted for this program: https://kotlinlang.org/docs/tutorials/command-line.html*

1. **Compile the application using the Kotlin compiler:** 

```
$ kotlinc ./src/main/kotlin/org/example/Hello.kt -include-runtime -d hello.jar
```

The `-d` option indicates the output path for generated class files, which may be either a directory or a *.jar* file. The `-include-runtime` option makes the resulting *.jar* file self-contained and runnable by including the Kotlin runtime library in it. 

Note: This might take up to 30 seconds, Repl is pretty slow.

2. **Run the application.**

```
$ java -jar hello.jar pride-and-prejudice.txt
(mr, 786)
(elizabeth, 635)
(very, 488)
(darcy, 418)
(such, 395)
(mrs, 343)
(much, 329)
(more, 327)
(bennet, 323)
(bingley, 306)
(jane, 295)
(miss, 283)
(one, 275)
(know, 239)
(before, 229)
(herself, 227)
(though, 226)
(well, 224)
(never, 220)
(sister, 218)
(soon, 216)
(think, 211)
(now, 209)
(time, 203)
(good, 201)
```

## Troubleshooting

**Prior to compilation, ensure you are in the root directory of the project.**

Run the command 

```
$ pwd
```

and ensure that the output matches:

```
/home/runner/Week1
```



### Potential Errors and Solutions

**ERROR MESSAGE:**  

``` 
Please provide pride-and-prejudice.txt as a command-line argument
```

**Solution**: Ensure that pride-and-prejudice.txt is provided as a command line argument.  Pass exactly this command to the terminal:

```
java -jar hello.jar pride-and-prejudice.txt
```



**EXCEPTION:**

```
Exception in thread "main" java.io.FileNotFoundException: pride-and-prejudice.txt file cannot be found
```

**Solution**:  This should not happen as-is unless the user deleted the pride-and-prejudice.txt file from the repl.  Download the file again from the original repository: https://github.com/brookekelseyryan/pride-prejudice-and-programming/blob/main/pride-and-prejudice.txt.  Then put the file into the parent directory.   



## Contact

**Author**: Brooke Ryan 

**Email**: brooke.ryan@uci.edu



# Repl.it

Per course guidelines, homework was submitted to Repl.it.  There, the programs are guaranteed to run according to instructions outlined here.

[Week 1](https://repl.it/@brookekryan/Week1-2#README.md)

[Weeks 2-8](https://repl.it/@brookekryan/CS-253-Brooke-Ryan#README.md)
