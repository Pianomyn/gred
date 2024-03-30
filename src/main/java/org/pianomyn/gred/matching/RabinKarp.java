package org.pianomyn.gred.matching;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RabinKarp extends MatchingAlgorithm {
  private int BASE = 256; // Size of alphabet
  private int PRIME = 101; // Prime number close to size of alphabet (ASCII-256 in this instance).

  public RabinKarp(Path filePath, String pattern) {
    super(filePath, pattern);
  }

  public RabinKarp(Path filePath, String pattern, int base, int prime) {
    super(filePath, pattern);
    this.BASE = base;
    this.PRIME = prime;
  }

  @Override
  public List<List<Integer>> findMatches() {
    List<List<Integer>> result = new ArrayList<List<Integer>>();
    if (pattern == null || !this.fileExists()) {
      return result;
    }

    int m = this.pattern.length();
    if(m == 0) {
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

        int patternHash = this.hash(pattern, m);
        int textHash = this.hash(line.substring(0, m), m);

        for (int i = 0; i <= n - m; i++) {
          if (
            patternHash == textHash
            && RabinKarp.checkEqual(line, pattern, i, i + m - 1)
          ) {
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

  private int hash(String str, int length) {
    int hashValue = 0;
    for (int i = 0; i < length; i++) {
      hashValue = (hashValue * this.BASE + str.charAt(i)) % this.PRIME;
    }
    return hashValue;
  }

  private int recomputeHash(
      String text, int oldIndex, int oldTextHash, int patternLength) {
    int newHash = oldTextHash - text.charAt(oldIndex) * (int) Math.pow(this.BASE, patternLength - 1);
    newHash = (newHash * this.BASE + text.charAt(oldIndex + patternLength)) % this.PRIME;
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

  public static void main(String[] args) throws FileNotFoundException {
    RabinKarp r = new RabinKarp(Paths.get(System.getProperty("user.home"), "/mydir/a.py"), "if");
    for (List<Integer> match : r.findMatches()) {
      for (int i : match) {
        System.out.print(i + " ");
      }
      System.out.println();
    }
  }
}
