package org.pianomyn.gred.matching;

import org.pianomyn.gred.reading.LineReader;

import java.util.List;
import java.util.Map;

public class MatchingAlgorithmFactory {
  public static MatchingAlgorithm create(
          Map<String, List<List<Integer>>> matches,
          Algorithm algorithmType,
          LineReader lineReader,
          String pattern
  ) {
    switch (algorithmType) {
      case BOYER_MOORE:
        return new BoyerMoore(matches, lineReader, pattern);
      case KMP:
        return new KMP(lineReader, pattern);
      case NAIVE:
        return new BoyerMoore(lineReader, pattern);
      case RABIN_KARP:
        return new BoyerMoore(lineReader, pattern);
      default:
        throw new IllegalArgumentException("Unknown algorithm type: " + algorithmType);
    }
  }
}
