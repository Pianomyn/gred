package org.pianomyn.gred.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utility {
  public static Path createUniqueTestDirectory() {
    String directoryName = "test_directory";
    Path testDirectory = Paths.get(directoryName);
    if (!Files.exists(testDirectory)) {
      try {
        Files.createDirectory(testDirectory);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      int counter = 1;
      while (Files.exists(testDirectory)) {
        testDirectory = Paths.get(directoryName + "_" + counter);
        counter++;
      }
      try {
        Files.createDirectory(testDirectory);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    //this.directoryPath = testDirectory;
    return testDirectory;
  }

  public static void deleteDirectoriesRecursive(File file) {
    if (file.isDirectory()) {
      File[] files = file.listFiles();
      for (File subFile : files) {
        deleteDirectoriesRecursive(subFile);
      }
    }
    file.delete();
  }

  public static void appendToFile(Path directoryPath, Path filePath, String textToAppend) {
    try {
      // Create a FileWriter object in append mode
      FileWriter fw = new FileWriter(directoryPath.resolve(filePath).toString(), true);

      // Wrap the FileWriter in a BufferedWriter for efficient writing
      BufferedWriter bw = new BufferedWriter(fw);

      // Append the text to the file
      bw.write(textToAppend);
      bw.newLine(); // Add a newline after the text if needed

      // Close the BufferedWriter
      bw.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
