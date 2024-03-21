package org.pianomyn.gred.test.matching;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pianomyn.gred.matching.RabinKarp;
import org.pianomyn.gred.test.Utility;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RabinKarpTest {
  private Path directoryPath;
  private RabinKarp rk;
  @BeforeEach
  public void setup() {
    this.directoryPath = Utility.createUniqueTestDirectory();
    this.rk = new RabinKarp(this.directoryPath, "abc");
  }

  @AfterEach
  public void teardown() {
    Utility.deleteDirectoriesRecursive(new File(this.directoryPath.toString()));
  }

  @Test
  public void testPatternNull() {
    // Arrange
    this.rk.setPattern(null);
    Utility.appendToFile(this.directoryPath, Paths.get("testFile.txt"), "asdf");

    // Act and Assert
    assert(this.rk.findMatches().isEmpty());
  }
  @Test
  public void testPatternLengthZero() {
    // Arrange
    this.rk.setPattern(null);
    Utility.appendToFile(this.directoryPath, Paths.get("testFile.txt"), "asdf");

    // Act and Assert
    assert(this.rk.findMatches().isEmpty());
  }
  @Test
  public void testPatternLongerThanText() {
    // Arrange
    this.rk.setPattern(null);
    Utility.appendToFile(this.directoryPath, Paths.get("testFile.txt"), "a");

    // Act and Assert
    assert(this.rk.findMatches().isEmpty());
  }
}
