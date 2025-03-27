package org.pianomyn.gred.matching;

import java.util.List;
import java.util.Map;
import org.pianomyn.gred.matching.algorithms.BoyerMoore;
import org.pianomyn.gred.matching.algorithms.KMP;
import org.pianomyn.gred.matching.algorithms.Naive;
import org.pianomyn.gred.matching.algorithms.RabinKarp;
import org.pianomyn.gred.reading.LineReader;

public class MatchingAlgorithmFactory {
  public static MatchingAlgorithm create(
      Map<String, List<List<Integer>>> matches,
      Algorithm algorithmType,
      LineReader lineReader,
      String pattern) {
    switch (algorithmType) {
      case NAIVE:
        return new Naive(matches, lineReader, pattern);
      case RABIN_KARP:
        return new RabinKarp(matches, lineReader, pattern);
      case BOYER_MOORE:
        return new BoyerMoore(matches, lineReader, pattern);
      case KMP:
        return new KMP(matches, lineReader, pattern);
      default:
        throw new IllegalArgumentException("Unknown algorithm type: " + algorithmType);
    }
  }
}
