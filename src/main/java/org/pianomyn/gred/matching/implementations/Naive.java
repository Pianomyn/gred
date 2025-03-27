package org.pianomyn.gred.matching.implementations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.pianomyn.gred.matching.MatchingAlgorithm;
import org.pianomyn.gred.reading.LineReader;

public class Naive extends MatchingAlgorithm {

  public Naive(Map<String, List<List<Integer>>> matches, LineReader reader, String pattern) {
    super(matches, reader, pattern);
  }

  @Override
  public void findMatches() {
    if (this.pattern.isEmpty()) {
      return;
    }

    String matchKey = this.reader.getFilePathAsString();
    this.matches.computeIfAbsent(matchKey, k -> new ArrayList<>()).clear();

    int m = this.pattern.length();

    String line;
    int lineNumber = 1;
    try {
      while ((line = this.reader.readLine()) != null) {
        int n = line.length();
        if (m > n) {
          continue;
        }

        for (int i = 0; i <= n - m; i++) {
          if (this.pattern.equals(line.substring(i, i + m))) {
            this.matches.get(matchKey).add(Arrays.asList(lineNumber, i));
          }
        }
        lineNumber++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
