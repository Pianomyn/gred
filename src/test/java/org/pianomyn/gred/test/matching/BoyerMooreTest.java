package org.pianomyn.gred.test.matching;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pianomyn.gred.matching.BoyerMoore;
import org.pianomyn.gred.matching.Naive;
import org.pianomyn.gred.test.Utility;
import org.pianomyn.gred.traversal.BFS;

public class BoyerMooreTest {
  private Path directoryPath;
  private BoyerMoore bm;
  private Naive naive;

  @BeforeEach
  public void setup() {
    this.directoryPath = Utility.createUniqueTestDirectory();
    this.bm = new BoyerMoore(this.directoryPath, "abc");
    this.naive = new Naive(this.directoryPath, "abc");
  }

  @AfterEach
  public void teardown() {
    Utility.deleteDirectoriesRecursive(this.directoryPath.toFile());
  }

  @Test
  public void testFileDoesntExist() {
    // Arrange
    this.directoryPath = this.directoryPath.resolve("non_existant_file.txt");
    this.bm.setPathToSearch(this.directoryPath);

    // Act
    List<List<Integer>> result = this.bm.findMatches();

    // Assert
    assert (result.isEmpty());
  }

  @Test
  public void testPatternNull() {
    // Arrange
    this.bm.setPattern(null);
    Utility.appendToFile(this.directoryPath, Paths.get("testFile.txt"), "asdf");

    // Act and Assert
    assert (this.bm.findMatches().isEmpty());
  }

  @Test
  public void testPatternLengthZero() {
    // Arrange
    this.bm.setPattern(null);
    Utility.appendToFile(this.directoryPath, Paths.get("testFile.txt"), "asdf");

    // Act and Assert
    assert (this.bm.findMatches().isEmpty());
  }

  @Test
  public void testPatternLongerThanText() {
    // Arrange
    this.bm.setPattern(null);
    Utility.appendToFile(this.directoryPath, Paths.get("testFile.txt"), "a");

    // Act and Assert
    assert (this.bm.findMatches().isEmpty());
  }

  @Test
  public void testSingleFileMatch() {
    // Arrange
    this.bm.setPattern("GCAGAGAG");
    this.naive.setPattern("GCAGAGAG");
    Utility.appendToFile(this.directoryPath, Paths.get("testFile.txt"), "GCATCGCAGAGAGTATACAGTACG");

    // Act
    BFS traversal = new BFS(this.directoryPath);
    Queue<Path> files = traversal.traverse();

    List<List<Integer>> bmResult = new ArrayList<List<Integer>>();
    List<List<Integer>> naiveResult = new ArrayList<List<Integer>>();
    for (Path file : files) {
      this.naive.setPathToSearch(file);
      this.bm.setPathToSearch(file);

      bmResult.addAll(this.bm.findMatches());
      naiveResult.addAll(this.naive.findMatches());
    }

    // Assert
    assert (bmResult.size() == 1);
    assert (naiveResult.size() == 1);
    assert (bmResult.equals(naiveResult));
  }
}
