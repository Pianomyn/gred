package org.pianomyn.gred;

import org.apache.commons.cli.*;
import org.pianomyn.gred.matching.Algorithm;
import org.pianomyn.gred.orchestration.Orchestrator;

import static java.lang.System.exit;

public class Main {
  public static void main(String[] args) {
    /*
    Parse arguments (ensure flag combo is valid - only 1)
      Option is like flag
      CommandLineParser + Options
    Create an orchestrator based on flags (defaults if none)
    Make orchestrator do stuff
    Fetch results (index matches) from orchestrator
     */

    Options options = new Options();
    options.addOption(Algorithm.NAIVE.flag, "Naive");
    options.addOption(Algorithm.RABIN_KARP.flag, "Rabin-Karp");
    options.addOption(Algorithm.BOYER_MOORE.flag, "Boyer-Moore");
    options.addOption(Algorithm.KMP.flag, "Knuth-Morris-Pratt");

    CommandLineParser parser = new DefaultParser();
    CommandLine line = null;
    try {
      line = parser.parse(options, args);
    } catch(ParseException e) {
      System.err.println("Uh oh. " + e.getMessage());
    }
    if(line == null) {
      System.out.println("Uh oh 2. Should not reach here.");
      exit(1);
    }

    String[] remainingArgs = line.getArgs();
    // directory, pattern, algorithm
    Orchestrator orchestrator = new Orchestrator(null, null, null);

    if(line.hasOption(Algorithm.NAIVE.flag)) {
      orchestrator.setAlgorithmType(Algorithm.NAIVE);
    }
    else if(line.hasOption(Algorithm.RABIN_KARP.flag)) {
      orchestrator.setAlgorithmType(Algorithm.RABIN_KARP);
    }
    else if(line.hasOption(Algorithm.BOYER_MOORE.flag)) {
      orchestrator.setAlgorithmType(Algorithm.BOYER_MOORE);
    }
    else if(line.hasOption(Algorithm.KMP.flag)) {
      orchestrator.setAlgorithmType(Algorithm.KMP);
    } else {
      orchestrator.setAlgorithmType(Algorithm.BOYER_MOORE);
    }

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
