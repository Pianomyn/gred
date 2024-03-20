package org.pianomyn.matching;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
}
