# Gred - A pattern matching program
A fast pattern matching program similar in concept to the
Unix command-line tool "grep".

It will traverse the file system starting at a particular directory
and search all descendent files for matches with the specified pattern.

Searching is done with either the Rabin-Karp algorithm or the Boyer-Moore algorithm (also used by grep).

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
