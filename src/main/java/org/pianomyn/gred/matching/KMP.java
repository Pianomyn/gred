package org.pianomyn.gred.matching;

import java.util.List;
import org.pianomyn.gred.reading.LineReader;

public class KMP extends MatchingAlgorithm {
  public KMP(LineReader reader, String pattern) {
    super(reader, pattern);
  }

  private int[] constructPrefix() {
    return new int[] {};
  }

  @Override
  public List<List<Integer>> findMatches() {
    return List.of();
  }
}
