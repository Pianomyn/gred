package org.pianomyn.gred.matching;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Naive extends MatchingAlgorithm {

  public Naive(Path filePath, String pattern) {
    super(filePath, pattern);
  }

  @Override
  public List<List<Integer>> findMatches() {
    List<List<Integer>> result = new ArrayList<List<Integer>>();
    if (pattern == null || !this.fileExists()) {
      return result;
    }

    int m = this.pattern.length();
    if (m == 0) {
      return result;
    }

    try (BufferedReader br = Files.newBufferedReader(this.filePath)) {
      String line;
      int lineNumber = 1;
      while ((line = br.readLine()) != null) {
        int n = line.length();
        if (m > n) {
          continue;
        }

        for (int i = 0; i < n - m; i++) {
          if (pattern.equals(line.substring(i, i + m - 1))) {
            result.add(Arrays.asList(lineNumber, i));
          }
        }
        lineNumber++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return result;
  }
}
