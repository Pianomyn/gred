# Gred - A Pattern Matching Tool
Many everyday computing tasks like running plagiarism checks, searching with Control+F and filtering emails rely on efficient pattern matching algorithms.

Gred is a fast pattern matching tool inspired by the popular
Unix command-line utility `grep`.

It recursively traverses a directory (current directory by default)
and search all descendant files for occurrences of a given pattern.

I'm working on this project to improve my coding skills (splitting concerns across domains, testing and problem-solving) </br>
while also getting familiar with the structure of Java projects and learning
about the fascinating and well-researched field of pattern matching.



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

## Notes (For myself so I don't forget!)
- Pattern matching with well optimized pattern matching algorithms. Currently implemented
  - <u><b>Rabin-Karp</b></u>
    - Average Case: O(m + n) Assuming a good hash function that avoids collisions.
    - Worst Case: O(mn) If there are many collisions.
    - Uses a rolling hash (usually moving sum) to avoid pointless checks. Hard to pick a good rolling hash that balances correctness and speed.
  - <u><b>Boyer-Moore</b></u> ([Primer](https://www.youtube.com/watch?v=4Xyhb72LCX4&t=200s))
    - Average Case: O(n/m) esp when |alphabet| >> |unique_pattern_chars| as the Bad Character rule will often trigger large jumps.
    - Worst Case: O(mn) eg if pattern and text are all same characters. Can guarantee linear bound with Apostolico-Giancarlo version (Not implemented).
    - Precomputation on the pattern to skip redundant matches. Take the best of the 2 suggestions.
    - <b>Bad Char Table Precomputation</b> (Skip until mismatch becomes match):
      - Right to left
      - Classic approach is to use map to track last index occurrence. Less space, worse time complexity. Alternatively, create a matrix of size `pattern_length` * `|alphabet|`.
      - For each i in range \[0, pattern_length\], compute the distance that each character in the alphabet was last seen based on i-1.
    - <b>Good Suffix Table Precomputation</b> ([Primer](https://medium.com/@neethamadhu.ma/good-suffix-rule-in-boyer-moore-algorithm-explained-simply-9d9b6d20a773) Don't turn any existing matches into a mismatch)
      - Right to left
      - Case 1 (Weak): The **entire suffix matched before the mismatch** occurs **elsewhere in the pattern**.
        - Shift the pattern so that this earlier occurrence of the suffix lines up with the text.
      - Case 2 (Strong): A **proper suffix** of the matched part is also a **prefix** of the pattern.
        - Shift the pattern so that this prefix lines up with the suffix in the text.
      - A shift table is computed using both cases to determine how far the pattern can be safely shifted on a mismatch, **while preserving suffix matches**.
  - <u><b>KMP</b></u> (WIP)
    - Average Case: O(n)
    - Worst Case: O(m + n)
    - <b>Last Prefix Suffix</b>: Precomputation on the pattern to skip redundant matches.
      - Computation
        - Create an array of size `pattern_length` called `lps`.
        - Use 2 pointers, `left` and `right` and start both at 0.
        - If `pattern[right]` == `pattern[left]`, Set `lps`
- Recursive directory traversal
- Clearly formatted output
- `sudo docker run --rm -it --entrypoint=/bin/sh gred`
## Future Work
- Measure performance against grep
- Multi-threading
- Handle utf chars in boyer moore table
- Regex support

## Note to self
