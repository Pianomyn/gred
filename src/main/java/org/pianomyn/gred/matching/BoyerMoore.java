package org.pianomyn.gred.matching;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
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
    int[] goodSuffixTable = this.createGoodSuffixTable();

    try (BufferedReader br = Files.newBufferedReader(this.filePath)) {
      String line;
      int lineNumber = 1;
      while ((line = br.readLine()) != null) {
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
            result.add(Arrays.asList(lineNumber, textIndex - m + 1));
            textIndex++;
          } else {
            textIndex += shift;
          }
        }
        lineNumber++;
      }
    } catch (MalformedInputException e) {
      System.out.println(
          String.format(
              "It seems like %s contains binary data instead of text. Skipping this file.",
              this.filePath.toString()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
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
        return Math.max(badCharSuggestion, goodSuffixSuggestion);
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
          badCharTable[r][c] = c + 1;
        }

        lastSeenIndex.put(currentChar, c);
      }
    }
    return badCharTable;
  }

  public int[] createGoodSuffixTable() {
    int m = this.pattern.length();
    int[] goodSuffixTable = new int[m];
    Arrays.fill(goodSuffixTable, -1);

    // Iterate over all suffixes
    for (int i = m - 1; i > -1; i--) {
      String suffix = this.pattern.substring(i);
      int suffixLength = m - i;

      // Sliding window to find the first occurrence of the suffix to the left
      int left = i - suffixLength;

      while (left > -1) {
        if (this.pattern.substring(left, left + suffixLength).equals(suffix)) {
          goodSuffixTable[i] = i - left;
          break;
        }
        left--;
      }
    }

    return goodSuffixTable;
  }
}
