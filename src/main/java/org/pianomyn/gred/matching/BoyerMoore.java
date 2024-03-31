package org.pianomyn.gred.matching;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
     *    If mismatching character doesnt exist in pattern, shift by length of pattern
     *    If exists, shift to rightmost occurrence to the left of the mismatch
     *    For both, consult bad char table
     */
    List<List<Integer>> result = new ArrayList<List<Integer>>();
    return result;
  }
}

