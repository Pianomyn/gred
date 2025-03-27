package org.pianomyn.gred.matching.algorithms;

import java.util.List;
import java.util.Map;

import org.pianomyn.gred.matching.MatchingAlgorithm;
import org.pianomyn.gred.reading.LineReader;

public class KMP extends MatchingAlgorithm {
  public KMP(Map<String, List<List<Integer>>> matches, LineReader reader, String pattern) {
    super(matches, reader, pattern);
  }

  private int[] constructPrefix() {
    return new int[] {};
  }

  @Override
  public List<List<Integer>> findMatches() {
    return List.of();
  }
}
