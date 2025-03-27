package org.pianomyn.gred.test.matching;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pianomyn.gred.matching.algorithms.Naive;
import org.pianomyn.gred.matching.algorithms.RabinKarp;
import org.pianomyn.gred.reading.BufferedLineReader;
import org.pianomyn.gred.reading.LineReader;
import org.pianomyn.gred.test.FileSystemUtility;

public class RabinKarpTest {
  private Path directoryPath;
  private RabinKarp rk;
  private Naive naive;

  @BeforeEach
  public void setup() {
    this.directoryPath = FileSystemUtility.createUniqueTestDirectory();
    this.rk = new RabinKarp(null, "abc");
    this.naive = new Naive(null, "abc");
  }

  @AfterEach
  public void teardown() {
    FileSystemUtility.deleteDirectoriesRecursive(this.directoryPath.toFile());
  }

  @Test
  public void testPatternNull() {
    // Arrange
    this.rk.setPattern(null);

    // Act and Assert
    assert (this.rk.findMatches().isEmpty());
  }

  @Test
  public void testPatternLengthZero() {
    // Arrange
    this.rk.setPattern("");
    FileSystemUtility.appendToFile(this.directoryPath, Paths.get("testFile.txt"), "asdf");

    // Act and Assert
    assert (this.rk.findMatches().isEmpty());
  }

  @Test
  public void testPatternLongerThanText() {
    // Arrange
    FileSystemUtility.appendToFile(this.directoryPath, Paths.get("testFile.txt"), "a");
    this.rk.setPattern("REALLY LONG PATTERN!");

    try {
      this.rk.setReader(new BufferedLineReader(this.directoryPath.resolve("testFile.txt")));
    } catch (IOException e) {
    }

    // Act and Assert
    assert (this.rk.findMatches().isEmpty());
  }

  @Test
  public void testSingleFileMatch() {
    // Arrange
    this.rk.setPattern("GCAGAGAG");
    this.naive.setPattern("GCAGAGAG");
    FileSystemUtility.appendToFile(
        this.directoryPath, Paths.get("testFile.txt"), "GCATCGCAGAGAGTATACAGTACG");

    LineReader rkReader = null;
    LineReader naiveReader = null;
    try {
      rkReader = new BufferedLineReader(directoryPath.resolve("testFile.txt"));
      naiveReader = new BufferedLineReader(directoryPath.resolve("testFile.txt"));
    } catch (IOException e) {
    }
    this.rk.setReader(rkReader);
    this.naive.setReader(naiveReader);

    // Act
    List<List<Integer>> rkResult = this.rk.findMatches();
    List<List<Integer>> naiveResult = this.naive.findMatches();

    // Assert
    assert (rkResult.size() == 1);
    assert (naiveResult.size() == 1);
    assert (rkResult.equals(naiveResult));
  }
}
