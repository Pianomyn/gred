# Gred - A Pattern Matching Tool
Many everyday computing tasks like running plagiarism checks and searching with Control+F rely on pattern matching algorithms.

Gred is a Java project inspired by `grep`, a famous pattern matching tool that recursively traverses directories
and searches for occurrences of a pattern in each file. It is fast and lightweight due to efficient pattern matching algorithms (Typically Boyer-Moore).

This is not a production-level project (Java, not C++ or Rust), it's just for me to practice my design + coding skills and learn about pattern matching algorithms + multi-threading in Java. </br>
For a fast implementation of `grep` that makes use of modern computer architecture, please check out [ripgrep](https://github.com/BurntSushi/ripgrep).

## Architecture
Gred uses a producer-consumer model
- A single producer thread recursively traverses the directory tree and enqueues file paths into a `BlockingQueue`.
- Multiple consumer threads dequeue file paths, perform file I/O and pattern matching, and store matches in a shared `ConcurrentHashMap`.

I think this works best for a realistic file system where files vary in size and I/O is the main blocker - however, benchmarking is still in progress.

## Usage
### Using Java
Install the Java 17 JRE
``` bash
./gradlew clean build shadowJar
java -jar build/libs/gred-1.0-SNAPSHOT-all.jar PATTERN [DIRECTORY]
```
You can specify the pattern matching algorithm you want to use with flags (Default is Boyer-Moore).
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

## Algorithm Summaries
- Rabin-Karp
  - Uses a rolling hash (usually moving sum) to avoid pointless checks. Hard to pick a good rolling hash that balances correctness and speed.
  - Average Case: O(m + n) Assuming a good hash function that avoids collisions.
  - Worst Case: O(mn) If there are many collisions.
- Boyer-Moore
  - 2 Precomputations, Bad Char Rule to skip char mismatches that don't occur again in the pattern and Good Suffix Rule to maintain existing suffix matches.
  - Average Case: O(n/m) esp when |alphabet| >> |unique_pattern_chars| as the Bad Character rule will often trigger large jumps.
  - Worst Case: O(mn) if pattern and text are all same characters. Can guarantee linear bound with Apostolico-Giancarlo version (Not implemented).
- Knuth-Morris-Pratt

## Concurrency
Currently using a single producer (directory traversal) and multiple consumer (reading and matching) approach.


## Future Work
- Handle utf chars in boyer moore bad char table
- Regex support

## Note to self
- `sudo docker run --rm -it --entrypoint=/bin/sh gred`
