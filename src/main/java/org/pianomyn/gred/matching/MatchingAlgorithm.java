package org.pianomyn.gred.matching;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class MatchingAlgorithm {
  protected Path filePath;
  protected String pattern;

  public MatchingAlgorithm(Path filePath, String pattern) {
    this.filePath = filePath;
    this.pattern = pattern;
  }

  public abstract List<List<Integer>> findMatches(); // [[line, starting index]]

  public boolean fileExists() {
    return Files.exists(this.filePath);
  }

  public void setPathToSearch(Path newPath) {
    this.filePath = newPath;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }
}
