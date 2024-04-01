package org.pianomyn.gred.matching;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoyerMoore extends MatchingAlgorithm {
  public BoyerMoore(Path filePath, String pattern) { super(filePath, pattern); }

  @Override
  public List<List<Integer>> findMatches() {
    /*
     * Bad char table
     *   Should be matrix (all chars in alphabet vs all chars in pattern)
     * TODO: Good suffix table
     *
     * Line up pattern with left side of text
     * Iterate from right to left
     *  If match, advance pattern by 1 (as pattern could overlap with itself)
     *  Else (Idea is to shift pattern right to make the mismatch a match)
     *    If mismatching character doesnt exist in pattern, shift by length of
     * remaining pattern If exists, shift to rightmost occurrence to the left of
     * the mismatch For both, consult bad char table
     */
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
        // Logic here
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  int[][] createBadCharTable() {
    int patternLength = this.pattern.length();
    int[][] badCharTable = new int[128][patternLength]; // ASCII 128
    Map<Character, Integer> lastSeenIndex = new HashMap<>();

    for (int r = 0; r < 128; r++) {
      lastSeenIndex.clear();
      for (int c = 0; c < patternLength; c++) {
        char currentChar = (char)r;

        if (this.pattern.charAt(c) == currentChar) {
          badCharTable[r][c] = 0;
        } else if (lastSeenIndex.containsKey(currentChar)) {
          badCharTable[r][c] = c - lastSeenIndex.get(currentChar);
        } else {
          badCharTable[r][c] = c + 1;
        }

        lastSeenIndex.put(currentChar, c);
      }
    }
    return badCharTable;
  }
}
