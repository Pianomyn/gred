package org.pianomyn.gred;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.pianomyn.gred.matching.MatchingAlgorithm;
import org.pianomyn.gred.matching.RabinKarp;
import org.pianomyn.gred.traversal.BFS;

public class Main {
  public static void main(String[] args) {
    int n = args.length;
    Path pathToSearch = null;
    MatchingAlgorithm matchingAlgorithm = null;
    String pattern = null;
    BFS traversal = null;

    boolean expectingFlagValue = false;

    if (n < 2) {
      Main.printHelpMessage();
    }

    for (String arg : args) {
      if (arg.isEmpty()) {
        printHelpMessage();
        break;
      }

      if (expectingFlagValue) {
      } else if (arg.charAt(0) == '-') {
        // Handle flag
      } else if (pattern == null) {
        pattern = arg;
      } else if (pathToSearch == null) {
        pathToSearch = Paths.get(
            arg.toString().replace("~", System.getProperty("user.home")));
        // pathToSearch = Paths.get(arg);
        if (!Files.exists(pathToSearch)) {
          Main.printPathDoesntExist(pathToSearch.toString());
          break;
        }
      }
    }

    if (pathToSearch != null && pattern != null) {
      traversal = new BFS(pathToSearch);
      matchingAlgorithm = new RabinKarp(pathToSearch, pattern);

      Queue<Path> files = traversal.traverse();

      while (!files.isEmpty()) {
        pathToSearch = files.poll();
        matchingAlgorithm.setPathToSearch(pathToSearch);
        List<List<Integer>> matches = matchingAlgorithm.findMatches();
        for (List<Integer> match : matches) {
          if (match.size() != 2) {
            // Should use custom exception
            System.out.println(String.format(
                "Something went wrong. Path to Search %s match %s",
                pathToSearch.toString(), match));
            break;
          }
          printMatch(pathToSearch.toString(), match.get(0), match.get(1));
        }
      }
    }
  }

  public static void printHelpMessage() {
    System.out.println("Usage: docker run gred PATTERN DIRECTORY [FLAGS]");
  }
  public static void printPathDoesntExist(String path) {
    System.out.println(
        String.format("No file or directory exists at that path %s", path));
  }

  public static void printMatch(String filePath, int row, int col) {
    System.out.println(
        String.format(String.format("%s %s %s", filePath, row, col)));
  }
}
