package org.pianomyn.gred.matching.implementations;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.pianomyn.gred.matching.MatchingAlgorithm;
import org.pianomyn.gred.reading.LineReader;

public class RabinKarp extends MatchingAlgorithm {
  private long BASE = 128; // Size of alphabet
  private long PRIME = 131; // Prime number close to size of alphabet (ASCII-128

  // in this instance).

  public RabinKarp(Map<String, List<List<Integer>>> matches, LineReader reader, String pattern) {
    super(matches, reader, pattern);
  }

  public RabinKarp(
      Map<String, List<List<Integer>>> matches,
      LineReader reader,
      String pattern,
      long base,
      long prime) {
    super(matches, reader, pattern);
    this.BASE = base;
    this.PRIME = prime;
  }

  @Override
  public List<List<Integer>> findMatches() {
    List<List<Integer>> result = new ArrayList<>();

    if (this.pattern == null || this.pattern.isEmpty()) {
      return result;
    }

    int m = this.pattern.length();
    if (m == 0) {
      return result;
    }

    String line;
    int lineNumber = 1;
    try {
      while ((line = this.reader.readLine()) != null) {
        int n = line.length();
        if (m > n) {
          continue;
        }

        long patternHash = this.hash(this.pattern, m);
        long textHash = this.hash(line.substring(0, m), m);

        for (int i = 0; i <= n - m; i++) {
          if (patternHash == textHash && RabinKarp.checkEqual(line, this.pattern, i, i + m - 1)) {
            result.add(Arrays.asList(lineNumber, i));
          }
          if (i < n - m) {
            textHash = recomputeHash(line, i, textHash, m);
          }
        }
        lineNumber++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return result;
  }

  private long hash(String str, int length) {
    long hashValue = 0;
    for (int i = 0; i < length; i++) {
      hashValue = (hashValue * this.BASE + str.charAt(i)); // Modulo?
    }
    return hashValue;
  }

  private long recomputeHash(String text, int oldIndex, long oldTextHash, int patternLength) {
    long newHash =
        oldTextHash - text.charAt(oldIndex) * (long) Math.pow(this.BASE, patternLength - 1);
    newHash = (newHash * this.BASE + text.charAt(oldIndex + patternLength)); // Modulo?
    return (newHash < 0) ? (newHash + this.PRIME) : newHash; // Integer overflow
  }

  private static boolean checkEqual(String text, String pattern, int start, int end) {
    int pIndex = 0;
    for (int i = start; i <= end; i++) {
      if (text.charAt(i) != pattern.charAt(pIndex++)) {
        return false;
      }
    }
    return true;
  }
}
