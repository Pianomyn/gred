package org.pianomyn.gred.test.matching;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pianomyn.gred.matching.implementations.BoyerMoore;
import org.pianomyn.gred.matching.implementations.Naive;
import org.pianomyn.gred.reading.BufferedLineReader;
import org.pianomyn.gred.reading.LineReader;
import org.pianomyn.gred.test.FileSystemUtility;

public class BoyerMooreTest {
  private Path directoryPath;
  private BoyerMoore bm;
  private Naive naive;

  @BeforeEach
  public void setup() {
    this.directoryPath = FileSystemUtility.createUniqueTestDirectory();
    this.bm = new BoyerMoore(new HashMap<>(), null, "");
    this.naive = new Naive(new HashMap<>(), null, "");
  }

  @AfterEach
  public void teardown() {
    FileSystemUtility.deleteDirectoriesRecursive(this.directoryPath.toFile());
  }

  @Test
  public void testPatternNull() {
    // Arrange
    this.bm.setPattern(null);
    this.bm.findMatches();

    // Act and Assert
    assert (this.bm.getMatches().isEmpty());
  }

  @Test
  public void testPatternLengthZero() {
    // Arrange
    this.bm.setPattern("");
    FileSystemUtility.appendToFile(this.directoryPath, Paths.get("testFile.txt"), "asdf");
    this.bm.findMatches();

    // Act and Assert
    assert (this.bm.getMatches().isEmpty());
  }

  @Test
  public void testPatternLongerThanText() {
    // Arrange
    FileSystemUtility.appendToFile(this.directoryPath, Paths.get("testFile.txt"), "a");
    this.bm.setPattern("REALLY LONG PATTERN!");
    try {
      this.bm.setReader(new BufferedLineReader(this.directoryPath.resolve("testFile.txt")));
      int a = 2;
    } catch (IOException e) {
    }
    this.bm.findMatches();

    // Act and Assert
    assert (this.bm.getMatches().isEmpty());
  }

  @Test
  public void testCreateGoodSuffixTableSimple() {
    // Arrange
    String pattern = "sirhellofadhello";
    int m = pattern.length();
    Set<Integer> suffixIndices = new HashSet<>();
    this.bm.setPattern(pattern);

    // Act
    for (int i = 1; i < 6; i++) {
      suffixIndices.add(m - i);
    }
    int[] goodSuffixTable = this.bm.createGoodSuffixTable();

    // Assert
    for (int i = m - 1; i > -1; i--) {
      if (suffixIndices.contains(i)) {
        assert goodSuffixTable[i] == 8;
      } else {
        assert goodSuffixTable[i] == -1;
      }
    }
  }

  @Test
  public void testSingleFileMatch() {
    // Arrange
    // this.directoryPath = this.directoryPath.resolve("testFile.txt");
    FileSystemUtility.appendToFile(
        this.directoryPath, Paths.get("testFile.txt"), "GCATCGCAGAGAGTATACAGTACG");
    this.bm.setPattern("GCAGAGAG");
    this.naive.setPattern("GCAGAGAG");
    LineReader bmReader = null;
    LineReader naiveReader = null;
    try {
      bmReader = new BufferedLineReader(directoryPath.resolve("testFile.txt"));
      naiveReader = new BufferedLineReader(directoryPath.resolve("testFile.txt"));
    } catch (IOException e) {

    }
    this.bm.setReader(bmReader);
    this.naive.setReader(naiveReader);

    this.bm.findMatches();
    this.naive.findMatches();
    List<List<Integer>> bmResult = this.bm.getMatches();
    List<List<Integer>> naiveResult = this.naive.getMatches();

    // Assert
    System.out.println(bmResult.size());
    assert (bmResult.size() == 1);
    assert (naiveResult.size() == 1);
    assert (bmResult.equals(naiveResult));
  }
}
