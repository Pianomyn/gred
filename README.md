# Gred - A Pattern Matching Tool
Many everyday computing tasks like running plagiarism checks, searching with Control+F and filtering emails rely on efficient pattern matching algorithms.

Gred is a fast pattern matching tool inspired by the popular
Unix command-line utility `grep`.

It recursively traverses a directory (current directory by default)
and search all descendant files for occurrences of a given pattern.

I'm working on this project to improve my coding skills (splitting concerns across domains, testing and problem-solving) </br>
while also getting familiar with the structure of Java projects and learning
about the fascinating and well-researched field of pattern matching.

## Features
- Pattern matching with well optimized pattern matching algorithms. Currently implemented
  - <u><b>Rabin-Karp</b></u>
    - Uses a rolling hash (Usually moving sum) to avoid pointless checks. Hard to pick a good rolling hash that balances correctness and speed.
  - <u><b>Boyer-Moore</b></u> ([Primer](https://www.youtube.com/watch?v=4Xyhb72LCX4&t=200s))
      - Precomputation on the pattern to skip redundant matches. 2 well known precomputations are described below. Take the best of the 2 suggestions.
      - <b>Bad Char Table</b> (Skip until mismatch becomes match):
        - Computation
          - Classic approach is to use map to track last index occurrence. Less space, worse time complexity. Alternatively, create a matrix of size `pattern_length` * `|alphabet|`.
          - For each i in range \[0, pattern_length\], compute the distance that each character in the alphabet was last seen based on i-1.
        - Usage
          - Line the pattern up with the start of the text and in each iteration, compare from right to left.
          - When a mismatch occurs, move the pattern forwards so the closest-on-the-left occurrence of the mismatching character in the pattern lines up with the mismatching character in the text.
      - <b>Good Suffix Table</b> ([Primer](https://web.archive.org/web/20200427070016/https://www.inf.hs-flensburg.de/lang/algorithmen/pattern/bmen.htm) Don't turn any existing matches into a mismatch)
        - Computation
        - Usage
  - <u><b>KMP</b></u> (WIP)
    - If a mismatch occurs, 
    - <b>Last Prefix Suffix</b>: Precomputation on the pattern to skip redundant matches.
      - Computation
        - Create an array of size `pattern_length` called `lps`.
        - Use 2 pointers, `left` and `right` and start both at 0.
        - If `pattern[right]` == `pattern[left]`, Set `lps`
      - Usage
- Recursive directory traversal
- Clearly formatted output


## Usage
### Using Java
Install the Java 17 JRE
``` bash
./gradlew clean build shadowJar
java -jar build/libs/gred-1.0-SNAPSHOT-all.jar PATTERN [DIRECTORY]
```
You can specify the pattern matching algorithm you want to use with flags
``` bash
-nv Naive
-rk Rabin-Karp
-bm Boyer-Moore
-kmp Knuth-Morris-Pratt
```

### Using Docker (WIP)
Install the docker command line tool.
```
docker build -t gred-docker .
sh docker_run.sh
```

The returned output will be formatted like

`filename:lineNumber:characterIndex: matchedLineText`

![usage_example](https://github.com/user-attachments/assets/55207c5a-88bb-46c9-a594-4ebb34a17b35)

## Development
- Github Actions is used for CI. Create a PR to
    - Run all tests
    - Run lint checks using checkstyle
- Gradle tasks exist for linting and formatting locally
    - `./gradlew lint` Uses checkstyle and spotless
    - `./gradlew format` Uses spotless

## Future Work
- Measure performance against grep
- Multi-threading
- Handle utf chars in boyer moore table
- Regex support

## Note to self
sudo docker run --rm -it --entrypoint=/bin/sh gred
