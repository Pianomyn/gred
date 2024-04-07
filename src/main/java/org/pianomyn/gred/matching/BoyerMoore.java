package org.pianomyn.gred.matching;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoyerMoore extends MatchingAlgorithm {
  public BoyerMoore(Path filePath, String pattern) {
    super(filePath, pattern);
  }

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

    int[][] badCharTable = this.createBadCharTable();

    try (BufferedReader br = Files.newBufferedReader(this.filePath)) {
      String line;
      int lineNumber = 1;
      while ((line = br.readLine()) != null) {
        int n = line.length();
        int textIndex = m - 1;

        while (textIndex < n) {
          int shift =
              this.checkMatch(line, this.pattern, this.pattern.length(), textIndex, badCharTable);
          if (shift == 0) {
            result.add(Arrays.asList(lineNumber, textIndex - m + 1));
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
    return result;
  }

  int checkMatch(
      String text, String pattern, int patternLength, int textIndex, int[][] badCharTable) {
    int patternIndex = patternLength - 1;
    while (patternLength > 0) {
      if (text.charAt(textIndex) != pattern.charAt(patternIndex)) {
        return badCharTable[(int) text.charAt(textIndex)][patternIndex];
      }

      patternLength--;
      textIndex--;
      patternIndex--;
    }
    // Uses 0 to signify a match. Only shift by 1 because matches could be overlapping.
    return 0;
  }

  int[][] createBadCharTable() {
    // Alphabet size * pattern length
    int patternLength = this.pattern.length();
    int[][] badCharTable = new int[128][patternLength]; // ASCII 128
    Map<Character, Integer> lastSeenIndex = new HashMap<>();

    for (int r = 0; r < 128; r++) {
      lastSeenIndex.clear();
      for (int c = 0; c < patternLength; c++) {
        char currentChar = (char) r;

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

  int[][] createGoodSuffixTable() {
    int patternLength = this.pattern.length();
    int[][] goodSuffixTable = new int[patternLength][patternLength];
    return goodSuffixTable;
  }
}
