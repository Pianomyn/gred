package org.pianomyn.gred.test.matching;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pianomyn.gred.matching.implementations.Naive;
import org.pianomyn.gred.matching.implementations.RabinKarp;
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
    this.rk = new RabinKarp(new HashMap<>(), null, "abc");
    this.naive = new Naive(new HashMap<>(), null, "abc");
  }

  @AfterEach
  public void teardown() {
    FileSystemUtility.deleteDirectoriesRecursive(this.directoryPath.toFile());
  }

  @Test
  public void testPatternNull() {
    // Arrange
    this.rk.setPattern(null);
    this.rk.findMatches();

    // Act and Assert
    assert (this.rk.getMatches().isEmpty());
  }

  @Test
  public void testPatternLengthZero() {
    // Arrange
    this.rk.setPattern("");
    FileSystemUtility.appendToFile(this.directoryPath, Paths.get("testFile.txt"), "asdf");
    this.rk.findMatches();

    // Act and Assert
    assert (this.rk.getMatches().isEmpty());
  }

  @Test
  public void testPatternLongerThanText() {
    // Arrange
    FileSystemUtility.appendToFile(this.directoryPath, Paths.get("testFile.txt"), "a");
    this.rk.setPattern("REALLY LONG PATTERN!");

    LineReader reader = null;
    try {
      reader = new BufferedLineReader(this.directoryPath.resolve("testFile.txt"));
    } catch (IOException e) {

    }

    this.rk.setReader(reader);
    this.rk.findMatches();

    // Act and Assert
    assert (this.rk.getMatches().isEmpty());
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
    this.rk.findMatches();
    this.naive.findMatches();

    List<List<Integer>> rkResult = this.rk.getMatches();
    List<List<Integer>> naiveResult = this.naive.getMatches();

    // Assert
    assert (rkResult.size() == 1);
    assert (naiveResult.size() == 1);
    assert (rkResult.equals(naiveResult));
  }
}
