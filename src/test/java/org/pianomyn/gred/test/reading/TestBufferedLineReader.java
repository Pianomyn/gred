package org.pianomyn.gred.test.reading;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pianomyn.gred.matching.BoyerMoore;
import org.pianomyn.gred.reading.BufferedLineReader;
import org.pianomyn.gred.test.FileSystemUtility;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestBufferedLineReader {
    Path directoryPath;

    @BeforeEach
    public void setup() {
        this.directoryPath = FileSystemUtility.createUniqueTestDirectory();
    }

    @AfterEach
    public void teardown() {
        FileSystemUtility.deleteDirectoriesRecursive(this.directoryPath.toFile());
    }

    @Test
    public void testFileDoesntExist() {
        // Arrange Act and Assert
        IOException exception = assertThrows(IOException.class, () -> {
            new BufferedLineReader(directoryPath.resolve("non-existant.txt"));
        });
    }
}
