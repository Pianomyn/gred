package org.pianomyn.gred.matching;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.pianomyn.gred.reading.LineReader;

public class Naive extends MatchingAlgorithm {

  public Naive(LineReader reader, String pattern) {
    super(reader, pattern);
  }

  @Override
  public List<List<Integer>> findMatches() {
    List<List<Integer>> result = new ArrayList<List<Integer>>();

    int m = this.getPattern().length();
    if (m == 0) {
      return result;
    }

    String line;
    int lineNumber = 1;
    try {
      while ((line = this.getReader().readLine()) != null) {
        int n = line.length();
        if (m > n) {
          continue;
        }

        for (int i = 0; i <= n - m; i++) {
          if (this.getPattern().equals(line.substring(i, i + m))) {
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
