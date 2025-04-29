package org.pianomyn.gred.matching.implementations;

import java.io.IOException;
import java.util.*;
import org.pianomyn.gred.matching.MatchingAlgorithm;
import org.pianomyn.gred.reading.LineReader;

public class BoyerMoore extends MatchingAlgorithm {
  public BoyerMoore(Map<String, List<List<Integer>>> matches, LineReader reader, String pattern) {
    super(matches, reader, pattern);
  }

  @Override
  public void findMatches() {
    if (this.pattern == null || this.pattern.isEmpty() || this.reader == null) {
      return;
    }

    String matchKey = this.reader.getFilePathAsString();
    this.matches.computeIfAbsent(matchKey, k -> new ArrayList<>()).clear();

    int m = this.pattern.length();

    int[][] badCharTable = this.createBadCharTable();
    int[] goodSuffixTable = this.createGoodSuffixTable();

    String line;
    int lineNumber = 0;
    try {
      while ((line = this.reader.readLine()) != null) {
        int n = line.length();
        int textIndex = m - 1;

        while (textIndex < n) {
          int shift =
              this.checkMatch(
                  line,
                  this.pattern,
                  this.pattern.length(),
                  textIndex,
                  badCharTable,
                  goodSuffixTable);
          if (shift == 0) {
            this.matches.get(matchKey).add(Arrays.asList(lineNumber, textIndex - m + 1));
            textIndex++;
          } else {
            textIndex += shift;
          }
        }
        lineNumber++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  int checkMatch(
      String text,
      String pattern,
      int m,
      int textIndex,
      int[][] badCharTable,
      int[] goodSuffixTable) {
    int patternIndex = m - 1;
    while (m > 0) {
      if (text.charAt(textIndex) != pattern.charAt(patternIndex)) {
        int badCharSuggestion = badCharTable[(int) text.charAt(textIndex)][patternIndex];
        int goodSuffixSuggestion = 1;
        if (patternIndex + 1 < m) {
          goodSuffixSuggestion = goodSuffixTable[patternIndex + 1];
        }
        return Math.max(1, Math.max(badCharSuggestion, goodSuffixSuggestion));
      }

      m--;
      textIndex--;
      patternIndex--;
    }
    // Uses 0 to signify a match. Only shift by 1 because matches could be
    // overlapping.
    return 0;
  }

  int[][] createBadCharTable() {
    // Alphabet size * pattern length
    int m = this.pattern.length();
    int[][] badCharTable = new int[128][m]; // ASCII 128
    Map<Character, Integer> lastSeenIndex = new HashMap<>();

    for (int r = 0; r < 128; r++) {
      lastSeenIndex.clear();
      for (int c = 0; c < m; c++) {
        char currentChar = (char) r;

        if (this.pattern.charAt(c) == currentChar) {
          badCharTable[r][c] = 0;
        } else if (lastSeenIndex.containsKey(currentChar)) {
          badCharTable[r][c] = c - lastSeenIndex.get(currentChar);
        } else {
          badCharTable[r][c] = Math.max(c+1, m-c);
        }

        lastSeenIndex.put(currentChar, c);
      }
    }
    return badCharTable;
  }

  public int[] createGoodSuffixTable() {
    /*
    Terminology
    - A proper prefix is the entire prefix starting from some index i (As opposed to a partial prefix).
    - Some substring that is both a proper prefix and proper suffix is called a "border".

    If a mismatch at i-1, there are 2 cases
    - Weak Good Suffix Rule
      - Preserve the entire suffix (proper prefix) from i to m-1 (Another instance of the entirely matched suffix exists earlier in the pattern).
      - The i-1 char must be different, otherwise, same mismatch would occur.
      - Line up that occurrence with the suffix. Less useful as it's less likely for this to happen.
    - Strong Good Suffix Rule
      - Some part of the suffix exists as a prefix (ends at m-1, starts at index greater than i).
      - Line the occurrences up.
    */
    int m = this.pattern.length();
    // Index m can be treated as the "empty string", AKA no such border match.
    int[] borderPositions =
        new int[m + 1];
    int[] goodSuffixTable = new int[m + 1];

    createStrongGoodSuffixRule(borderPositions, goodSuffixTable);
    createWeakGoodSuffixRule(borderPositions, goodSuffixTable);

    return goodSuffixTable;
  }

  private void createStrongGoodSuffixRule(int[] borderPositions, int[] goodSuffixTable) {
    /*
     For each substring beginning at `left`, what is the longest border?
     If char before suffix and prefix are same, we can expand border.
     Else, try to find valid prev border.

     Time: O(m)
       Outer loop: O(m)
       Inner Loop: O(m) ammortized across all outer loop iterations.
    */
    int m = this.pattern.length();
    int left = m;
    int right = m + 1;
    borderPositions[left] = right;

    while (left > 0) {
      // Unable to expand current border. Recursively try to find next longest border (each one
      // smaller than current border).
      while (right <= m && this.pattern.charAt(left - 1) != this.pattern.charAt(right - 1)) {
        if (goodSuffixTable[right] == 0) {
          goodSuffixTable[right] = right - left;
        }
        right = borderPositions[right];
      }

      // Expand current border
      left--;
      right--;
      borderPositions[left] = right;
    }
  }

  private void createWeakGoodSuffixRule(int[] borderPositions, int[] goodSuffixTable) {
    int m = this.pattern.length();
    int left, right = borderPositions[0];

    for (left = 0; left <= m; left++) {
      if (goodSuffixTable[left] == 0) goodSuffixTable[left] = right;
      if (left == right) right = borderPositions[right];
    }

    for (int i = 0; i <= m; i++) {
      if (goodSuffixTable[i] == 0) goodSuffixTable[i] = m;
    }
  }
}
