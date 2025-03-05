package org.pianomyn.gred.matching;

import java.util.List;
import org.pianomyn.gred.reading.LineReader;

public abstract class MatchingAlgorithm {
  private LineReader reader;
  private String pattern;

  public MatchingAlgorithm(LineReader reader, String pattern) {
    this.reader = reader;
    this.pattern = pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public LineReader getReader() {
    return this.reader;
  }

  public String getPattern() {
    return this.pattern;
  }

  public void setReader(LineReader reader) {
    this.reader = reader;
  }

  public abstract List<List<Integer>> findMatches(); // [[line, starting index]]
}
