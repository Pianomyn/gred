package org.pianomyn.gred.orchestration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import org.pianomyn.gred.matching.MatchingAlgorithm;
import org.pianomyn.gred.matching.MatchingAlgorithmFactory;
import org.pianomyn.gred.matching.Algorithm;
import org.pianomyn.gred.reading.BufferedLineReader;
import org.pianomyn.gred.reading.LineReader;
import org.pianomyn.gred.traversal.BFS;

public class Orchestrator {
  Path directoryPath;
  String pattern;
  Algorithm algorithmType;
  Map<String, List<List<Integer>>> matches;

  public Orchestrator(Path directoryPath, String pattern, Algorithm algorithmType) {
    this.directoryPath = directoryPath;
    this.pattern = pattern;
    this.algorithmType = algorithmType;
    this.matches = new HashMap<>();
  }

  // Getters

  // Setters
  public void setAlgorithmType(Algorithm algorithm) {
    this.algorithmType = algorithm;
  }

  public void setDirectoryPath(Path directoryPath) {
    this.directoryPath = directoryPath;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }


  public List<List<Integer>> traverseAndFindMatches() throws IOException {
    LineReader reader = null;
    try {
      reader = new BufferedLineReader(directoryPath);
    } catch (IOException e) {
      e.printStackTrace();
      throw (e);
    }
    List<List<Integer>> matches = new ArrayList<>();
    BFS traversal = new BFS(directoryPath);
    MatchingAlgorithm matchingAlgorithm =
        MatchingAlgorithmFactory.create(algorithmType, reader, this.pattern);

    Queue<Path> filePaths = traversal.traverse();

    while (!filePaths.isEmpty()) {
      Path path = filePaths.poll();
      try {
        reader = new BufferedLineReader(path);
      } catch (IOException e) {
        // Handle the exception
        System.err.println("Error creating LineReader: " + e.getMessage());
      }
      matchingAlgorithm.setReader(reader);
      matches.addAll(matchingAlgorithm.findMatches());
    }

    return matches;
  }
}
