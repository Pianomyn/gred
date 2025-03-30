# Gred - A Java Pattern Matching Tool
Many everyday computing tasks like running plagiarism checks, searching with Control+F and filtering emails rely on efficient pattern matching algorithms.

Gred is a fast pattern matching tool inspired by the popular
Unix command-line utility `grep`.

It recursively traverses a directory (current directory by default)
and search all descendant files for occurrences of a given pattern.

I'm working on this project to improve my coding skills (splitting concerns across domains, testing and problem-solving) </br>
while also getting familiar with the structure of Java projects and learning
about the fascinating and well-researched field of pattern matching.

## Features
- Pattern matching with well optimized pattern matching algorithms. Currently implemented (Benchmark WIP)
  - Rabin-Karp
  - Boyer-Moore
  - KMP (WIP)
- Recursive directory traversal
- Clearly formatted output

## Motivations


The returned output will be formatted like

`filename:lineNumber:characterIndex: matchedLineText`

![usage_example](https://github.com/user-attachments/assets/55207c5a-88bb-46c9-a594-4ebb34a17b35)

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
