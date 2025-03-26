package org.pianomyn.gred;

import org.pianomyn.gred.matching.MatchingAlgorithm;

public class Main {
  public record ParseArgsResult(MatchingAlgorithm matchingAlgorithm, String pattern) {}

  public static void main(String[] args) {
    // ParseArgsResult (args);
    /*
    Parse arguments (ensure flag combo is valid - only 1)
      Option is like flag
      CommandLineParser + Options
    Create an orchestrator based on flags (defaults if none)
    Make orchestrator do stuff
    Fetch results (index matches) from orchestrator
     */

    /*
    int n = args.length;
    Path pathToSearch = null;
    MatchingAlgorithm matchingAlgorithm = null;
    String pattern = null;
    BFS traversal = null;

    if (n < 2) {
      Main.printHelpMessage();
    }

    for (String arg : args) {
      if (arg.isEmpty()) {
        printHelpMessage();
        break;
      }

      if (arg.charAt(0) == '-') {
        if (arg.equals("-rk") || arg.equals("--rabin-karp")) {
          //matchingAlgorithm = new RabinKarp(pathToSearch, pattern);
        }
      } else if (pattern == null) {
        pattern = arg;
      } else if (pathToSearch == null) {
        pathToSearch = Paths.get(arg.toString().replace("~", System.getProperty("user.home")));
        // pathToSearch = Paths.get(arg);
        if (!Files.exists(pathToSearch)) {
          Main.printPathDoesntExist(pathToSearch.toString());
          break;
        }
      }
    }

    if (pathToSearch != null && pattern != null) {
      traversal = new BFS(pathToSearch);
      if (matchingAlgorithm == null) {
        //matchingAlgorithm = new BoyerMoore(pathToSearch, pattern);
      }

      Queue<Path> files = traversal.traverse();

      while (!files.isEmpty()) {
        pathToSearch = files.poll();
        LineReader reader = new BufferedLineReader(pathToSearch);

        //matchingAlgorithm.setPathToSearch(pathToSearch);
        List<List<Integer>> matches = matchingAlgorithm.findMatches();
        for (List<Integer> match : matches) {
          if (match.size() != 2) {
            // Should use custom exception
            System.out.println(
                String.format(
                    "Something went wrong, missing a line or column value. Path to Search %s match %s",
                    pathToSearch.toString(), match));
            break;
          }
          printMatch(pathToSearch.toString(), match.get(0), match.get(1));
        }
      }
    }
     */
  }

  /*
  private static ParseArgsResult parseArgs(String[] args) {
    for(String arg : args) {

    }
  }
   */

  public static void printHelpMessage() {
    System.out.println(
        "Usage: docker run gred PATTERN DIRECTORY [FLAGS]\n"
            + "Possible Flags:\n"
            + "-nv: Naive\n"
            + "-rf: Rabin-Karp\n"
            + "-bm: Boyer-Moore\n"
            + "-kmp: KMP\n");
  }

  public static void printPathDoesntExist(String path) {
    System.out.println(String.format("No file or directory exists at that path %s", path));
  }

  public static void printMatch(String filePath, int row, int col) {
    System.out.println(String.format(String.format("%s %s %s", filePath, row, col)));
  }
}
