package org.pianomyn.gred.matching;

import java.util.List;
import java.util.Map;
import org.pianomyn.gred.reading.LineReader;

public abstract class MatchingAlgorithm {
  protected LineReader reader;
  protected String pattern;
  protected Map<String, List<List<Integer>>> matches;

  public MatchingAlgorithm(
      Map<String, List<List<Integer>>> matches, LineReader reader, String pattern) {
    this.matches = matches;
    this.reader = reader;
    this.pattern = pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public void setReader(LineReader reader) {
    this.reader = reader;
  }

  public abstract List<List<Integer>> findMatches(); // [[line, starting index]]

  public List<List<Integer>> getMatches() {
    return this.matches.get(this.reader.getFilePath().toString());
  }
}
