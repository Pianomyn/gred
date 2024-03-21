package org.pianomyn.gred.test.matching;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pianomyn.gred.test.Utility;
import org.pianomyn.gred.traversal.BFS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Queue;

public class BFSTest {
  private Path directoryPath;
  private BFS b;


  public void deleteTestDirectory() {
    File directory = new File(this.directoryPath.toString());
    deleteDirectoriesRecursive(directory);
  }

  public static void deleteDirectoriesRecursive(File file) {
    if (file.isDirectory()) {
      File[] files = file.listFiles();
      if (files == null) {
        return;
      }
      for (File subFile : files) {
        deleteDirectoriesRecursive(subFile);
      }
    }
    file.delete();
  }

  @BeforeEach
  public void setup() {
    this.directoryPath = Utility.createUniqueTestDirectory();
    this.b = new BFS(this.directoryPath);
  }

  @AfterEach
  public void teardown() {
    Utility.deleteDirectoriesRecursive(new File(this.directoryPath.toString()));
  }

  @Test
  public void testTraverseEmpty() {
    Queue<Path> result = this.b.traverse();
    assert result.isEmpty();
  }

  @Test
  public void testTraverseSingleLevel() {
    // Arrange
    String[] fileNames = {"file1.txt", "file2.txt"};
    for (String names : fileNames) {
      try {
        Path filePath = this.directoryPath.resolve(names);
        Files.createFile(filePath);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // Act
    Queue<Path> result = this.b.traverse();

    // Assert
    assert result.size() == 2;
    assert Objects.equals(result.poll(), this.directoryPath.resolve(fileNames[0]));
    assert Objects.equals(result.poll(), this.directoryPath.resolve(fileNames[1]));
  }

  @Test
  public void testTraverseNested() {
    // Arrange
    String[] fileNames = {"file1.txt", "file2.txt"};
    Path nestedDirectory = this.directoryPath.resolve("nested_directory");
    try {
      Files.createDirectory(nestedDirectory);
      for (String name : fileNames) {
        Path filePath = this.directoryPath.resolve(name);
        Files.createFile(filePath);
        filePath = nestedDirectory.resolve(name);
        Files.createFile(filePath);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Act
    Queue<Path> result = this.b.traverse();

    // Assert
    assert result.size() == 4;
    for (int i = 0; i < 2; i++) {
      assert Objects.equals(result.poll(), this.directoryPath.resolve(fileNames[i]));
    }
    for (int i = 0; i < 2; i++) {
      assert Objects.equals(result.poll(), (nestedDirectory.resolve(fileNames[i])));
    }
  }
}
