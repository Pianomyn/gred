package org.pianomyn.gred;

import static java.lang.System.exit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.*;
import org.pianomyn.gred.matching.AlgorithmType;
import org.pianomyn.gred.orchestration.Orchestrator;

public class Main {
  public static void main(String[] args) {
    Orchestrator orchestrator = initialiseOrchestrator(args);
    try {
      orchestrator.findMatches();
    } catch (IOException e) {
    }

    printMatches(orchestrator.getMatches());
  }

  private static Orchestrator initialiseOrchestrator(String[] args) {
    Options options = new Options();
    options.addOption(AlgorithmType.NAIVE.flag, "Naive");
    options.addOption(AlgorithmType.RABIN_KARP.flag, "Rabin-Karp");
    options.addOption(AlgorithmType.BOYER_MOORE.flag, "Boyer-Moore");
    options.addOption(AlgorithmType.KMP.flag, "Knuth-Morris-Pratt");
    options.addOption("jar", "Needed to run bundled file");
    options.addOption("j", "jar", false, "Needed to run bundled file");
    options.addOption("t", "consumer-threads", true, "Needed to run bundled file");

    CommandLineParser parser = new DefaultParser();
    CommandLine line = null;
    try {
      line = parser.parse(options, args);
    } catch (ParseException e) {
      System.err.println("Uh oh. " + e.getMessage());
      exit(1);
    }
    if (line == null) {
      System.out.println("Uh oh 2. Should not reach here.");
      exit(2);
    }
    String[] remainingArgs = line.getArgs();

    int numConsumers = 1;
    if (line.hasOption("t") || line.hasOption("consumer-threads")) {
      numConsumers = Integer.parseInt(line.getOptionValue("t"));
    }

    Orchestrator orchestrator = new Orchestrator(null, null, null, numConsumers);
    if (remainingArgs[0].equals("docker")) {
      // TODO: WIP
      if (remainingArgs.length < 4 || remainingArgs.length > 5) {
        printHelpMessage();
        exit(3);
      }
      // TODO: Docker usage

    } else {
      if (remainingArgs.length > 2) {
        printHelpMessage();
        exit(4);
      }
      String pattern = remainingArgs[0];
      String directoryPath = remainingArgs.length == 2 ? remainingArgs[1] : "."; // TODO: check

      orchestrator.setPattern(pattern);
      orchestrator.setDirectoryPath(Paths.get(directoryPath));
    }

    if (line.hasOption("-h") || line.hasOption("--help")) {
      printHelpMessage();
      exit(0);
    } else if (line.hasOption(AlgorithmType.NAIVE.flag)) {
      orchestrator.setAlgorithmType(AlgorithmType.NAIVE);
    } else if (line.hasOption(AlgorithmType.RABIN_KARP.flag)) {
      orchestrator.setAlgorithmType(AlgorithmType.RABIN_KARP);
    } else if (line.hasOption(AlgorithmType.BOYER_MOORE.flag)) {
      orchestrator.setAlgorithmType(AlgorithmType.BOYER_MOORE);
    } else if (line.hasOption(AlgorithmType.KMP.flag)) {
      orchestrator.setAlgorithmType(AlgorithmType.KMP);
    } else {
      orchestrator.setAlgorithmType(AlgorithmType.BOYER_MOORE);
    }

    return orchestrator;
  }

  public static void printMatches(Map<String, List<List<Integer>>> matches) {
    for (Map.Entry<String, List<List<Integer>>> entry : matches.entrySet()) {
      String filename = entry.getKey();
      List<List<Integer>> positions = entry.getValue();

      try {
        List<String> allLines = Files.readAllLines(Paths.get(filename));

        for (List<Integer> pos : positions) {
          int lineNum = pos.get(0);
          int charPos = pos.get(1);

          if (lineNum > -1 && lineNum <= allLines.size() - 1) {
            String line = allLines.get(lineNum);
            System.out.printf("%s:%d:%d: %s%n", filename, lineNum, charPos, line);
          }
        }
      } catch (IOException e) {
        System.err.println("Error reading file: " + filename);
      }
    }
  }

  private static void printHelpMessage() {
    System.out.println(
        "Usage:\n"
            + "  docker run -v gred PATTERN [DIRECTORY] [FLAGS]\n"
            + "Or\n"
            + "  java Main PATTERN [DIRECTORY] [FLAGS]\n"
            + "Possible Flags:\n"
            + "  -nv: Naive\n"
            + "  -rf: Rabin-Karp\n"
            + "  -bm: Boyer-Moore\n"
            + "  -kmp: KMP\n");
  }

  private static void printPathDoesntExist(String path) {
    System.out.println(String.format("No file or directory exists at that path %s", path));
  }

  private static void printMatch(String filePath, int row, int col) {
    System.out.println(String.format(String.format("%s %s %s", filePath, row, col)));
  }
}
