# Gred - A pattern matching program
A fast pattern matching program similar in concept to the
Unix command-line tool "grep".

It will traverse the file system starting at a particular directory
and search all descendent files for matches with the specified pattern.

The returned output will be formatted like

/path/to/match1 lineNumber columnNumber
/path/to/match2 lineNumber columnNumber
...

IMAGE

## Usage
### Using Java
Install the Java 17 JRE
``` bash
./gradlew build
java build/libs/gred-1.0-SNAPSHOT.jar PATTERN DIRECTORY
```

### Using Docker
Install the docker command line tool.
```
docker build -t IMAGE_NAME .
docker run -v DIRECTORY:/MOUNTED_DIR IMAGE_NAME PATTERN MOUNTED_DIR
```

## Development
- Github Actions is used for CI. Create a PR to
    - Run all tests
    - Run lint checks using checkstyle
- Gradle tasks exist for linting and formatting locally
    - `gradlew lint` Uses checkstyle and spotless
    - `gradlew format` Uses spotless

## Future Work
- Multi-threading
- Regex support

## Note to self
sudo docker run --rm -it --entrypoint=/bin/sh gred
