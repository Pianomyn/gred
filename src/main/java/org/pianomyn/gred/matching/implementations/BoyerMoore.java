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
        return Math.max(
                1,
                Math.max(
                  badCharSuggestion, goodSuffixSuggestion
                )
        );
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
    /*
    If a mismatch at i-1, index i tells us how much to shift to
    - Preserve the entire suffix from i to m-1
    - Preserve some part of the suffix (different start position to i).
    */
    int m = this.pattern.length();
    int[] goodSuffixTable = new int[m+1];
    int[] shiftTable = new int[m+1];

    createGoodSuffixTableCase1(shiftTable, goodSuffixTable);
    createGoodSuffixTableCase2And3(shiftTable, goodSuffixTable);

    return goodSuffixTable;
  }

  private void createGoodSuffixTableCase1(int[] shiftTable, int[] goodSuffixTable) {
    int m = this.pattern.length();
    int left = m;
    int right = m+1;
    shiftTable[left] = right;

    while(left > 0) {
      while(right <= m && this.pattern.charAt(left-1) != this.pattern.charAt(right-1)) {
        if(goodSuffixTable[right] == 0) {
          goodSuffixTable[right] = right-left;
        }
        right = shiftTable[right];
      }
      left--;
      right--;
      shiftTable[left] = right;
    }
  }

  private void createGoodSuffixTableCase2And3(int[] shiftTable, int[] goodSuffixTable) {
    int m = this.pattern.length();
    int left, right=shiftTable[0];

    for(left = 0; left <= m; left++) {
      if(goodSuffixTable[left] == 0) goodSuffixTable[left] = right;
      if(left == right) right = shiftTable[right];
    }

    for(int i = 0; i <= m; i++) {
      if(goodSuffixTable[i] == 0) goodSuffixTable[i] = m;
    }
  }

}
